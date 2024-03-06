package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
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

    private lateinit var displayAddProject: MutableState<Boolean>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                displayAddProject = remember { mutableStateOf(false) }
                Scaffold (
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { displayAddProject.value = true },
                            containerColor = md_theme_light_primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        AddProject()
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
                                if(projects.isNotEmpty()) {
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
                                                        val intent = Intent(
                                                            this@MainActivity,
                                                            ProjectActivity::class.java
                                                        )
                                                        intent.putExtra(PROJECT_KEY, project)
                                                        startActivity(intent)
                                                    },
                                                colors = ListItemDefaults.colors(
                                                    containerColor = Color.White
                                                ),
                                                leadingContent = { Logo(project.logoUrl) },
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
                                } else {
                                    EmptyList(
                                        icon = Icons.Default.FolderOff,
                                        description = R.string.no_projects_yet
                                    )
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

    @Composable
    private fun AddProject() {
        var projectName by remember { mutableStateOf("") }
        var projectNameError by remember { mutableStateOf(false) }
        val resetLayout = {
            projectName = ""
            projectNameError = false
            displayAddProject.value = false
        }
        NovaAlertDialog(
            show = displayAddProject,
            onDismissAction = resetLayout,
            icon = Icons.Default.CreateNewFolder,
            title = stringResource(R.string.add_project),
            message = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box (
                        modifier = Modifier
                            .size(125.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: USE THE NOVA PROJECT LOGO AS DEFAULT IMAGE
                        Image(
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape),
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xD0DFD8D8))
                                .align(Alignment.BottomEnd),
                            onClick = {
                                // TODO: CHANGE PROFILE PICK WITH PICKER
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    OutlinedTextField(
                        singleLine = true,
                        value = projectName,
                        onValueChange = {
                            projectNameError = it.isEmpty() && projectName.isNotEmpty()
                            projectName = it
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.name)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { projectName = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        },
                        isError = projectNameError
                    )
                }
            },
            dismissAction = resetLayout,
            confirmAction = {
                if(projectName.isNotEmpty()) {
                    // TODO: MAKE REQUEST THEN
                    resetLayout()
                } else
                    projectNameError = true
            }
        )
    }

}