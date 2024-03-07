package com.tecknobit.nova.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun Logo(
    url: String
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .size(60.dp),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EmptyList(
    icon: ImageVector,
    description: Int
) {
    EmptyList(
        icon = rememberVectorPainter(icon),
        description = description
    )
}

@Composable
fun EmptyList(
    icon: Painter,
    description: Int
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(125.dp),
            painter = icon,
            contentDescription = null,
            tint = Color(200, 203, 210)
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 10.dp
                ),
            text = stringResource(description),
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}