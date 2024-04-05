package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.storage.LocalSessionHelper
import com.tecknobit.nova.helpers.utils.AndroidRequester
import com.tecknobit.nova.helpers.utils.download.AssetDownloader
import com.tecknobit.nova.helpers.utils.ui.NotificationsReceiver.NotificationsHelper
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.auth.AuthActivity
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession
import com.tecknobit.novacore.records.User.PROJECTS_KEY
import com.tecknobit.novacore.records.project.Project.PROJECT_KEY
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
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
class Splashscreen : ComponentActivity(), ImageLoaderFactory {

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
            context = LocalContext.current
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
                    val intentDestination: Class<*> = when(intent.getStringExtra(DESTINATION_KEY)) {
                        PROJECTS_KEY -> MainActivity::class.java
                        PROJECT_KEY -> ProjectActivity::class.java
                        else -> {
                            val activeSession = localSessionsHelper.activeSession
                            if(activeSession != null) {
                                requester = AndroidRequester(
                                    host = activeSession.hostAddress,
                                    userId = activeSession.id,
                                    userToken = activeSession.token
                                )
                                activeLocalSession = activeSession
                                MainActivity::class.java
                            } else 
                                AuthActivity::class.java
                        }
                    }
                    // TODO: SET THE ACTIVE LOCAL SESSION FETCHING THE intent.getStringExtra(IDENTIFIER_KEY);
                    startActivity(Intent(this@Splashscreen, intentDestination))
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

}