package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.RELEASE_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.ReleaseStatusBadge
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily

@OptIn(ExperimentalMaterial3Api::class)
class ProjectActivity : ComponentActivity() {

    private lateinit var project: MutableState<Project>

    private lateinit var displayMembers: MutableState<Boolean>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navBackIntent = Intent(this@ProjectActivity, MainActivity::class.java)
        setContent {
            project = remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    mutableStateOf(intent.getSerializableExtra(PROJECT_KEY, Project::class.java)!!)
                else
                    mutableStateOf(intent.getSerializableExtra(PROJECT_KEY)!! as Project)
            }
            val showDeleteProject = remember { mutableStateOf(false) }
            displayMembers = remember { mutableStateOf(false) }
            NovaTheme {
                Scaffold (
                    topBar = {
                        LargeTopAppBar(
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = md_theme_light_primary
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { startActivity(navBackIntent) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        // TODO: MAKE REAL WORKFLOW
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = { displayMembers.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                ProjectMembers()
                                IconButton(
                                    onClick = { showDeleteProject.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteForever,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                NovaAlertDialog(
                                    show = showDeleteProject,
                                    icon = Icons.Default.Warning,
                                    title = R.string.delete_project,
                                    message = R.string.delete_project_alert_message,
                                    confirmAction = {
                                        // TODO: MAKE THE REQUEST THEN
                                        showDeleteProject.value = false
                                        startActivity(navBackIntent)
                                    }
                                )
                            }
                        )
                    },
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
                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                top = it.calculateTopPadding()
                            )
                            .fillMaxSize()
                            .background(gray_background),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(
                            key = { release -> release.id },
                            items = project.value.releases
                        ) { release ->
                            OutlinedCard(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(15.dp),
                                elevation = CardDefaults.cardElevation(5.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                onClick = {
                                    val intent = Intent(this@ProjectActivity,
                                        ReleaseActivity::class.java)
                                    intent.putExtra(RELEASE_KEY, release)
                                    intent.putExtra(PROJECT_KEY, project.value)
                                    startActivity(intent)
                                }
                            ) {
                                Column (
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxSize()
                                ) {
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = release.releaseVersion,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        ReleaseStatusBadge(
                                            releaseStatus = release.status
                                        )
                                    }
                                    Column (
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp,
                                                start = 5.dp
                                            ),
                                    ) {
                                        Row (
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Text(
                                                text = getString(R.string.creation_date),
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = release.creationDate,
                                                fontSize = 16.sp,
                                                fontFamily = thinFontFamily
                                            )
                                        }
                                        if(release.status == ReleaseStatus.Approved) {
                                            Row (
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                Text(
                                                    text = getString(R.string.approbation_date),
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = release.approbationDate,
                                                    fontSize = 16.sp,
                                                    fontFamily = thinFontFamily
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp
                                            ),
                                        text = getString(R.string.release_notes),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Column(
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp,
                                                start = 5.dp
                                            ),
                                    ) {
                                        release.releaseNotes.forEach { releaseNote ->
                                            Row (
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "-",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = releaseNote.content,
                                                    fontSize = 16.sp,
                                                    textAlign = TextAlign.Justify,
                                                    style = TextStyle(
                                                        lineHeight = 14.sp
                                                    )
                                                )
                                            }
                                        }
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
                startActivity(navBackIntent)
            }
        })
    }

    @Composable
    private fun ProjectMembers() {
        val sheetState = rememberModalBottomSheetState()
        if(displayMembers.value) {
            ModalBottomSheet(
                onDismissRequest = { displayMembers.value = false },
                sheetState = sheetState,
                containerColor = gray_background
            ) {
                LazyColumn {
                    items(
                        key = { member -> member.id},
                        items = project.value.members
                    ) { member ->
                        ListItem(
                            leadingContent = { Logo(member.profilePicUrl) },
                            headlineContent = {
                                Text(
                                    text = "${member.name} ${member.surname}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = gray_background
                            ),
                            supportingContent = {
                                Text(
                                    text = member.email,
                                    fontSize = 16.sp,
                                )
                            },
                            trailingContent = {
                                // TODO: MAKE THE REAL WORKFLOW TO HIDE THE POSSIBILITY TO REMOVE OR REMOVED FROM THE GROUP
                                IconButton(
                                    onClick = {
                                        /*TODO MAKE THE REQUEST THEN*/
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonRemove,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }

}