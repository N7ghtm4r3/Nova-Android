package com.tecknobit.nova.ui.activities

import androidx.activity.ComponentActivity
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.tecknobit.nova.helpers.utils.ui.SnackbarLauncher
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.novacore.records.NovaItem

abstract class NovaActivity: ComponentActivity() {

    /**
     * **snackbarLauncher** -> the launcher used to display the [Snackbar]
     */
    protected lateinit var snackbarLauncher: SnackbarLauncher

    @Composable
    protected fun InitLauncher() {
        snackbarLauncher = SnackbarLauncher(LocalContext.current)
        snackbarLauncher.InitSnackbarInstances()
    }

    protected fun continueToFetch(
        currentActivity: NovaActivity
    ) : Boolean {
        return activeActivity == currentActivity
    }

    interface ListFetcher<T : NovaItem> {

        fun refreshList()

    }

    interface ItemFetcher<T : NovaItem> {

        fun refreshItem()

    }

}