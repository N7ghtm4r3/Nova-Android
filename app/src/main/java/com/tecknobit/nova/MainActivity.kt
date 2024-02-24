package com.tecknobit.nova

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.md_theme_light_primary

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                Scaffold (
                    topBar = {
                        LargeTopAppBar(
                            title = {
                                Text("gagag")
                            },
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = md_theme_light_primary
                            )
                        )
                    }
                ) {

                }
            }
        }
    }

}