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
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.theme.fromHexToColor
import com.tecknobit.novacore.records.release.Release
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.*
import com.tecknobit.novacore.records.release.events.RejectedTag
import com.tecknobit.novacore.records.release.events.ReleaseEvent
import com.tecknobit.novacore.records.release.events.ReleaseStandardEvent
import com.tecknobit.novacore.records.release.events.ReleaseStandardEvent.*

/**
 * Function to create a badge for a [ReleaseStatus]
 *
 * @param releaseStatus: the status to use to create the badge
 * @param paddingStart: the padding from the start, default value 10.[dp]
 */
@Composable
fun ReleaseStatusBadge(
    releaseStatus: Release.ReleaseStatus,
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

/**
 * Function to create a badge for a [ReleaseStatus]
 *
 * @param releaseStatus: the status to use to create the badge
 * @param paddingStart: the padding from the start, default value 10.[dp]
 * @param onClick: the action to execute when the badge is clicked
 */
@Composable
fun ReleaseStatusBadge(
    releaseStatus: Release.ReleaseStatus,
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

/**
 * Function to display the content of the [ReleaseStatusBadge]
 *
 * @param releaseStatus: the status to use to create the badge
 */
@Composable
private fun ReleaseStatusBadgeContent(
    releaseStatus: Release.ReleaseStatus
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

/**
 * Function to create a specific color from the [ReleaseStatus]
 *
 * @return the specific color as [Color]
 */
fun Release.ReleaseStatus.createColor(): Color {
    return fromHexToColor(color)
}

/**
 * Function to create a badge for a [RejectedTag]
 *
 * @param tag: the tag to use to create the badge
 * @param isLastEvent: whether the tag is placed in the last event occurred in the release
 * @param onClick: the action to execute when the badge is clicked
 */
@Composable
fun ReleaseTagBadge(
    tag: RejectedTag,
    isLastEvent: Boolean,
    onClick: () -> Unit = {}
) {
    val modifier = Modifier
        .requiredWidthIn(
            min = 45.dp,
            max = 140.dp
        )
        .height(35.dp)
    val isAdded = tag.comment != null && tag.comment.isNotEmpty()
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
    if(((activeLocalSession.isVendor && tag.comment.isNullOrEmpty()) || !isLastEvent)) {
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

/**
 * Function to display the content of the [ReleaseTagBadge]
 *
 * @param tag: the tag to use to create the badge
 * @param textColor: the color of the text
 */
@Composable
private fun ReleaseTagContent(
    tag: ReleaseEvent.ReleaseTag,
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

/**
 * Function to create a specific color from the [ReleaseTag]
 *
 * @return the specific color as [Color]
 */
fun ReleaseEvent.ReleaseTag.createColor(): Color {
    return fromHexToColor(color)
}

/**
 * Function to get the specific message for a [ReleaseStandardEvent]
 *
 * @return the specific message as [Int]
 */
fun ReleaseStandardEvent.getMessage(): Int {
    return when(this.status) {
        Approved -> R.string.approved_timeline_message
        Alpha -> R.string.alpha_timeline_message
        Beta -> R.string.beta_timeline_message
        Latest -> R.string.latest_timeline_message
        else -> -1
    }
}