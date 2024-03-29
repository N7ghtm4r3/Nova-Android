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

@SuppressLint("CustomSplashScreen")
class Splashscreen : ComponentActivity() {

    companion object {

        lateinit var user: User

        lateinit var assetDownloader: AssetDownloader

        lateinit var localSessionHelper: LocalSessionHelper

    }

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