package com.tecknobit.nova.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.novacore.records.User
import com.tecknobit.novacore.records.User.Role
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Alpha
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Beta

/**
 * **customerColor** -> color for the [Role.Customer]
 */
private val customerColor = Alpha.createColor()

/**
 * **vendorColor** -> color for the [Role.Vendor]
 */
private val vendorColor = Beta.createColor()

/**
 * Function to create a badge for a [Role]
 *
 * @param background: the background color to use, default is [gray_background]
 * @param role: the role to use to create the badge
 */
@Composable
fun UserRoleBadge(
    background: Color = gray_background,
    role: User.Role
) {
    val badgeColor = if(role == User.Role.Customer)
        customerColor
    else
        vendorColor
    OutlinedCard (
        modifier = Modifier
            .requiredWidthIn(
                min = 65.dp,
                max = 100.dp
            )
            .wrapContentWidth()
            .height(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        ),
        border = BorderStroke(
            width = 1.dp,
            color = badgeColor
        )
    ) {
        Column (
            modifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = role.name,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
        }
    }
}

/**
 * Function to create a badge for a [Role]
 *
 * @param role: the role to use to create the badge
 * @param selected: whether the badge is selected or not
 * @param onClick: the action to execute when the badge is clicked
 */
@Composable
fun UserRoleBadge(
    role: User.Role,
    selected: MutableState<Boolean>,
    onClick: () -> Unit
) {
    val badgeColor = if(role == User.Role.Customer)
        customerColor
    else
        vendorColor
    OutlinedCard (
        modifier = Modifier
            .requiredWidthIn(
                min = 65.dp,
                max = 100.dp
            )
            .wrapContentWidth()
            .height(35.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(selected.value)
                badgeColor
            else
                gray_background
        ),
        border = BorderStroke(
            width = 1.dp,
            color = badgeColor
        ),
        onClick = onClick
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 10.dp,
                    end = 10.dp
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = role.name,
                fontWeight = FontWeight.Bold,
                color = if(selected.value)
                    Color.White
                else
                    badgeColor
            )
        }
    }
}