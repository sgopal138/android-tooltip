package com.example.tooltipapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun CheckoutScreen() {


    var showDialog = remember { mutableStateOf(false) }
    var currentDialogIndex = remember { mutableStateOf(0) }

    val checkoutItems = remember {
        mutableStateListOf(
            CheckoutItemData("SKOGSFRAKEN", "Pillow, high", "RM45", R.drawable.ic_cart, "Bedroom department", "123.456.78"),
            CheckoutItemData("POANG", "armchair", "RM309", R.drawable.ic_cart, "Self serve", "192.407.88")
        )
    }

    val dialogItems = checkoutItems.filter { it.location == "Self serve" }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            itemsIndexed(checkoutItems) { index, item ->
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

                CheckoutItem(
                    item = item,
                    onQuantityChange = { newQty -> checkoutItems[index] = checkoutItems[index].copy(quantity = newQty) },
                    onCollectedChange = { newValue -> checkoutItems[index] = checkoutItems[index].copy(collected = newValue) }
                )

                ProductCard()
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
            .background(Color(0x80000000)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Progress indicator
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
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Item row
                Row {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text(item.description, fontSize = 14.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Article number", fontSize = 12.sp, color = Color.Gray)
                        Text(item.id, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Section", fontSize = 10.sp, color = Color.Gray)
                                    Text("16", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Rack", fontSize = 10.sp, color = Color.Gray)
                                    Text("51", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = item.collected, onCheckedChange = {})
                    Text("Collected", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show skip only if not last item
                    if (index < total) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color.Black),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Text("Skip")
                        }

                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    // Next / Done button
                    Button(
                        onClick = onNext,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (index == total) "Close" else "Next",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
    var quantity: Int = 1,
    var collected: Boolean = false
)



@Composable
fun CheckoutItem(
    item: CheckoutItemData,
    onQuantityChange: (Int) -> Unit,
    onCollectedChange: (Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text(item.description, color = Color.Gray)

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Article number", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
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
                    Text(item.price, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuantitySelector(
                        quantity = item.quantity,
                        onIncrement = { onQuantityChange(item.quantity + 1) },
                        onDecrement = { if (item.quantity > 1) onQuantityChange(item.quantity - 1) }
                    )

                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "More Options")
                    }

                    if (showDialog) {
                        MoreOptionsDialog(
                            onDismiss = { showDialog = false },
                            onSaveAsFavourite = { /* save logic */ },
                            onDelete = {
                                showDialog = false
                                showDeleteConfirm = true
                            }
                        )
                    }

                    if (showDeleteConfirm) {
                        ConfirmDeleteDialog(
                            onConfirm = {
                                // Delete logic here
                                showDeleteConfirm = false
                            },
                            onDismiss = { showDeleteConfirm = false }
                        )
                    }


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = item.collected,
                            onCheckedChange = { onCollectedChange(it) }
                        )
                        Text("Collected", fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Are you sure you want\nto remove?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Yes", color = Color(0xFF007AFF), fontSize = 18.sp)
                }

                Divider()

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("No", color = Color(0xFF007AFF), fontSize = 18.sp)
                }
            }
        }
    }
}


@Composable
fun MoreOptionsDialog(
    onDismiss: () -> Unit,
    onSaveAsFavourite: () -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Save as favourite
                TextButton(
                    onClick = {
                        onSaveAsFavourite()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Save as favourite", color = Color(0xFF007AFF), fontSize = 18.sp)
                }

                Divider()

                // Delete
                TextButton(
                    onClick = {
                        onDelete()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Delete", color = Color(0xFF007AFF), fontSize = 18.sp)
                }

                Divider()

                // Cancel
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold, color = Color(0xFF007AFF), fontSize = 18.sp)
                }
            }
        }
    }
}




@Composable
fun QuantitySelector(quantity: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .clickable { onDecrement() },
            contentAlignment = Alignment.Center
        ) {
            Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(quantity.toString(), fontWeight = FontWeight.Bold)
        }

        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight()
                .clickable { onIncrement() },
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

@Composable
fun ProductCard() {
    var expanded by remember { mutableStateOf(true) }
    var quantity by remember { mutableStateOf(1) }
    val image = painterResource(R.drawable.ic_cart) // Replace with your image resource

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        // Product summary
        Row(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = image,
                contentDescription = "Chair",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("POANG", fontWeight = FontWeight.Bold)
                Text("armchair", color = Color.Gray)
                Text("192.407.88", color = Color.White, modifier = Modifier
                    .background(Color.Black)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Text("RM309", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        // Quantity controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(50))
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text("$quantity", fontWeight = FontWeight.Bold)
            IconButton(onClick = { quantity++ }) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Expandable section
        /*Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (expanded) "Hide packages (2)" else "Show packages (2)",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
            )
        }*/

        SectionDividerWithToggle(
            expanded = expanded,
            onToggle = { expanded = !expanded }
        )

        Spacer(Modifier.height(8.dp))

        if (expanded) {
            PackageItem("Armchair frame", "301.586.59", "16", "51")
            Spacer(Modifier.height(8.dp))
            PackageItem("Armchair cushion", "903.951.44", "16", "51")
        }
    }
}

@Composable
fun PackageItem(name: String, article: String, section: String, rack: String) {
    var collected by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("1x $name", fontWeight = FontWeight.Bold)
        Text("POÄNG", fontSize = 12.sp)

        Spacer(Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            InfoBox(text = article)
            Spacer(Modifier.width(8.dp))
            InfoBox(text = section)
            Spacer(Modifier.width(8.dp))
            InfoBox(text = rack)

            Spacer(Modifier.width(16.dp))

            Icon(Icons.Default.QrCode, contentDescription = "Barcode")
            Spacer(Modifier.width(4.dp))
            Checkbox(checked = collected, onCheckedChange = { collected = it })
            Text("Collected", fontSize = 12.sp)
        }
    }
}

@Composable
fun InfoBox(text: String) {
    Text(
        text,
        color = Color.White,
        modifier = Modifier
            .background(Color.Black, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        fontSize = 12.sp
    )
}


@Composable
fun SectionDividerWithToggle(
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(50))
                .clickable { onToggle() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (expanded) "Hide packages (2)" else "Show packages (2)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Divider(
            modifier = Modifier.weight(1f),
            color = Color.LightGray,
            thickness = 1.dp
        )
    }
}


