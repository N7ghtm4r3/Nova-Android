package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
class ProjectActivity : ComponentActivity() {

    private lateinit var project: MutableState<Project>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            mutableStateOf(intent.getSerializableExtra(Project.PROJECT_KEY, Project::class.java)!!)
        else
            mutableStateOf(intent.getSerializableExtra(Project.PROJECT_KEY)!! as Project)
        setContent {
            NovaTheme {
                Scaffold (
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = md_theme_light_primary
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        startActivity(Intent(this@ProjectActivity,
                                            MainActivity::class.java))
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            },
                            title = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = spacedBy(5.dp)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .alignBy(LastBaseline),
                                        text = project.value.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        modifier = Modifier
                                            .alignBy(LastBaseline),
                                        text = project.value.workingProgressVersionText,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }

                            }
                        )
                    }
                ) {

                }
            }
        }
    }

}