package com.example.tooltipapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CheckoutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top section (list of items)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(1) { // You can make this dynamic later
                CheckoutItem()
            }
        }

        // Bottom section (Subtotal and Generate QR button)
        CheckoutFooter()
    }
}

@Composable
fun CheckoutItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cart), // Replace with your pillow image
                contentDescription = "Pillow",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("SKOGSFRAKEN", fontWeight = FontWeight.Bold)
                Text("Pillow, high")
                Text("123.456.78", fontWeight = FontWeight.Bold, modifier = Modifier
                    .background(Color.Black)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp)), color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("RM45", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuantitySelector()
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {}
                )
                Text("Collected")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("This item located at ", color = Color.Gray)
            Text("Bedroom department", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun QuantitySelector() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text("-", fontSize = 18.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Text("1", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(16.dp))
        Text("+", fontSize = 18.sp)
    }
}

@Composable
fun CheckoutFooter() {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
            .border(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Subtotal", fontWeight = FontWeight.Bold)
                Text(
                    "Prices might be different during the checkout",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text("RM45", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Generate QR logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0058A3))
        ) {
            Text("Generate QR to check out")
        }
    }
}
