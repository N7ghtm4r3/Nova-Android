package com.tecknobit.nova.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Alpha
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Approved
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Beta
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Latest
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.RejectedTag
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
    tag: RejectedTag,
    onClick: () -> Unit = {}
) {
    val modifier = Modifier
        .requiredWidthIn(
            min = 45.dp,
            max = 140.dp
        )
        .height(35.dp)
    val isAdded = tag.comment.isNotEmpty()
    val tagColor = tag.tag.createColor()
    val colors = CardDefaults.cardColors(
        containerColor = if(isAdded)
            tagColor
        else
            Color.White
    )
    val textColor = if(!isAdded)
        tagColor
    else
        Color.White
    val border = BorderStroke(
        width = 1.dp,
        color = textColor
    )
    // TODO: MAKE THE REAL WORKFLOW TO SELECT THE CORRECT BADGE IF IS THE CLIENT SHOW ONCLICK POSSIBILITY ELSE HIDE TO THE VENDOR
    val isVendor = false
    if(isVendor) {
        OutlinedCard(
            modifier = modifier,
            colors = colors,
            border = border
        ) {
            ReleaseTagContent(
                tag = tag.tag,
                textColor = textColor
            )
        }
    } else {
        OutlinedCard(
            modifier = modifier,
            colors = colors,
            border = border,
            onClick = onClick
        ) {
            ReleaseTagContent(
                tag = tag.tag,
                textColor = textColor
            )
        }
    }
}

@Composable
private fun ReleaseTagContent(
    tag: ReleaseTag,
    textColor: Color
) {
    Column (
        modifier = Modifier
            .fillMaxHeight()
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
            color = textColor,
            fontSize = 14.sp
        )
    }
}

fun ReleaseTag.createColor(): Color {
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