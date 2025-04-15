package com.example.tooltipapplication.utils

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem

@Composable
fun loadNetworkMedia(
    url: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 0.dp,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (() -> Unit)? = null
) {
    when {
        url.endsWith(".mp4") || url.endsWith(".webm") -> {
            loadNetworkVideo(url, modifier, cornerRadius)
        }

        else -> {
            val imageModifier = modifier
                .then(
                    if (cornerRadius > 0.dp) Modifier.clip(RoundedCornerShape(cornerRadius)) else Modifier
                )
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = "Network Image",
                contentScale = contentScale,
                modifier = imageModifier
            )
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun loadNetworkVideo(
    url: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 0.dp
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(url)))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(cornerRadius))
    )
}

@Composable
fun loadDrawableImage(
    @DrawableRes resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    cornerRadius: Dp = 0.dp,
    tintColor: Color? = null,
    onClick: (() -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val shapeModifier = if (cornerRadius > 0.dp) {
        modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
    } else {
        modifier.size(size)
    }

    val clickableModifier = if (onClick != null) {
        shapeModifier.clickable { onClick() }
    } else {
        shapeModifier
    }

    Image(
        painter = painterResource(id = resId),
        contentDescription = contentDescription,
        contentScale = contentScale,
        colorFilter = tintColor?.let { ColorFilter.tint(it) },
        modifier = clickableModifier
    )
}
