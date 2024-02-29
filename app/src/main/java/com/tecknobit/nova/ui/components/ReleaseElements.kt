package com.tecknobit.nova.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Alpha
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Approved
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Beta
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Latest
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseStandardEvent
import com.tecknobit.nova.ui.theme.fromHexToColor

@Composable
fun ReleaseStatusBadge(
    releaseStatus: ReleaseStatus,
    paddingStart: Dp = 10.dp
) {
    OutlinedCard (
        modifier = Modifier
            .padding(
                start = paddingStart
            )
            .requiredWidthIn(
                min = 65.dp,
                max = 100.dp
            )
            .wrapContentWidth()
            .height(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = releaseStatus.createColor()
        )
    ) {
        ReleaseStatusBadgeContent(
            releaseStatus = releaseStatus
        )
    }
}

@Composable
fun ReleaseStatusBadge(
    releaseStatus: ReleaseStatus,
    paddingStart: Dp = 10.dp,
    onClick: () -> Unit
) {
    OutlinedCard (
        modifier = Modifier
            .padding(
                start = paddingStart
            )
            .requiredWidthIn(
                min = 65.dp,
                max = 100.dp
            )
            .wrapContentWidth()
            .height(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = releaseStatus.createColor()
        ),
        onClick = onClick
    ) {
        ReleaseStatusBadgeContent(
            releaseStatus = releaseStatus
        )
    }
}

@Composable
private fun ReleaseStatusBadgeContent(
    releaseStatus: ReleaseStatus
) {
    Column (
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = releaseStatus.name,
            fontWeight = FontWeight.Bold,
            color = releaseStatus.createColor()
        )
    }
}

fun ReleaseStatus.createColor(): Color {
    return fromHexToColor(color)
}

@Composable
fun ReleaseTagBadge(
    tag: ReleaseTag,
    onClick: () -> Unit
) {
    val tagColor = tag.createColor()
    OutlinedCard (
        modifier = Modifier
            .requiredWidthIn(
                min = 40.dp,
                max = 140.dp
            )
            .height(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = tagColor
        ),
        onClick = onClick
    ) {
        Column (
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = tag.name,
                fontWeight = FontWeight.Bold,
                color = tagColor
            )
        }
    }
}

private fun ReleaseTag.createColor(): Color {
    return fromHexToColor(color)
}

fun ReleaseStandardEvent.getMessage(): Int {
    return when(this.status) {
        Approved -> R.string.approved_timeline_message
        Alpha -> R.string.alpha_timeline_message
        Beta -> R.string.beta_timeline_message
        Latest -> R.string.latest_timeline_message
        else -> -1
    }
}