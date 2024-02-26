package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary


class MainActivity : ComponentActivity() {

    companion object {

        // TODO: TO LOAD CORRECTLY
        private val projects = mutableListOf(
            Project("Nova"),
            Project("Pandoro", "1.0.1"),
            Project("Glider", "1.0.5")
        )

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                Scaffold (
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                // TODO: MAKE REAL WORKFLOW
                            },
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
                            colors = CardDefaults.cardColors(
                                containerColor = gray_background
                            ),
                            elevation = CardDefaults.cardElevation(10.dp),
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
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentPadding = PaddingValues(
                                        top = 10.dp,
                                        bottom = 10.dp
                                    ),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(
                                        key = { project -> project.id },
                                        items = projects
                                    ) { project ->
                                        ListItem(
                                            modifier = Modifier
                                                .shadow(
                                                    elevation = 5.dp,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .clip(RoundedCornerShape(15.dp))
                                                .clickable {
                                                    val intent = Intent(this@MainActivity,
                                                        ProjectActivity::class.java)
                                                    intent.putExtra(PROJECT_KEY, project)
                                                    startActivity(intent)
                                                },
                                            colors = ListItemDefaults.colors(
                                                containerColor = Color.White
                                            ),
                                            leadingContent = {
                                                AsyncImage(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(15.dp))
                                                        .size(60.dp),
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(project.logoUrl)
                                                        .crossfade(true)
                                                        .build(),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop
                                                )
                                            },
                                            headlineContent = {
                                                Text(
                                                    text = project.name,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            supportingContent = {
                                                val workingProgressVersionText = project.workingProgressVersionText
                                                Text(
                                                    text = if(workingProgressVersionText != null)
                                                        workingProgressVersionText
                                                    else
                                                        getString(R.string.no_version_available_yet),
                                                    fontSize = 16.sp
                                                )
                                            },
                                            trailingContent = {
                                                Icon(
                                                    modifier = Modifier
                                                        .size(30.dp),
                                                    imageVector = Icons.Default.KeyboardArrowRight,
                                                    contentDescription = null
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
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

}