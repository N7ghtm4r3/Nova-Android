package com.tecknobit.nova.ui.activities

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.tecknobit.nova.helpers.utils.ui.SnackbarLauncher
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

abstract class NovaActivity: ComponentActivity() {

    /**
     * **snackbarLauncher** -> the launcher used to display the [Snackbar]
     */
    protected lateinit var snackbarLauncher: SnackbarLauncher

    protected lateinit var refreshRoutine: CoroutineScope

    protected lateinit var currentContext: Context

    protected var isRefreshing: Boolean = false

    @Composable
    protected fun InitLauncher() {
        snackbarLauncher = SnackbarLauncher(LocalContext.current)
        snackbarLauncher.InitSnackbarInstances()
    }

    protected fun canRefresherStarts(): Boolean {
        return !isRefreshing && activeLocalSession.isHostSet
    }

    protected fun suspendRefresher() {
        refreshRoutine.coroutineContext.cancelChildren()
        isRefreshing = false
    }

    protected fun checkToSetErrorMessage(
        errorMessage: MutableState<String>,
        errorMessageKey: Int,
        error: MutableState<Boolean>
    ) {
        errorMessage.value =
            if(error.value)
                currentContext.getString(errorMessageKey)
            else
                ""
    }

    protected fun setErrorMessage(
        errorMessage: MutableState<String>,
        errorMessageKey: Int,
        error: MutableState<Boolean>
    ) {
        setErrorMessage(
            errorMessage = errorMessage,
            errorMessageValue = currentContext.getString(errorMessageKey),
            error = error
        )
    }

    protected fun setErrorMessage(
        errorMessage: MutableState<String>,
        errorMessageValue: String,
        error: MutableState<Boolean>
    ) {
        errorMessage.value = errorMessageValue
        error.value = true
    }

    protected fun continueToFetch(
        currentActivity: NovaActivity
    ) : Boolean {
        return activeActivity == currentActivity
    }

}