package com.amirnlz.stylora.pages.feedback.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val requestBuilder = remember(context, imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(300)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .crossfade(300)
    }

    AsyncImage(
        model = requestBuilder.build(),
        contentDescription = "FeedbackResponse image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
    )

}