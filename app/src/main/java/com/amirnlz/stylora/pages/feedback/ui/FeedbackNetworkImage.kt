package com.amirnlz.stylora.pages.feedback.ui

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun FeedbackNetworkImage(
    imageUrl: String,
    size: Int = 200
) {
    val context = LocalContext.current
    val imageUrl = remember { imageUrl }
    Log.d("FeedbackNetworkImage", "Loading image from URL: $imageUrl")

    val requestBuilder = remember(context, imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl.replace("http://", "https://"))
            .crossfade(300)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .crossfade(300)
            .listener(
                onStart = { Log.d("Coil", "Loading started") },
                onError = { _, throwable ->
                    Log.e("Coil", "Error loading image: $throwable")
                },
                onSuccess = { _, _ -> Log.d("Coil", "Loaded successfully") }
            )
    }

    AsyncImage(
        model = requestBuilder.build(),
        contentDescription = "FeedbackResponse image",
        contentScale = ContentScale.Fit,
        placeholder = ColorPainter(Color.LightGray),
        error = ColorPainter(Color.Red),
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}