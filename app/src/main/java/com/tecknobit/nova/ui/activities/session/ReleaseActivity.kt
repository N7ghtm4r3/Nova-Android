package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeExtendedEvent
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.RELEASE_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Approved
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Latest
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus.Rejected
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.AssetUploadingEvent
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.RejectedReleaseEvent
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.RejectedTag
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseEvent.ReleaseTag.*
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.events.ReleaseStandardEvent
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.ReleaseStatusBadge
import com.tecknobit.nova.ui.components.ReleaseTagBadge
import com.tecknobit.nova.ui.components.createColor
import com.tecknobit.nova.ui.components.getMessage
import com.tecknobit.nova.ui.theme.BlueSchemeColors
import com.tecknobit.nova.ui.theme.LightblueSchemeColors
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.RedSchemeColors
import com.tecknobit.nova.ui.theme.Typography
import com.tecknobit.nova.ui.theme.VioletSchemeColors
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
class ReleaseActivity : ComponentActivity() {

    private lateinit var release: MutableState<Release>

    private var navBackIntent: Intent? = null

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val releaseCurrentStatus = release.value.status
            val isReleaseApproved = releaseCurrentStatus == Approved
            val showPromoteRelease = remember { mutableStateOf(false) }
            val showDeleteRelease = remember { mutableStateOf(false) }
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
                                    onClick = { showDeleteRelease.value = true }
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
                        // TODO: MAKE THE WORKFLOW TO HIDE WHEN MEMBER IS THE CLIENT
                        if(releaseCurrentStatus != Latest) {
                            FloatingActionButton(
                                onClick = {
                                    if(isReleaseApproved)
                                        showPromoteRelease.value = true
                                    else {

                                    }
                                },
                                containerColor = md_theme_light_primary
                            ) {
                                Icon(
                                    imageVector = if(isReleaseApproved)
                                        Icons.Default.Verified
                                    else
                                        Icons.Default.Upload,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    containerColor = gray_background
                ) {
                    NovaAlertDialog(
                        show = showDeleteRelease,
                        icon = Icons.Default.Warning,
                        title = R.string.delete_release,
                        message = R.string.delete_release_alert_message,
                        confirmAction = {
                            // TODO: MAKE THE REQUEST THEN
                            showDeleteRelease.value = false
                            startActivity(navBackIntent)
                        }
                    )
                    if(isReleaseApproved) {
                        NovaAlertDialog(
                            show = showPromoteRelease,
                            icon = Icons.Default.Verified,
                            title = R.string.promote_release_as_latest,
                            message = R.string.promoted_release_alert_message,
                            confirmAction = {
                                // TODO: MAKE THE REQUEST TO PROMOTE THE RELEASE AS LATEST THEN
                                showPromoteRelease.value = false
                            }
                        )
                    }
                    val events = release.value.releaseEvents
                    JetLimeColumn(
                        modifier = Modifier
                            .padding(
                                top = it.calculateTopPadding() + 25.dp,
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 20.dp
                            ),
                        itemsList = ItemsList(events),
                        style = JetLimeDefaults.columnStyle(
                            contentDistance = 24.dp,
                            lineThickness = 3.dp
                        ),
                        key = { _, item -> item.id },
                    ) { _, event, position ->
                        val isAssetUploadingEvent = event is AssetUploadingEvent
                        JetLimeExtendedEvent(
                            style = JetLimeEventDefaults.eventStyle(
                                position = position,
                                pointRadius = 11.5.dp,
                                pointStrokeWidth = if(isAssetUploadingEvent)
                                    6.dp
                                else
                                    1.5.dp
                            ),
                            additionalContent = {
                                Column (
                                    modifier = Modifier
                                        .width(105.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if(event is ReleaseStandardEvent) {
                                        ReleaseStatusBadge(
                                            releaseStatus = event.status,
                                            paddingStart = 0.dp,
                                        )
                                    }
                                }
                            }
                        ) {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Column {
                                    Text(
                                        text = event.releaseEventDate,
                                        fontFamily = thinFontFamily,
                                    )
                                    if(event !is RejectedReleaseEvent) {
                                        val message = if(isAssetUploadingEvent)
                                            R.string.new_asset_has_been_uploaded
                                        else
                                            (event as ReleaseStandardEvent).getMessage()
                                        Text(
                                            text = getString(message),
                                        )
                                        // TODO: MAKE THE WORKFLOW TO HIDE WHEN MEMBER IS NOT THE CLIENT
                                        if((isAssetUploadingEvent && ((releaseCurrentStatus != Approved)
                                                    && (releaseCurrentStatus != Latest)))) {
                                            if(!(event as AssetUploadingEvent).isCommented) {
                                                val showCommentAsset = remember { mutableStateOf(false) }
                                                val isApproved = remember { mutableStateOf(true) }
                                                val reasons = remember { mutableStateOf("") }
                                                val isError = remember { mutableStateOf(false) }
                                                val closeAction = {
                                                    isApproved.value = true
                                                    reasons.value = ""
                                                    isError.value = false
                                                    showCommentAsset.value = false
                                                }
                                                NovaAlertDialog(
                                                    show = showCommentAsset,
                                                    icon = Icons.AutoMirrored.Filled.Comment,
                                                    onDismissAction = closeAction,
                                                    title = R.string.comment_the_asset,
                                                    message = commentReleaseMessage(
                                                        isApproved = isApproved,
                                                        reasons = reasons,
                                                        isError = isError
                                                    ),
                                                    dismissAction = closeAction,
                                                    confirmAction = {
                                                        if(isApproved.value) {
                                                            // TODO: MAKE THE REQUEST THEN
                                                            closeAction()
                                                        } else {
                                                            if(reasons.value.isNotEmpty()) {
                                                                // TODO: MAKE THE REQUEST THEN
                                                                closeAction()
                                                            } else
                                                                isError.value = true
                                                        }
                                                    }
                                                )
                                                Row (
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            // TODO: MAKE THE REAL WORKFLOW
                                                        }
                                                    ) {
                                                        // TODO: FIND A SUITABLE TEXT
                                                        Text(
                                                            text = "Test"
                                                        )
                                                    }
                                                    Button(
                                                        onClick = { showCommentAsset.value = true }
                                                    ) {
                                                        Text(
                                                            text = getString(R.string.comment)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Column {
                                            Text(
                                                text = event.reasons,
                                                textAlign = TextAlign.Justify
                                            )
                                            LazyHorizontalGrid(
                                                modifier = Modifier
                                                    .requiredHeightIn(
                                                        min = 35.dp,
                                                        max = 70.dp
                                                    ),
                                                contentPadding = PaddingValues(
                                                    top = 5.dp
                                                ),
                                                rows = GridCells.Fixed(2),
                                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {
                                                items(
                                                    key = { tag -> tag.tag.name },
                                                    items = event.tags
                                                ) { tag ->
                                                    val showAlert = remember {
                                                        mutableStateOf(false)
                                                    }
                                                    ReleaseTagBadge(
                                                        tag = tag,
                                                        onClick = { showAlert.value = true }
                                                    )
                                                    TagInformation(
                                                        show = showAlert,
                                                        tag = tag,
                                                        date = event.releaseEventDate
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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(navBackIntent!!)
            }
        })
    }

    private fun commentReleaseMessage(
        isApproved: MutableState<Boolean>,
        reasons: MutableState<String>,
        isError: MutableState<Boolean>
    ) = @Composable {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val green = Approved.createColor()
            val red = Rejected.createColor()
            Row (
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if(isApproved.value)
                            green
                        else
                            Color.Unspecified,
                        contentColor = if(isApproved.value)
                            Color.White
                        else
                            green
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = green
                    ),
                    modifier = Modifier
                        .width(120.dp),
                    onClick = {
                        if(!isApproved.value)
                            isApproved.value = true
                    }
                ) {
                    Text(
                        text = stringResource(R.string.approve)
                    )
                }
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if(!isApproved.value)
                            red
                        else
                            Color.Unspecified,
                        contentColor = if(!isApproved.value)
                            Color.White
                        else
                            red
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = red
                    ),
                    modifier = Modifier
                        .width(120.dp),
                    onClick = {
                        if(isApproved.value)
                            isApproved.value = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.reject)
                    )
                }
            }
            if(!isApproved.value) {
                OutlinedTextField(
                    value = reasons.value,
                    onValueChange = {
                        isError.value = it.isEmpty() && reasons.value.isNotEmpty()
                        reasons.value = it
                    },
                    label = {
                        Text(
                            text = getString(R.string.reasons)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { reasons.value = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    },
                    isError = isError.value
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = getString(R.string.tags),
                    fontSize = 20.sp
                )
                LazyHorizontalGrid(
                    modifier = Modifier
                        .requiredHeightIn(
                            min = 40.dp,
                            max = 80.dp
                        )
                        .align(Alignment.Start),
                    contentPadding = PaddingValues(
                        top = 5.dp
                    ),
                    rows = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(
                        key = { tag -> tag.name },
                        items = ReleaseTag.entries.toTypedArray()
                    ) { tag ->
                        var isAdded by remember { mutableStateOf(false) }
                        val tagColor = tag.createColor()
                        OutlinedButton(
                            modifier = Modifier
                                .requiredWidthIn(
                                    min = 40.dp,
                                    max = 150.dp
                                )
                                .height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if(isAdded)
                                    tagColor
                                else
                                    Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if(!isAdded)
                                    tagColor
                                else
                                    Color.White
                            ),
                            onClick = { isAdded = !isAdded }
                        ) {
                            Text(
                                text = tag.name,
                                fontWeight = FontWeight.Bold,
                                color = if(!isAdded)
                                    tagColor
                                else
                                    Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TagInformation(
        show: MutableState<Boolean>,
        tag: RejectedTag,
        date: String
    ) {
        MaterialTheme(
            colorScheme = when(tag.tag) {
                Bug -> RedSchemeColors
                Issue -> VioletSchemeColors
                LayoutChange -> LightblueSchemeColors
                else -> BlueSchemeColors
            },
            typography = Typography,
        ) {
            if(show.value) {
                val isInputMode = tag.comment.isEmpty()
                val isInputButton = @Composable {
                    TextButton(
                        onClick = { show.value = false }
                    ) {
                        Text(
                            text = stringResource(R.string.dismiss)
                        )
                    }
                }
                val dismissButton = if(isInputMode)
                    isInputButton
                else
                    null
                val description = remember { mutableStateOf("") }
                val isError = remember { mutableStateOf(false) }
                AlertDialog(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null
                        )
                    },
                    onDismissRequest = { show.value = false },
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = tag.tag.name,
                            textAlign = TextAlign.Start
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = date,
                                fontFamily = thinFontFamily,
                            )
                            if(!isInputMode) {
                                Text(
                                    text = tag.comment,
                                    textAlign = TextAlign.Justify
                                )
                            } else {
                                OutlinedTextField(
                                    value = description.value,
                                    onValueChange = {
                                        isError.value = it.isEmpty() && description.value.isNotEmpty()
                                        description.value = it
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(R.string.description)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { description.value = "" }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    isError = isError.value
                                )
                            }
                        }
                    },
                    dismissButton = dismissButton,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if(isInputMode) {
                                    if(description.value.isNotEmpty()) {
                                        // TODO: MAKE REQUEST THEN
                                        show.value = false
                                    } else
                                        isError.value = true
                                } else
                                    show.value = false
                            }
                        ) {
                            val buttonText = if(isInputMode)
                                R.string.confirm
                            else
                                R.string.close
                            Text(
                                text = stringResource(buttonText)
                            )
                        }
                    }
                )
            }
        }
    }

}