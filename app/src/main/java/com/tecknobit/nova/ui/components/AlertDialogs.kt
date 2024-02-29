package com.tecknobit.nova.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.tecknobit.nova.R

@Composable
fun NovaAlertDialog(
    show: MutableState<Boolean>,
    icon: ImageVector,
    onDismissAction: () -> Unit = { show.value = false },
    title: Int,
    message: Int,
    dismissAction: () -> Unit = { show.value = false },
    confirmAction: () -> Unit
) {
    NovaAlertDialog(
        show = show,
        icon = icon,
        onDismissAction = onDismissAction,
        title = {
            Text(
                text = stringResource(title)
            )
        },
        message = {
            Text(
                text = stringResource(message),
                textAlign = TextAlign.Justify
            )
        },
        dismissAction = dismissAction,
        confirmAction = confirmAction
    )
}

@Composable
fun NovaAlertDialog(
    show: MutableState<Boolean>,
    icon: ImageVector,
    onDismissAction: () -> Unit = { show.value = false },
    title: Int,
    message: @Composable (() -> Unit),
    dismissAction: () -> Unit = { show.value = false },
    confirmAction: () -> Unit
) {
    NovaAlertDialog(
        show = show,
        icon = icon,
        onDismissAction = onDismissAction,
        title = {
            Text(
                text = stringResource(title)
            )
        },
        message = message,
        dismissAction = dismissAction,
        confirmAction = confirmAction
    )
}

@Composable
fun NovaAlertDialog(
    show: MutableState<Boolean>,
    icon: ImageVector,
    onDismissAction: () -> Unit = { show.value = false },
    title: @Composable (() -> Unit),
    message: @Composable (() -> Unit),
    dismissAction: () -> Unit = { show.value = false },
    confirmAction: () -> Unit
) {
    if(show.value) {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            },
            onDismissRequest = onDismissAction,
            title = title,
            text = message,
            dismissButton = {
                TextButton(
                    onClick = dismissAction
                ) {
                    Text(
                        text = stringResource(R.string.dismiss)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = confirmAction
                ) {
                    Text(
                        text = stringResource(R.string.confirm)
                    )
                }
            }
        )
    }
}