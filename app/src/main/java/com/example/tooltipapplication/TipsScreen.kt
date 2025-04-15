package com.example.tooltipapplication

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

import androidx.activity.compose.BackHandler
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipBottomSheet(mediaUrl: String?) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()

    val imageHeight = 200.dp
    val scrollState = rememberScrollState()

    // BackHandler to close sheet
    val isSheetVisible = sheetState.bottomSheetState.currentValue != SheetValue.Hidden
    BackHandler(enabled = isSheetVisible) {
        scope.launch { sheetState.bottomSheetState.hide() }
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Box {
                // Scrollable content with image placeholder collapsing effect
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight)
                            .background(Color.LightGray)
                    ) {
                        if (!mediaUrl.isNullOrBlank()) {
                            MediaContent(
                                mediaUrl = mediaUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch { sheetState.bottomSheetState.hide() }
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                                .background(Color.White, RoundedCornerShape(50))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    TooltipContent(modifier = Modifier.padding(16.dp))
                }
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        scope.launch { sheetState.bottomSheetState.expand() }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Show Tooltip", fontSize = 16.sp)
                }
            }
        }
    )
}

@Composable
fun TooltipContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Scan a barcode on a product or QR in the price tag to:",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(8.dp))
        BulletText("See its price")
        BulletText("Check availability")

        Spacer(Modifier.height(24.dp))
        Text("Collect your self-serve items", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        BulletText("Go to item location")

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabelValueBox("Article number", "301.586.59")
            LabelValueBox("Rack", "18")
            LabelValueBox("Section", "00")
        }

        Spacer(Modifier.height(16.dp))
        BulletText("Scan your item before putting in the bag/trolley")

        Spacer(Modifier.height(24.dp))
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart Icon",
            tint = Color.Black,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun MediaContent(mediaUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(mediaUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Tooltip Media",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}





/*@Composable
fun BulletText(text: String) {
    Row(modifier = Modifier.padding(bottom = 4.dp)) {
        Text("• ", fontSize = 16.sp)
        Text(text, fontSize = 16.sp, color = Color.Gray)
    }
}*/

@Composable
fun LabelValueBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Box(
            modifier = Modifier
                .background(Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(value, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}



