package com.example.tooltipapplication



import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class WelcomeScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TooltipScreen(mediaUrl = "https://fastly.picsum.photos/id/1/5000/3333.jpg?hmac=Asv2DU3rA_5D1xSe22xZK47WEAN0wjWeFOhzd13ujW4") // <-- Replace this
        }
    }
}

@Composable
fun TooltipScreen(mediaUrl: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // space for button
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.LightGray)
            ) {
                if (!mediaUrl.isNullOrBlank()) {
                    MediaContent(mediaUrl = mediaUrl, modifier = Modifier.fillMaxSize())
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No media available", color = Color.DarkGray)
                    }
                }

                IconButton(
                    onClick = { /* Handle close */ },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(Color.White, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)) {
                Text(
                    text = "Scan a barcode on a product or QR in the price tag to:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                BulletText("See its price")
                BulletText("Check availability")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Collect your self-serve items",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                BulletText("Go to item location")

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("301.586.59", "18", "00").forEach {
                        Box(
                            modifier = Modifier
                                .background(Color.Black, RoundedCornerShape(4.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(it, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                BulletText("Scan your item before putting in the bag/trolley")

                Spacer(modifier = Modifier.height(24.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_cart),
                    contentDescription = "Cart",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Generate your QR and proceed to checkout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // Button fixed at bottom
        Button(
            onClick = { /* Handle click */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2451C4))
        ) {
            Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}



@Composable
fun BulletText(text: String) {
    Row(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
        Text("• ", fontSize = 14.sp)
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun MediaContent(mediaUrl: String?, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    if (mediaUrl.isNullOrBlank()) {
        // Placeholder for null or empty URL
        Box(
            modifier = modifier.background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("No media available", color = Color.DarkGray)
        }
        return
    }

    val extension = getFileExtensionFromUrl(mediaUrl)

    when {
        extension in listOf("mp4", "mkv") -> {
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(Uri.parse(mediaUrl)))
                    prepare()
                    playWhenReady = false
                }
            }

            DisposableEffect(Unit) {
                onDispose { exoPlayer.release() }
            }

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        useController = true
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = modifier
            )
        }

        extension in listOf("jpg", "jpeg", "png", "gif") -> {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(mediaUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }

        else -> {
            Box(
                modifier = modifier.background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Unsupported media format", color = Color.DarkGray)
            }
        }
    }
}

fun getFileExtensionFromUrl(url: String): String {
    return Uri.parse(url).lastPathSegment
        ?.substringAfterLast('.', "")
        ?.lowercase() ?: ""
}
