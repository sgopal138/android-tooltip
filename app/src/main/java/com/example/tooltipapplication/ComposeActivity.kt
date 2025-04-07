package com.example.tooltipapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val storeList = listOf("IKEA Cheras", "IKEA Damansara", "IKEA Penang", "IKEA Johor")
            StoreSelectionScreen(storeList)
        }
    }
}

@Composable
fun StoreSelectionScreen(storeList: List<String>) {
    var selectedStore by remember { mutableStateOf(storeList.first()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button and Title
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Select store", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Store Icon
        Icon(
            painter = painterResource(id = android.R.drawable.ic_dialog_info),
            contentDescription = "Store Icon",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(text = "Select your current store", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic Store List
        storeList.forEach { storeName ->
            StoreOption(storeName, selectedStore) { selectedStore = it }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Button
        Button(
            onClick = { /* Handle Start Scanning Action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Start scanning", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun StoreOption(storeName: String, selectedStore: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(storeName) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_dialog_map),
            contentDescription = "Store Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = storeName, fontSize = 18.sp, modifier = Modifier.weight(1f))
        RadioButton(
            selected = storeName == selectedStore,
            onClick = { onSelect(storeName) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoreSelectionScreen() {
    val storeList = listOf("IKEA Cheras", "IKEA Damansara", "IKEA Penang", "IKEA Johor")
    StoreSelectionScreen(storeList)
}
