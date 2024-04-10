package com.tecknobit.nova.ui.activities

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ehsanmsz.mszprogressindicator.progressindicator.BallClipRotatePulseProgressIndicator
import com.tecknobit.apimanager.annotations.Structure
import com.tecknobit.nova.helpers.utils.ui.SnackbarLauncher
import com.tecknobit.nova.ui.activities.navigation.MainActivity.Companion.projects
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.novacore.records.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren

/**
 * The **NovaActivity** class is useful to create an activity with the behavior to show the UI
 * data correctly and manage their refreshing
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
@Structure
abstract class NovaActivity: ComponentActivity() {

    companion object {

        /**
         * **EXECUTING_REQUEST** -> whether other requests has being executing so the refresh of the [ProjectsScreen.notifications]
         * need to be suspended
         */
        @Volatile
        var EXECUTING_REQUEST: Boolean = false

    }

    /**
     * **snackbarLauncher** -> the launcher used to display the [Snackbar]
     */
    protected lateinit var snackbarLauncher: SnackbarLauncher

    /**
     * **refreshRoutine** -> the [CoroutineScope] used to refresh the UI data
     */
    protected lateinit var refreshRoutine: CoroutineScope

    /**
     * **currentContext** -> the context got from the current [ComponentActivity] displayed
     */
    protected lateinit var currentContext: Context

    /**
     * **isRefreshing** -> whether the [refreshRoutine] is already refreshing
     */
    private var isRefreshing: Boolean = false

    /**
     * Function to init the [SnackbarLauncher] <br>
     *
     * No-any params required
     */
    @Composable
    protected fun InitLauncher() {
        snackbarLauncher = SnackbarLauncher(LocalContext.current)
        snackbarLauncher.InitSnackbarInstances()
        refreshRoutine = rememberCoroutineScope()
    }

    /**
     * Function to get project from the [projects] list
     *
     * @param projectId: the identifier of the project to get
     *
     * @return the project as [Project] if exists
     */
    protected fun getProject(
        projectId: String?
    ): Project? {
        if(projectId != null) {
            projects.forEach { project ->
                if(project.id == projectId)
                    return project
            }
        }
        return null
    }

    /**
     * Function to display the loading UI when a data to display is not yet loaded
     *
     * No-any params required
     */
    @Composable
    protected fun LoadingUI() {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BallClipRotatePulseProgressIndicator(
                minBallDiameter = 50.dp,
                minGap = 30.dp
            )
        }
    }

    /**
     * Function to get whether the [refreshRoutine] can start, so if there aren't other jobs that
     * routine is already executing and if the current [activeLocalSession] is connected with a real
     * host
     *
     * No-any params required
     *
     * @return whether the [refreshRoutine] can start as [Boolean]
     */
    protected fun canRefresherStart(): Boolean {
        return !isRefreshing && activeLocalSession.isHostSet
    }

    /**
     * Function to suspend the current [refreshRoutine] to execute other requests to the backend,
     * the [isRefreshing] will be set as **false** to allow the restart of the routine after executing
     * the other requests
     *
     * No-any params required
     */
    protected fun suspendRefresher() {
        EXECUTING_REQUEST = true
        refreshRoutine.coroutineContext.cancelChildren()
        isRefreshing = false
    }

    /**
     * Function to restart the current [refreshRoutine] after other requests has been executed,
     * the [isRefreshing] will be set as **true** to deny the restart of the routine after executing
     * the other requests
     *
     * No-any params required
     */
    protected fun restartRefresher() {
        EXECUTING_REQUEST = false
        isRefreshing = true
    }

    /**
     * Function to check and if needed set an error message
     *
     * @param errorMessage: the variable where store the message
     * @param errorMessageKey: the key of the resource string to display
     * @param error: the variable where store whether is in error
     */
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

    /**
     * Function to set an error message
     *
     * @param errorMessage: the variable where store the message
     * @param errorMessageKey: the key of the resource string to display
     * @param error: the variable where store whether is in error
     */
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

    /**
     * Function to set an error message
     *
     * @param errorMessage: the variable where store the message
     * @param errorMessageValue: the value to display
     * @param error: the variable where store whether is in error
     */
    protected fun setErrorMessage(
        errorMessage: MutableState<String>,
        errorMessageValue: String,
        error: MutableState<Boolean>
    ) {
        errorMessage.value = errorMessageValue
        error.value = true
    }

    /**
     * Function to check if the [refreshRoutine] can continue to refresh or need to be stopped, this for
     * example when the [ComponentActivity] displayed changes and the requests to refresh the UI data
     * also changes
     *
     * @param currentActivity: the current activity where the [refreshRoutine] is executing
     *
     * @return whether the [refreshRoutine] can continue to refresh as [Boolean]
     */
    protected fun continueToFetch(
        currentActivity: NovaActivity
    ) : Boolean {
        return activeActivity == currentActivity
    }

}