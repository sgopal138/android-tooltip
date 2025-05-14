package com.example.tooltipapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


    var showDialog = remember { mutableStateOf(false) }
    var currentDialogIndex = remember { mutableStateOf(0) }

    val checkoutItems = listOf(
        CheckoutItemData("SKOGSFRAKEN", "Pillow, high", "RM45", R.drawable.ic_cart, "Bedroom department", "123.456.78", true),
        CheckoutItemData("POANG", "armchair", "RM309", R.drawable.ic_cart, "Self serve", "192.407.88", false)
    )

    val dialogItems = checkoutItems.filter { it.location == "Self serve" }

    Box(modifier = Modifier.fillMaxSize() .background(Color.White)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            itemsIndexed(checkoutItems) { index, item ->
                // Insert header before the item where location is "Self serve"
                if (item.location == "Self serve" && (index == 0 || checkoutItems[index - 1].location != "Self serve")) {
                    Text(
                        "Collect from Self serve",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(12.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                CheckoutItem(item)
            }
        }


        // Fixed footer
        CheckoutFooter(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            onGenerateQRClick = { showDialog.value = true }
        )

        if (showDialog.value && dialogItems.isNotEmpty()) {
            PickupDialog(
                item = dialogItems[currentDialogIndex.value],
                index = currentDialogIndex.value + 1,
                total = dialogItems.size,
                onDismiss = { showDialog.value = false },
                onNext = {
                    if (currentDialogIndex.value < dialogItems.size - 1) {
                        currentDialogIndex.value += 1
                    } else {
                        showDialog.value = false
                        currentDialogIndex.value = 0
                    }
                }
            )
        }
    }
}

@Composable
fun PickupDialog(
    item: CheckoutItemData,
    index: Int,
    total: Int,
    onDismiss: () -> Unit,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "$index of $total",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Find your items to pick up.", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                "You can find the items by checking the section and rack number.",
                fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(item.name, fontWeight = FontWeight.Bold)
                    Text(item.description)
                    Text("Article number", fontSize = 12.sp, color = Color.Gray)
                    Text(item.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        Column(
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Section", fontSize = 10.sp, color = Color.Gray)
                            Text("16", fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Rack", fontSize = 10.sp, color = Color.Gray)
                            Text("51", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = item.collected, onCheckedChange = {})
                Text("Collected", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Skip")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNext, modifier = Modifier.weight(1f)) {
                    Text(if (index == total) "Done" else "Next")
                }
            }
        }
    }
}




data class CheckoutItemData(
    val name: String,
    val description: String,
    val price: String,
    val imageRes: Int,
    val location: String,
    val id: String,
    val collected: Boolean
)


@Composable
fun CheckoutItem(item: CheckoutItemData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            // 🖼 Image on the left
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // ➡ Column with two rows to the right of the image
            Column(modifier = Modifier.weight(1f)) {

                // 🔝 Top Row: name, description, price
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text(item.description, color = Color.Gray)

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Text("Article number", fontSize = 12.sp, color = Color.Gray)
//                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.Black)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(item.id, color = Color.White, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        item.price,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🔻 Bottom Row: quantity, ⋯, collected
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuantitySelector()

                    Text("⋯", fontSize = 20.sp)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = item.collected, onCheckedChange = null)
                        Text("Collected", fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ℹ️ Location info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("This item located at ", color = Color.Gray, fontSize = 12.sp)
            Text(item.location, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}


@Composable
fun QuantitySelector() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
    ) {
        // Decrement button
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // Quantity count
        Box(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text("1", fontWeight = FontWeight.Bold)
        }

        // Increment button
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}


/*@Composable
fun CheckoutFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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
            Text("RM2,460", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { *//* Generate QR logic *//* },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0058A3))
        ) {
            Icon(Icons.Default.QrCode, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate QR to check out")
        }
    }
}*/

@Composable
fun CheckoutFooter(
    modifier: Modifier = Modifier,
    onGenerateQRClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
//            .border(1.dp, Color.LightGray)
    ) {
        // Black separator line
        Divider(
            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        // Subtotal section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text("Subtotal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    "Prices might be different during the checkout",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                "RM2,460",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // QR icon + main button row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // QR Icon inside white circle with border
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .border(1.dp, Color.Black, RoundedCornerShape(50))
                    .clickable { onGenerateQRClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Main checkout button
            Button(
                onClick = onGenerateQRClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0058A3))
            ) {
                Text(
                    text = "Generate QR to check out",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}




