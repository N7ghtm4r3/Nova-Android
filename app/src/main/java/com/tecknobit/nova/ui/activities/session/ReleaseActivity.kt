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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.EventBusy
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkrockstudios.libraries.mpfilepicker.MultipleFilePicker
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeExtendedEvent
import com.tecknobit.nova.R.string
import com.tecknobit.nova.R.string.approve
import com.tecknobit.nova.R.string.close
import com.tecknobit.nova.R.string.comment
import com.tecknobit.nova.R.string.comment_the_asset
import com.tecknobit.nova.R.string.confirm
import com.tecknobit.nova.R.string.delete_release
import com.tecknobit.nova.R.string.delete_release_alert_message
import com.tecknobit.nova.R.string.dismiss
import com.tecknobit.nova.R.string.new_asset_has_been_uploaded
import com.tecknobit.nova.R.string.no_events_yet
import com.tecknobit.nova.R.string.promote_alpha_release_alert_message
import com.tecknobit.nova.R.string.promote_beta_release_alert_message
import com.tecknobit.nova.R.string.promote_latest_release_alert_message
import com.tecknobit.nova.R.string.promote_release
import com.tecknobit.nova.R.string.promote_release_as_beta
import com.tecknobit.nova.R.string.promote_release_as_latest
import com.tecknobit.nova.R.string.reject
import com.tecknobit.nova.R.string.tags
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.assetDownloader
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.user
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.NovaTextField
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
import com.tecknobit.novacore.InputValidator.areRejectionReasonsValid
import com.tecknobit.novacore.InputValidator.isTagCommentValid
import com.tecknobit.novacore.records.project.Project
import com.tecknobit.novacore.records.project.Project.PROJECT_KEY
import com.tecknobit.novacore.records.release.Release
import com.tecknobit.novacore.records.release.Release.ALLOWED_ASSETS_TYPE
import com.tecknobit.novacore.records.release.Release.RELEASE_KEY
import com.tecknobit.novacore.records.release.Release.ReleaseStatus
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Alpha
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Approved
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Latest
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Rejected
import com.tecknobit.novacore.records.release.events.AssetUploadingEvent
import com.tecknobit.novacore.records.release.events.RejectedReleaseEvent
import com.tecknobit.novacore.records.release.events.RejectedTag
import com.tecknobit.novacore.records.release.events.ReleaseEvent
import com.tecknobit.novacore.records.release.events.ReleaseEvent.ReleaseTag
import com.tecknobit.novacore.records.release.events.ReleaseEvent.ReleaseTag.Bug
import com.tecknobit.novacore.records.release.events.ReleaseEvent.ReleaseTag.Issue
import com.tecknobit.novacore.records.release.events.ReleaseEvent.ReleaseTag.LayoutChange
import com.tecknobit.novacore.records.release.events.ReleaseStandardEvent

/**
 * The {@code ReleaseActivity} activity is used to manage and display the [Release] details
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
class ReleaseActivity : NovaActivity() {

    /**
     * **release** -> the release displayed
     */
    private lateinit var release: MutableState<Release>

    /**
     * **navBackIntent** -> the intent reached when navigate back
     */
    private var navBackIntent: Intent? = null

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
            currentContext = LocalContext.current
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
                                        // TODO: MAKE REQUEST THEN
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
                        if(releaseCurrentStatus != Latest && user.isVendor) {
                            var showFilePicker by remember { mutableStateOf(false) }
                            MultipleFilePicker(
                                show = showFilePicker,
                                fileExtensions = ALLOWED_ASSETS_TYPE
                            ) { assets ->
                                if(!assets.isNullOrEmpty()) {
                                    // TODO: MAKE REQUEST THEN
                                    showFilePicker = false
                                }
                            }
                            FloatingActionButton(
                                onClick = {
                                    if(isReleaseApproved)
                                        showPromoteRelease.value = true
                                    else
                                        showFilePicker = true
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
                        title = delete_release,
                        message = delete_release_alert_message,
                        confirmAction = {
                            // TODO: MAKE THE REQUEST THEN
                            showDeleteRelease.value = false
                            startActivity(navBackIntent)
                        }
                    )
                    if(isReleaseApproved) {
                        val lastEventStatus = getLastEventStatus()
                        NovaAlertDialog(
                            show = showPromoteRelease,
                            icon = Icons.Default.Verified,
                            title = when (lastEventStatus) {
                                Approved -> promote_release
                                Alpha -> promote_release_as_beta
                                else -> promote_release_as_latest
                            },
                            message = {
                                val resId = when(lastEventStatus) {
                                    Approved -> { promote_release }
                                    Alpha -> { promote_beta_release_alert_message }
                                    else -> { promote_latest_release_alert_message }
                                }
                                if(lastEventStatus == Approved) {
                                    var isAlphaSelected by remember {
                                        mutableStateOf(true)
                                    }
                                    var warnText by remember {
                                        mutableIntStateOf(promote_alpha_release_alert_message)
                                    }
                                    Column {
                                        Row (
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            RadioButton(
                                                selected = isAlphaSelected,
                                                onClick = {
                                                    if(!isAlphaSelected) {
                                                        isAlphaSelected = true
                                                        warnText = promote_alpha_release_alert_message
                                                    }
                                                }
                                            )
                                            Text(
                                                text = Alpha.name
                                            )
                                            RadioButton(
                                                selected = !isAlphaSelected,
                                                onClick = {
                                                    if(isAlphaSelected) {
                                                        isAlphaSelected = false
                                                        warnText = promote_latest_release_alert_message
                                                    }
                                                }
                                            )
                                            Text(
                                                text = Latest.name
                                            )
                                        }
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally),
                                            text = getString(warnText),
                                            textAlign = TextAlign.Justify
                                        )
                                    }
                                } else {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = getString(resId),
                                        textAlign = TextAlign.Justify
                                    )
                                }
                            },
                            confirmAction = {
                                // TODO: MAKE THE REQUEST TO PROMOTE THE RELEASE AS LATEST THEN
                                showPromoteRelease.value = false
                            }
                        )
                    }
                    val events = release.value.releaseEvents
                    if(events.isNotEmpty()) {
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
                                                new_asset_has_been_uploaded
                                            else
                                                (event as ReleaseStandardEvent).getMessage()
                                            Text(
                                                text = getString(message),
                                            )
                                            if((isAssetUploadingEvent && ((releaseCurrentStatus != Approved)
                                                        && (releaseCurrentStatus != Latest)))) {
                                                if(!(event as AssetUploadingEvent).isCommented) {
                                                    val showCommentAsset = remember { mutableStateOf(false) }
                                                    val isApproved = remember { mutableStateOf(true) }
                                                    val reasons = remember { mutableStateOf("") }
                                                    val reasonsErrorMessage = remember { mutableStateOf("") }
                                                    val isError = remember { mutableStateOf(false) }
                                                    val closeAction = {
                                                        isApproved.value = true
                                                        reasons.value = ""
                                                        isError.value = false
                                                        reasonsErrorMessage.value = ""
                                                        showCommentAsset.value = false
                                                    }
                                                    NovaAlertDialog(
                                                        show = showCommentAsset,
                                                        icon = Icons.AutoMirrored.Filled.Comment,
                                                        onDismissAction = closeAction,
                                                        title = comment_the_asset,
                                                        message = commentReleaseMessage(
                                                            isApproved = isApproved,
                                                            reasons = reasons,
                                                            isError = isError,
                                                            reasonsErrorMessage = reasonsErrorMessage
                                                        ),
                                                        dismissAction = closeAction,
                                                        confirmAction = {
                                                            if(isApproved.value) {
                                                                // TODO: MAKE THE REQUEST THEN
                                                                closeAction()
                                                            } else {
                                                                if(areRejectionReasonsValid(reasons.value)) {
                                                                    // TODO: MAKE THE REQUEST THEN
                                                                    closeAction()
                                                                } else {
                                                                    checkToSetErrorMessage(
                                                                        errorMessage = reasonsErrorMessage,
                                                                        errorMessageKey = string.wrong_reasons,
                                                                        error = isError
                                                                    )
                                                                }
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
                                                                event.assetsUploaded.forEach { asset ->
                                                                    assetDownloader.downloadAsset(
                                                                        asset.url
                                                                    )
                                                                }
                                                            }
                                                        ) {
                                                            // TODO: FIND A SUITABLE TEXT
                                                            Text(
                                                                text = "Test"
                                                            )
                                                        }
                                                        if(user.isCustomer) {
                                                            Button(
                                                                onClick = { showCommentAsset.value = true }
                                                            ) {
                                                                Text(
                                                                    text = getString(comment)
                                                                )
                                                            }
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
                    } else {
                        EmptyList(
                            icon = Icons.Default.EventBusy,
                            description = no_events_yet
                        )
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

    /**
     * Function to create and display the UI to comment a release
     *
     * @param isApproved: state to indicate whether the release is approved
     * @param reasons: the rejection reasons
     * @param isError: state to indicate whether an error occurred
     * @param reasonsErrorMessage: message to display when the rejection reasons are not valid
     */
    private fun commentReleaseMessage(
        isApproved: MutableState<Boolean>,
        reasons: MutableState<String>,
        isError: MutableState<Boolean>,
        reasonsErrorMessage: MutableState<String>,
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
                        text = stringResource(approve)
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
                        text = stringResource(reject)
                    )
                }
            }
            if(!isApproved.value) {
                NovaTextField(
                    value = reasons,
                    onValueChange = {
                        isError.value = !areRejectionReasonsValid(it) && reasons.value.isNotEmpty()
                        checkToSetErrorMessage(
                            errorMessage = reasonsErrorMessage,
                            errorMessageKey = string.wrong_reasons,
                            error = isError
                        )
                        reasons.value = it
                    },
                    label = string.reasons,
                    errorMessage = reasonsErrorMessage,
                    isError = isError
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    text = getString(tags),
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

    /**
     * Function to display a [NovaAlertDialog] with the comment of a [RejectedTag]
     *
     * @param show: whether show or not the alert
     * @param tag: the tag used to display the alert
     * @param date: the date when the tag has been commented
     */
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
                            text = stringResource(dismiss)
                        )
                    }
                }
                val dismissButton = if(isInputMode)
                    isInputButton
                else
                    null
                val description = remember { mutableStateOf("") }
                val isError = remember { mutableStateOf(false) }
                val descriptionErrorMessage = remember { mutableStateOf("") }
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
                                NovaTextField(
                                    value = description,
                                    onValueChange = {
                                        isError.value = !isTagCommentValid(it) &&
                                                description.value.isNotEmpty()
                                        checkToSetErrorMessage(
                                            errorMessage = descriptionErrorMessage,
                                            errorMessageKey = string.wrong_description,
                                            error = isError
                                        )
                                        description.value = it
                                    },
                                    label = string.description,
                                    errorMessage = descriptionErrorMessage,
                                    isError = isError
                                )
                            }
                        }
                    },
                    dismissButton = dismissButton,
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if(isInputMode) {
                                    if(isTagCommentValid(description.value)) {
                                        // TODO: MAKE REQUEST THEN
                                        show.value = false
                                    } else {
                                        checkToSetErrorMessage(
                                            errorMessage = descriptionErrorMessage,
                                            errorMessageKey = string.wrong_description,
                                            error = isError
                                        )
                                    }
                                } else
                                    show.value = false
                            }
                        ) {
                            val buttonText = if(isInputMode)
                                confirm
                            else
                                close
                            Text(
                                text = stringResource(buttonText)
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Function to get the last event occurred in the current [release]
     *
     * No-any params required
     *
     * @return the last status of the last event occurred as [ReleaseStatus]
     */
    private fun getLastEventStatus() : ReleaseStatus {
        val releaseEvents = mutableListOf<ReleaseEvent>()
        releaseEvents.addAll(release.value.releaseEvents)
        releaseEvents.reverse()
        var lastEventStatus = Alpha
        releaseEvents.forEachIndexed { index, event ->
            val eventValue = event as ReleaseStandardEvent
            if (eventValue.status == Approved) {
                val nextIndex = index + 1
                lastEventStatus = if(nextIndex >= releaseEvents.size)
                    Approved
                else
                    (releaseEvents[index + 1] as ReleaseStandardEvent).status
            }
        }
        return lastEventStatus
    }

}