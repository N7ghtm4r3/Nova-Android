package com.tecknobit.nova.helpers.utils.ui

import android.content.Context
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarLauncher(
    private val context: Context
) {

    private lateinit var scope: CoroutineScope

    private lateinit var snackbarHostState: SnackbarHostState

    @Composable
    fun InitSnackbarInstances() {
        scope = rememberCoroutineScope()
        snackbarHostState = remember { SnackbarHostState() }
    }

    @Composable
    fun CreateSnackbarHost() {
        SnackbarHost(hostState = snackbarHostState) {
            Snackbar(
                containerColor = md_theme_light_primary,
                contentColor = gray_background,
                snackbarData = it
            )
        }
    }

    fun showSnackError(
        message: Int,
        isError: MutableState<Boolean>
    ) {
        showSnack(context.getString(message))
        isError.value = true
    }

    fun showSnack(
        message: Int
    ) {
        showSnack(context.getString(message))
    }

    fun showSnack(
        message: String
    ) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

}