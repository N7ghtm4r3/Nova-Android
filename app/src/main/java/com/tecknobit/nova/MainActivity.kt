package com.tecknobit.nova

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {  },
                            containerColor = md_theme_light_primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                ) {
                    Column (
                        modifier = Modifier
                            .background(md_theme_light_primary)
                            .fillMaxSize(),
                    ) {
                        Spacer(modifier = Modifier.height(125.dp))
                        Card (
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            shape = RoundedCornerShape(
                                topStart = 35.dp,
                                topEnd = 35.dp
                            ),
                            elevation = CardDefaults.cardElevation(15.dp),
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = 15.dp,
                                        end = 15.dp,
                                        bottom = 15.dp,
                                        start = 20.dp
                                    )
                            ) {
                                Text(
                                    text = getString(R.string.projects),
                                    fontSize = 22.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}