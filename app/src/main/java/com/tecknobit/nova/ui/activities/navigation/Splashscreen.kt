package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.storage.LocalSessionHelper
import com.tecknobit.nova.helpers.utils.AndroidRequester
import com.tecknobit.nova.helpers.utils.download.AssetDownloader
import com.tecknobit.nova.helpers.utils.ui.NotificationsReceiver.NotificationsHelper
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.auth.AuthActivity
import com.tecknobit.nova.ui.activities.navigation.MainActivity.Companion.notifications
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.activities.session.ReleaseActivity
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.novacore.InputValidator.DEFAULT_LANGUAGE
import com.tecknobit.novacore.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.helpers.Requester.ListFetcher
import com.tecknobit.novacore.records.NovaNotification
import com.tecknobit.novacore.records.User.IDENTIFIER_KEY
import com.tecknobit.novacore.records.User.PROJECTS_KEY
import com.tecknobit.novacore.records.project.Project.PROJECT_IDENTIFIER_KEY
import com.tecknobit.novacore.records.project.Project.PROJECT_KEY
import com.tecknobit.novacore.records.release.Release.RELEASE_IDENTIFIER_KEY
import com.tecknobit.novacore.records.release.Release.RELEASE_KEY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * The {@code Splashscreen} activity is used to retrieve and load the session data and enter in
 * the application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see ImageLoaderFactory
 */
@SuppressLint("CustomSplashScreen")
class Splashscreen : NovaActivity(), ImageLoaderFactory, ListFetcher {

    companion object {

        /**
         * {@code DESTINATION_KEY} the key for the <b>"destination"</b> field
         */
        const val DESTINATION_KEY = "destination"

        /**
         * **user** -> helper to manage the assets downloading
         */
        lateinit var assetDownloader: AssetDownloader

        /**
         * **localSessionsHelper** -> the helper to manage the local sessions stored locally in
         * the device
         */
        lateinit var localSessionsHelper: LocalSessionHelper

        /**
         * **requester** -> the instance to manage the requests with the backend
         */
        lateinit var requester: AndroidRequester

        /**
         * **activeLocalSession** -> the current active session that user is using
         */
        lateinit var activeLocalSession: NovaSession

        /**
         * **activeActivity** -> the current activity displayed to the user
         */
        lateinit var activeActivity: NovaActivity

    }

    /**
     * **context** -> the context of the [Splashscreen]
     */
    private lateinit var context: Context

    /**
     * **appUpdateManager** the manager to check if there is an update available
     */
    private lateinit var appUpdateManager: AppUpdateManager

    /**
     * **launcher** the result registered for [appUpdateManager] and the action to execute if fails
     */
    private var launcher  = registerForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode != RESULT_OK)
            launchApp(MainActivity::class.java)
    }

    /**
     * On create method
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     * If your ComponentActivity is annotated with {@link ContentView}, this will
     * call {@link #setContentView(int)} for you.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
            context = LocalContext.current
            currentContext = context
            refreshRoutine = rememberCoroutineScope()
            Coil.imageLoader(context)
            Coil.setImageLoader(newImageLoader())
            assetDownloader = AssetDownloader(context)
            localSessionsHelper = LocalSessionHelper(context)
            val notificationsHelper = NotificationsHelper(context)
            notificationsHelper.scheduleRoutine()
            NovaTheme {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = getString(R.string.app_name),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 45.sp
                        )
                    }
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(
                                bottom = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "by Tecknobit",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                LaunchedEffect(key1 = true) {
                    delay(250)
                    var projectId: String? = null
                    var releaseId: String? = null
                    var checkForUpdates = false
                    val intentDestination: Class<*> = when(intent.getStringExtra(DESTINATION_KEY)) {
                        PROJECTS_KEY -> {
                            localSessionsHelper.setNewActiveSession(intent.getStringExtra(IDENTIFIER_KEY))
                            initAndStartSession(
                                destination = MainActivity::class.java
                            )
                        }
                        PROJECT_KEY -> {
                            localSessionsHelper.setNewActiveSession(intent.getStringExtra(IDENTIFIER_KEY))
                            projectId = intent.getStringExtra(PROJECT_IDENTIFIER_KEY)
                            initAndStartSession(
                                destination = ProjectActivity::class.java
                            )
                        }
                        RELEASE_KEY -> {
                            localSessionsHelper.setNewActiveSession(intent.getStringExtra(IDENTIFIER_KEY))
                            projectId = intent.getStringExtra(PROJECT_IDENTIFIER_KEY)
                            releaseId = intent.getStringExtra(RELEASE_IDENTIFIER_KEY)
                            initAndStartSession(
                                destination = ReleaseActivity::class.java
                            )
                        }
                        else -> {
                            checkForUpdates = true
                            initAndStartSession(
                                destination = MainActivity::class.java
                            )
                        }
                    }
                    if(checkForUpdates)
                        checkForUpdates(intentDestination)
                    else {
                        launchApp(
                            intentDestination = intentDestination,
                            projectId = projectId,
                            releaseId = releaseId
                        )
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    /**
     * Function to init and start the session with the correct session data.
     *
     * If there is an active [NovaSession] available will be started the application if not will
     * be launched the [AuthActivity] to execute the dedicated auth operations
     *
     * @param destination: the [Intent] destination to reach after start the application
     *
     * @return the destination as [Class]
     */
    private fun initAndStartSession(destination: Class<*>): Class<*> {
        val activeSession = localSessionsHelper.activeSession
        return if(activeSession != null) {
            requester = AndroidRequester(
                host = activeSession.hostAddress,
                userId = activeSession.id,
                userToken = activeSession.token
            )
            activeLocalSession = activeSession
            setLocale()
            refreshList()
            destination
        } else
            AuthActivity::class.java
    }

    /**
     * Return a new [ImageLoader].
     */
    override fun newImageLoader(): ImageLoader {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, validateSelfSignedCertificate(), SecureRandom())
        return ImageLoader.Builder(context)
            .okHttpClient {
                OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory,
                        validateSelfSignedCertificate()[0] as X509TrustManager
                    )
                    .hostnameVerifier { _: String?, _: SSLSession? -> true }
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .build()
            }
            .addLastModifiedToFileCacheKey(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }

    /**
     * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
     * No-any params required
     *
     * @return list of trust managers as [Array] of [TrustManager]
     * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to
     * use for test only or in a private distribution on own infrastructure
     */
    private fun validateSelfSignedCertificate(): Array<TrustManager> {
        return arrayOf(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
    }

    /**
     * Function to refresh the current [notifications]
     *
     * No-any params required
     */
    override fun refreshList() {
        if(activeLocalSession.isHostSet) {
            refreshRoutine.launch {
                while (true) {
                    if(!EXECUTING_REQUEST) {
                        requester.sendRequest(
                            request = {
                                requester.getNotifications()
                            },
                            onSuccess = { response ->
                                val jNotifications = response.getJSONArray(RESPONSE_MESSAGE_KEY)
                                notifications.clear()
                                for(j in 0 until jNotifications.length())
                                    notifications.add(NovaNotification(jNotifications.getJSONObject(j)))
                            },
                            onFailure = {}
                        )
                    }
                    delay(1000L)
                }
            }
        }
    }

    /**
     * Method to check if there are some update available to install
     *
     * @param intentDestination: the intent to reach
     */
    private fun checkForUpdates(
        intentDestination: Class<*>
    ) {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
            val isUpdateSupported = info.isImmediateUpdateAllowed
            if(isUpdateAvailable && isUpdateSupported) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    launcher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else
                launchApp(intentDestination)
        }.addOnFailureListener {
            launchApp(intentDestination)
        }
    }

    /**
     * Method to launch the app and the user session
     *
     * @param intentDestination: the intent to reach
     * @param projectId: the project identifier
     * @param releaseId: the release identifier
     *
     */
    private fun launchApp(
        intentDestination: Class<*>,
        projectId: String? = null,
        releaseId: String? = null
    ) {
        val destination = Intent(this@Splashscreen, intentDestination)
        if(projectId != null)
            destination.putExtra(PROJECT_IDENTIFIER_KEY, projectId)
        if(releaseId != null)
            destination.putExtra(RELEASE_IDENTIFIER_KEY, releaseId)
        startActivity(destination)
    }

    /**
     * Function to set locale language for the application
     *
     * No-any params required
     */
    private fun setLocale() {
        var tag: String = DEFAULT_LANGUAGE
        LANGUAGES_SUPPORTED.forEach { (key, value) ->
            if(value == activeLocalSession.language) {
                tag = key
                return@forEach
            }
        }
        val locale = Locale.forLanguageTag(tag)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}