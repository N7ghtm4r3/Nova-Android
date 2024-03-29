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
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Alpha
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Beta
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.users.User.Role
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.users.User.Role.Customer
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.novacore.records.User

private val customerColor = Alpha.createColor()

private val vendorColor = Beta.createColor()

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

// TODO: TO REPLACE WITH THE OFFICIAL
@Composable
fun UserRoleBadge(
    background: Color = gray_background,
    role: Role
) {
    val badgeColor = if(role == Customer)
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

@Composable
fun UserRoleBadge(
    role: Role,
    selected: MutableState<Boolean>,
    onClick: () -> Unit
) {
    val badgeColor = if(role == Customer)
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