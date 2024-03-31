package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
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
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.storage.LocalSessionHelper
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.users.User
import com.tecknobit.nova.helpers.utils.download.AssetDownloader
import com.tecknobit.nova.ui.theme.NovaTheme
import kotlinx.coroutines.delay

/**
 * The {@code Splashscreen} activity is used to retrieve and load the session data and enter in
 * the application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
@SuppressLint("CustomSplashScreen")
class Splashscreen : ComponentActivity() {

    companion object {

        /**
         * **user** -> the user of the current session
         */
        lateinit var user: User

        /**
         * **user** -> helper to manage the assets downloading
         */
        lateinit var assetDownloader: AssetDownloader

        /**
         * **localSessionHelper** -> the helper to manage the local sessions stored locally in
         * the device
         */
        lateinit var localSessionHelper: LocalSessionHelper

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
            assetDownloader = AssetDownloader(LocalContext.current)
            localSessionHelper = LocalSessionHelper(LocalContext.current)
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
                    delay(500)
                    // TODO: MAKE THE REAL WORKFLOW
                    user = User("Manuel", "Maurizio")
                    startActivity(Intent(this@Splashscreen, MainActivity::class.java))
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

}