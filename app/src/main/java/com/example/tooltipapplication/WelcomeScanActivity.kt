package com.example.tooltipapplication



import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
            TooltipScreen(mediaUrl = "https://your-url.com/media.mp4") // <-- Replace this
        }
    }
}

@Composable
fun TooltipScreen(mediaUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.LightGray)
        ) {
            MediaContent(mediaUrl = mediaUrl, modifier = Modifier.fillMaxSize())

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

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Scan a barcode on a product or QR in the price tag to:",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            BulletText("See its price")
            BulletText("Check availability")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Collect your self-serve items",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            BulletText("Go to item location")

            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("301.586.59", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("18", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("00", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BulletText("Scan your item before putting in the bag/trolley")

            Spacer(modifier = Modifier.height(24.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_cart), // Your cart icon
                contentDescription = "Cart",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Generate your OR and proceed to",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Navigate */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2451C4))
            ) {
                Text("Continue", color = Color.White, fontWeight = FontWeight.Bold)
            }
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
fun MediaContent(mediaUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    when {
        mediaUrl.endsWith(".mp4", true) || mediaUrl.endsWith(".mkv", true) -> {
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

        mediaUrl.endsWith(".gif", true) || mediaUrl.endsWith(".jpg", true) ||
                mediaUrl.endsWith(".jpeg", true) || mediaUrl.endsWith(".png", true) -> {
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
                modifier = modifier
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Unsupported media", color = Color.Black)
            }
        }
    }
}
