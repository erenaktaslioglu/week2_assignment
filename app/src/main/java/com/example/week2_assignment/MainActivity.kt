package com.example.week2_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.week2_assignment.ui.theme.Week2_assignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week2_assignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        DevClusterScreen(hardwareList = donanimListesi)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Week2_assignmentTheme {
        Greeting("Android")
    }
}

@Immutable
data class HardwareItem(
    val id: String,
    val name: String,
    val category: String,
    val cost: Double,
    val stockCount: Int
)
val donanimListesi = listOf(
    HardwareItem(id="101", name="MacBook Pro M3 Max", category="Geliştirici Bilgisayarı", cost=3499.0, stockCount=5),
    HardwareItem(id="102", name="NVIDIA RTX 4090 Sunucu", category="Yapay Zeka / GPU", cost=1599.0, stockCount=2),
    HardwareItem(id="103", name="Google Pixel 8 Pro", category="Mobil Test Cihazı", cost=999.0, stockCount=8),
    HardwareItem(id="104", name="Dell UltraSharp 49\"", category="Monitör", cost=1199.0, stockCount=4),
    HardwareItem(id="105", name="Sony WH-1000XM5 Kulaklık", category="Odaklanma Ekipmanı", cost=399.0, stockCount=12),
    HardwareItem(id="106", name="Raspberry Pi 5 8GB", category="IoT Geliştirme Kiti", cost=85.0, stockCount=25),
    HardwareItem(id="107", name="HHKB Professional Keyboard", category="Ergonomik Klavye", cost=299.0, stockCount=0),
    HardwareItem(id="108", name="AMD Ryzen Threadripper", category="Yapay Zeka / GPU", cost=4999.0, stockCount=1)
)

@Composable
fun HardwareSearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        label = { Text("Donanım Ara...") }
    )
}

@Composable
fun HardwareCard(item: HardwareItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Kategori: ${item.category}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fiyat: $${item.cost}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Stok: ${item.stockCount}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun DevClusterScreen(hardwareList: List<HardwareItem>) {
    // B. Konfigürasyon Değişim Koruması
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // B. Turnike Mantığı (Memoization)
    val filteredList = remember(searchQuery, hardwareList) {
        if (searchQuery.isBlank()) hardwareList else hardwareList.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column {
        HardwareSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        // C. Tembel Yükleme ve Nokta Atışı Recomposition
        LazyColumn {
            items(
                items = filteredList,
                key = { it.id }
            ) { item ->
                HardwareCard(item = item, onClick = { })
            }
        }
    }
}
