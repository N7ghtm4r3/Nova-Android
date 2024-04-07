package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.meetup.twain.MarkdownEditor
import com.meetup.twain.MarkdownText
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.nova.R
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.requester
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.NovaTextField
import com.tecknobit.nova.ui.components.ReleaseStatusBadge
import com.tecknobit.nova.ui.components.UserRoleBadge
import com.tecknobit.nova.ui.components.getMemberProfilePicUrl
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_error
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily
import com.tecknobit.novacore.InputValidator.WRONG_RELEASE_VERSION_MESSAGE
import com.tecknobit.novacore.InputValidator.areReleaseNotesValid
import com.tecknobit.novacore.InputValidator.isMailingListValid
import com.tecknobit.novacore.InputValidator.isReleaseVersionValid
import com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession.HOST_ADDRESS_KEY
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.helpers.Requester.ItemFetcher
import com.tecknobit.novacore.records.User.Role.Customer
import com.tecknobit.novacore.records.User.Role.Vendor
import com.tecknobit.novacore.records.project.JoiningQRCode
import com.tecknobit.novacore.records.project.Project
import com.tecknobit.novacore.records.project.Project.IDENTIFIER_KEY
import com.tecknobit.novacore.records.project.Project.PROJECT_KEY
import com.tecknobit.novacore.records.release.Release.RELEASE_KEY
import com.tecknobit.novacore.records.release.Release.ReleaseStatus.Approved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * The {@code ProjectActivity} activity is used to manage and display a [Project] details
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see NovaActivity
 * @see ItemFetcher
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
class ProjectActivity : NovaActivity(), ItemFetcher {

    /**
     * **project** -> the project displayed
     */
    private lateinit var project: MutableState<Project>

    /**
     * **displayAddMembers** -> state used to display the [CreateQrcode] UI
     */
    private lateinit var displayAddMembers: MutableState<Boolean>

    /**
     * **displayAddRelease** -> state used to display the [AddRelease] UI
     */
    private lateinit var displayAddRelease: MutableState<Boolean>

    /**
     * **displayMembers** -> state used to display the [ProjectMembers] UI
     */
    private lateinit var displayMembers: MutableState<Boolean>

    /**
     * **isProjectAuthor** -> whether the current [user] is the author of the current [project]
     */
    private var isProjectAuthor: Boolean = false

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
        val navBackIntent = Intent(this@ProjectActivity, MainActivity::class.java)
        setContent {
            var projectFromExtra: Project?
            project = remember {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    projectFromExtra = intent.getSerializableExtra(PROJECT_KEY, Project::class.java)
                    if(projectFromExtra != null)
                        mutableStateOf(projectFromExtra!!)
                    else
                    // TODO: MAKE THE REQUEST OR CHECK HOW TO AVOID A REQUEST
                        mutableStateOf(Project())
                } else {
                    projectFromExtra = intent.getSerializableExtra(PROJECT_KEY) as Project?
                    if(projectFromExtra != null)
                        mutableStateOf(projectFromExtra!!)
                    else
                    // TODO: MAKE THE REQUEST OR CHECK HOW TO AVOID A REQUEST
                        mutableStateOf(Project())
                }
            }
            currentContext = LocalContext.current
            refreshRoutine = rememberCoroutineScope()
            InitLauncher()
            refreshItem()
            isProjectAuthor = project.value.amITheProjectAuthor(activeLocalSession.id)
            val showWorkOnProject = remember { mutableStateOf(false) }
            displayAddMembers = remember { mutableStateOf(false) }
            displayAddRelease = remember { mutableStateOf(false) }
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
                                if(activeLocalSession.isVendor) {
                                    IconButton(
                                        onClick = {
                                            displayAddMembers.value = true
                                            suspendRefresher()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.QrCode,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                    CreateQrcode()
                                }
                                IconButton(
                                    onClick = {
                                        displayMembers.value = true
                                        suspendRefresher()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                ProjectMembers()
                                IconButton(
                                    onClick = {
                                        showWorkOnProject.value = true
                                        suspendRefresher()
                                    }
                                ) {
                                    Icon(
                                        imageVector = if(isProjectAuthor)
                                            Icons.Default.DeleteForever
                                        else
                                            Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                NovaAlertDialog(
                                    show = showWorkOnProject,
                                    onDismissAction = {
                                        showWorkOnProject.value = false
                                        refreshItem()
                                    },
                                    icon = Icons.Default.Warning,
                                    title = if(isProjectAuthor)
                                        R.string.delete_project
                                    else
                                        R.string.leave_from_project,
                                    message = if(isProjectAuthor)
                                        R.string.delete_project_alert_message
                                    else
                                        R.string.leave_project_alert_message,
                                    confirmAction = {
                                        requester.sendRequest(
                                            request = {
                                                if(isProjectAuthor) {
                                                    requester.deleteProject(
                                                        projectId = project.value.id
                                                    )
                                                } else {
                                                    requester.leaveProject(
                                                        projectId = project.value.id
                                                    )
                                                }
                                            },
                                            onSuccess = {
                                                showWorkOnProject.value = false
                                                startActivity(navBackIntent)
                                            },
                                            onFailure = { response ->
                                                showWorkOnProject.value = false
                                                snackbarLauncher.showSnack(
                                                    message = response.getString(RESPONSE_MESSAGE_KEY)
                                                )
                                                refreshItem()
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    },
                    snackbarHost = {
                        snackbarLauncher.CreateSnackbarHost()
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                displayAddRelease.value = true
                                suspendRefresher()
                            },
                            containerColor = md_theme_light_primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        AddRelease()
                    }
                ) {
                    val releases = project.value.releases.sortedByDescending { release ->
                        release.creationDate
                    }
                    if(releases.isNotEmpty()) {
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
                                items = releases
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
                                    BadgedBox(
                                        badge = {
                                            val notifications = release.getNotifications(
                                                MainActivity.notifications
                                            )
                                            if(notifications > 0) {
                                                Badge (
                                                    modifier = Modifier
                                                        .padding(
                                                            top = 10.dp
                                                        )
                                                        .size(
                                                            width = 40.dp,
                                                            height = 25.dp
                                                        )
                                                ) {
                                                    Text(
                                                        text = "$notifications"
                                                    )
                                                }
                                            }
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
                                                if(release.status == Approved) {
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
                                            MarkdownText(
                                                modifier = Modifier
                                                    .padding(
                                                        top = 5.dp
                                                    )
                                                    .fillMaxWidth(),
                                                markdown = release.releaseNotes,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        EmptyList(
                            icon = Icons.AutoMirrored.Filled.LibraryBooks,
                            description = R.string.no_releases_yet
                        )
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

    /**
     * Function to create and display the UI to create a qrcode to add members to the current [project]
     *
     * No-any params required
     */
    @Composable
    private fun CreateQrcode() {
        val mailingList = remember { mutableStateOf("") }
        val mailingListIsError = remember { mutableStateOf(false) }
        val mailingListErrorMessage = remember { mutableStateOf("") }
        var generateJoinCode by remember { mutableStateOf(true) }
        var displayQrcode by remember { mutableStateOf(false) }
        val resetLayout = {
            mailingList.value = ""
            mailingListIsError.value = false
            mailingListErrorMessage.value = ""
            generateJoinCode = true
            displayQrcode = false
            displayAddMembers.value = false
            refreshItem()
        }
        val customerSelected = remember { mutableStateOf(true) }
        val vendorSelected = remember { mutableStateOf(false) }
        var responsePayload: JsonHelper? = null
        NovaAlertDialog(
            show = displayAddMembers,
            onDismissAction = resetLayout,
            icon = Icons.Default.PersonAdd,
            title = stringResource(R.string.project_members),
            message = {
                if(displayQrcode) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .shadow(
                                    elevation = 5.dp,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .size(130.dp),
                            painter = rememberQrBitmapPainter(
                                JSONObject()
                                    .put(HOST_ADDRESS_KEY, activeLocalSession.hostAddress)
                                    .put(IDENTIFIER_KEY, responsePayload!!.getString(IDENTIFIER_KEY))
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        if(generateJoinCode) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp
                                    ),
                                text = responsePayload!!.getString(JoiningQRCode.JOIN_CODE_KEY),
                                letterSpacing = 10.sp,
                                fontSize = 20.sp
                            )
                        }
                    }
                } else {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            UserRoleBadge(
                                role = Customer,
                                selected = customerSelected,
                                onClick = {
                                    if(!customerSelected.value) {
                                        customerSelected.value = true
                                        vendorSelected.value = false
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            UserRoleBadge(
                                role = Vendor,
                                selected = vendorSelected,
                                onClick = {
                                    if(!vendorSelected.value) {
                                        customerSelected.value = false
                                        vendorSelected.value = true
                                    }
                                }
                            )
                        }
                        NovaTextField(
                            modifier = Modifier
                                .width(240.dp),
                            singleLine = false,
                            value = mailingList,
                            onValueChange = {
                                mailingListIsError.value = !isMailingListValid(it) &&
                                        mailingList.value.isNotEmpty()
                                checkToSetErrorMessage(
                                    errorMessage = mailingListErrorMessage,
                                    errorMessageKey = R.string.wrong_mailing_list,
                                    error = mailingListIsError
                                )
                                mailingList.value = it
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.separate_emails_with_a_comma),
                                    fontSize = 14.sp
                                )
                            },
                            label = R.string.mailing_list,
                            keyboardType = KeyboardType.Email,
                            errorMessage = mailingListErrorMessage,
                            isError = mailingListIsError
                        )
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = generateJoinCode,
                                onCheckedChange = { generateJoinCode = it }
                            )
                            Text(
                                text = stringResource(R.string.generate_a_join_code_also)
                            )
                        }
                    }
                }
            },
            dismissAction = resetLayout,
            confirmAction = {
                if(displayQrcode)
                    resetLayout()
                else {
                    if(isMailingListValid(mailingList.value)) {
                        mailingList.value = mailingList.value.replace(" ", "")
                        val role = if(customerSelected.value)
                            Customer
                        else
                            Vendor
                        requester.sendRequest(
                            request = {
                                requester.addMembers(
                                    projectId = project.value.id,
                                    mailingList = mailingList.value,
                                    role = role,
                                    createJoinCode = generateJoinCode
                                )
                            },
                            onSuccess = { response ->
                                responsePayload = response
                                displayQrcode = true
                            },
                            onFailure = { response ->
                                resetLayout()
                                snackbarLauncher.showSnack(
                                    message = response.getString(RESPONSE_MESSAGE_KEY)
                                )
                            }
                        )
                    } else {
                        checkToSetErrorMessage(
                            errorMessage = mailingListErrorMessage,
                            errorMessageKey = R.string.wrong_mailing_list,
                            error = mailingListIsError
                        )
                    }
                }
            }
        )
    }

    /**
     * Function to create and display the UI to add a new release to the current [project]
     *
     * No-any params required
     */
    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun AddRelease() {
        val releaseVersion = remember { mutableStateOf("") }
        val releaseVersionError = remember { mutableStateOf(false) }
        val releaseVersionErrorMessage = remember { mutableStateOf("") }
        val releaseNotes = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var releaseNotesError by remember { mutableStateOf(false) }
        val resetLayout = {
            releaseVersion.value = ""
            releaseVersionError.value = false
            releaseNotes.value = TextFieldValue("")
            releaseNotesError = false
            displayAddRelease.value = false
            refreshItem()
        }
        NovaAlertDialog(
            show = displayAddRelease,
            onDismissAction = { resetLayout() },
            icon = Icons.Default.NewReleases,
            title = stringResource(id = R.string.add_release),
            message = {
                Column (
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NovaTextField(
                        value = releaseVersion,
                        onValueChange = {
                            releaseVersionError.value = !isReleaseVersionValid(it) &&
                                    releaseVersion.value.isNotEmpty()
                            checkToSetErrorMessage(
                                errorMessage = releaseVersionErrorMessage,
                                errorMessageKey = R.string.wrong_release_version,
                                error = releaseVersionError
                            )
                            releaseVersion.value = it
                        },
                        label = R.string.release_version,
                        errorMessage = releaseVersionErrorMessage,
                        isError = releaseVersionError
                    )
                    Text(
                        text = stringResource(R.string.release_notes),
                        fontSize = 18.sp
                    )
                    Card (
                        shape = RoundedCornerShape(5.dp),
                        border = if(releaseNotesError) {
                            BorderStroke(
                                width = 2.dp,
                                color = md_theme_light_error
                            )
                        } else
                            null
                    ) {
                        MarkdownEditor(
                            modifier = Modifier
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp
                                )
                                .heightIn(
                                    min = 55.dp,
                                    max = 250.dp
                                )
                                .fillMaxWidth(),
                            value = releaseNotes.value,
                            onValueChange = { value ->
                                releaseNotesError = !areReleaseNotesValid(value.text)
                                releaseNotes.value = value.copy(text = value.text)
                            },
                            setView = {
                                it.textCursorDrawable = ContextCompat.getDrawable(
                                    this@ProjectActivity,
                                    R.drawable.custom_cursor
                                )
                            }
                        )
                    }
                }
            },
            dismissAction = { resetLayout() },
            confirmAction = {
                if(isReleaseVersionValid(releaseVersion.value)) {
                    if(areReleaseNotesValid(releaseNotes.value.text)) {
                        requester.sendRequest(
                            request = {
                                requester.addRelease(
                                    projectId = project.value.id,
                                    releaseVersion = releaseVersion.value,
                                    releaseNotes = releaseNotes.value.text
                                )
                            },
                            onSuccess = {
                                resetLayout()
                            },
                            onFailure = { response ->
                                val message = response.getString(RESPONSE_MESSAGE_KEY)
                                if(message == WRONG_RELEASE_VERSION_MESSAGE) {
                                    setErrorMessage(
                                        errorMessage = releaseVersionErrorMessage,
                                        errorMessageValue = message,
                                        error = releaseVersionError
                                    )
                                } else {
                                    snackbarLauncher.showSnack(
                                        message = message
                                    )
                                }
                            }
                        )
                    } else
                        releaseNotesError = true
                } else {
                    setErrorMessage(
                        errorMessage = releaseVersionErrorMessage,
                        errorMessageKey = R.string.wrong_release_version,
                        error = releaseVersionError
                    )
                }
            }
        )
    }

    /**
     * Function to create and display the UI to show the current members of the current [project]
     *
     * No-any params required
     */
    @Composable
    private fun ProjectMembers() {
        val sheetState = rememberModalBottomSheetState()
        if(displayMembers.value) {
            suspendRefresher()
            ModalBottomSheet(
                onDismissRequest = { displayMembers.value = false },
                sheetState = sheetState,
                containerColor = gray_background
            ) {
                LazyColumn {
                    items(
                        key = { member -> member.id},
                        items = project.value.projectMembers
                    ) { member ->
                        ListItem(
                            leadingContent = {
                                Logo(
                                    url = getMemberProfilePicUrl(member)
                                )
                            },
                            headlineContent = {
                                Row (
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${member.name} ${member.surname}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    UserRoleBadge(
                                        role = member.role
                                    )
                                }
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
                                val memberId = member.id
                                if(isProjectAuthor && memberId != activeLocalSession.id) {
                                    IconButton(
                                        onClick = {
                                            requester.sendRequest(
                                                request = {
                                                    requester.removeMember(
                                                        projectId = project.value.id,
                                                        memberId = memberId
                                                    )
                                                },
                                                onSuccess = {
                                                    refreshItem()
                                                },
                                                onFailure = { response ->
                                                    displayMembers.value = false
                                                    snackbarLauncher.showSnack(
                                                        message = response.getString(RESPONSE_MESSAGE_KEY)
                                                    )
                                                }
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PersonRemove,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        } else
            refreshItem()
    }

    /**
     * Function to create the [BitmapPainter] to display the qrcode created
     *
     * @param content: the content to display
     * @param size: the size of the container which contains the qrcode created, default value is 100.[dp]
     * @param padding: the padding of the container, default value is 0.[dp]
     */
    @Composable
    private fun rememberQrBitmapPainter(
        content: JSONObject,
        size: Dp = 100.dp,
        padding: Dp = 0.dp
    ): BitmapPainter {
        val contentSource = content.toString(4)
        var showProgress by remember { mutableStateOf(true) }
        if (showProgress) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(85.dp),
                    strokeWidth = 2.dp
                )
            }
        }
        val density = LocalDensity.current
        val sizePx = with(density) { size.roundToPx() }
        val paddingPx = with(density) { padding.roundToPx() }
        var bitmap by remember(contentSource) { mutableStateOf<Bitmap?>(null) }
        LaunchedEffect(bitmap) {
            if (bitmap != null) return@LaunchedEffect
            launch(Dispatchers.IO) {
                val qrCodeWriter = QRCodeWriter()
                val encodeHints = mutableMapOf<EncodeHintType, Any?>()
                    .apply {
                        this[EncodeHintType.MARGIN] = paddingPx
                    }
                val bitmapMatrix = try {
                    qrCodeWriter.encode(
                        contentSource, BarcodeFormat.QR_CODE,
                        sizePx, sizePx, encodeHints
                    )
                } catch (ex: WriterException) {
                    null
                }
                val matrixWidth = bitmapMatrix?.width ?: sizePx
                val matrixHeight = bitmapMatrix?.height ?: sizePx
                val newBitmap = Bitmap.createBitmap(
                    bitmapMatrix?.width ?: sizePx,
                    bitmapMatrix?.height ?: sizePx,
                    Bitmap.Config.ARGB_8888,
                )
                for (x in 0 until matrixWidth) {
                    for (y in 0 until matrixHeight) {
                        val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                        val pixelColor = if (shouldColorPixel)
                            Color.Black.toArgb()
                        else
                            Color.White.toArgb()
                        newBitmap.setPixel(x, y, pixelColor)
                    }
                }
                bitmap = newBitmap
            }
        }
        return remember(bitmap) {
            val currentBitmap = bitmap ?: Bitmap.createBitmap(
                sizePx, sizePx,
                Bitmap.Config.ARGB_8888,
            ).apply { eraseColor(android.graphics.Color.TRANSPARENT) }
            showProgress = false
            BitmapPainter(currentBitmap.asImageBitmap())
        }
    }

    /**
     * Function to refresh the current [project]
     *
     * Will be invoked the [canRefresherStart] function to check whether the [refreshRoutine] can start
     *
     * No-any params required
     */
    override fun refreshItem() {
        if(canRefresherStart()) {
            restartRefresher()
            refreshRoutine.launch {
                while (continueToFetch(this@ProjectActivity)) {
                    requester.sendRequest(
                        request = {
                            requester.getProject(
                                projectId = project.value.id
                            )
                        },
                        onSuccess = { response ->
                            project.value = Project(response.jsonObjectSource)
                        },
                        onFailure = {
                            refreshRoutine.cancel()
                            startActivity(Intent(this@ProjectActivity,
                                MainActivity::class.java))
                        },
                        onConnectionError = { response ->
                            snackbarLauncher.showSnack(response.getString(RESPONSE_MESSAGE_KEY))
                            refreshRoutine.cancel()
                        }
                    )
                    delay(1000L)
                }
            }
        }
    }

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or {@link #onPause}. This
     * is usually a hint for your activity to start interacting with the user, which is a good
     * indicator that the activity became active and ready to receive input. This sometimes could
     * also be a transit state toward another resting state. For instance, an activity may be
     * relaunched to {@link #onPause} due to configuration changes and the activity was visible,
     * but wasn’t the top-most activity of an activity task. {@link #onResume} is guaranteed to be
     * called before {@link #onPause} in this case which honors the activity lifecycle policy and
     * the activity eventually rests in {@link #onPause}.
     *
     * <p>On platform versions prior to {@link android.os.Build.VERSION_CODES#Q} this is also a good
     * place to try to open exclusive-access devices or to get access to singleton resources.
     * Starting  with {@link android.os.Build.VERSION_CODES#Q} there can be multiple resumed
     * activities in the system simultaneously, so {@link #onTopResumedActivityChanged(boolean)}
     * should be used for that purpose instead.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     * @see #onTopResumedActivityChanged(boolean)
     */
    override fun onResume() {
        super.onResume()
        activeActivity = this
    }

}