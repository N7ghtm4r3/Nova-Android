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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.RELEASE_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.AssetUploadingEvent
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.RejectedReleaseEvent
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseStandardEvent
import com.tecknobit.nova.ui.components.ReleaseStatusBadge
import com.tecknobit.nova.ui.components.ReleaseTagBadge
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
class ReleaseActivity : ComponentActivity() {

    private lateinit var release: MutableState<Release>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var navBackIntent: Intent? = null
        setContent {
            release = remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    mutableStateOf(intent.getSerializableExtra(RELEASE_KEY, Release::class.java)!!)
                else
                    mutableStateOf(intent.getSerializableExtra(RELEASE_KEY)!! as Release)
            }
            val sourceProject = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getSerializableExtra(PROJECT_KEY, Project::class.java)!!
            else
                intent.getSerializableExtra(PROJECT_KEY)!! as Project
            navBackIntent = Intent(this@ReleaseActivity, ProjectActivity::class.java)
            navBackIntent!!.putExtra(PROJECT_KEY, sourceProject)
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
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            },
                            title = {
                                Column (
                                    verticalArrangement = Arrangement.spacedBy(0.dp)
                                ) {
                                    Text(
                                        text = sourceProject.name,
                                        color = Color.White,
                                        fontSize = 22.sp
                                    )
                                    Text(
                                        text = release.value.releaseVersion,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                        // TODO: MAKE THE REAL WORKFLOW
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        // TODO: MAKE REQUEST THEN
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteForever,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        // TODO: MAKE THE REAL WORKFLOW TO DISPLAY OR NOT THIS BUTTON
                        FloatingActionButton(
                            onClick = {
                                // TODO: MAKE REAL WORKFLOW
                            },
                            containerColor = md_theme_light_primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null
                            )
                        }
                    },
                    containerColor = gray_background

                ) {
                    val events = release.value.releaseEvents
                    if(events.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .background(gray_background)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = it.calculateTopPadding() + 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                key = { event -> event.id },
                                items = events
                            ) { event ->
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    val isAssesUploading = event is AssetUploadingEvent
                                    if(isAssesUploading) {
                                        IconButton(
                                            onClick = {
                                                // TODO: DOWNLOAD THE ASSET
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Download,
                                                contentDescription = null
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Download,
                                            contentDescription = null
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = event.releaseEventDate,
                                            fontSize = 14.sp
                                        )
                                        if(isAssesUploading) {
                                            Text(
                                                text = getString(R.string.new_asset_uploaded),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 22.sp
                                            )
                                        } else if(event is ReleaseStandardEvent) {
                                            ReleaseStatusBadge(
                                                releaseStatus = event.status,
                                                paddingStart = 0.dp,
                                                onClick = {
                                                    // TODO: DISPLAY THE MESSAGE FOR THE TAG
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                            if(event is RejectedReleaseEvent) {
                                                LazyRow {
                                                    items(
                                                        key = { tag -> tag.tag.name },
                                                        items = event.tags
                                                    ) { tag ->
                                                        ReleaseTagBadge(
                                                            tag = tag.tag,
                                                            onClick = {
                                                                // TODO: DISPLAY THE MESSAGE FOR THE TAG
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
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(navBackIntent!!)
            }
        })
    }

}