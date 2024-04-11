package com.tecknobit.nova.ui.activities.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.nova.R
import com.tecknobit.nova.R.string.scan_to_join_in_a_project
import com.tecknobit.nova.helpers.utils.AndroidRequester
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionsHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.requester
import com.tecknobit.nova.ui.activities.session.ProfileActivity
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.NovaTextField
import com.tecknobit.nova.ui.components.getFilePath
import com.tecknobit.nova.ui.components.getProjectLogoUrl
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.novacore.InputValidator.isProjectNameValid
import com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession.HOST_ADDRESS_KEY
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.helpers.Requester.ListFetcher
import com.tecknobit.novacore.records.NovaNotification
import com.tecknobit.novacore.records.User
import com.tecknobit.novacore.records.User.IDENTIFIER_KEY
import com.tecknobit.novacore.records.User.PROFILE_PIC_URL_KEY
import com.tecknobit.novacore.records.User.PROJECTS_KEY
import com.tecknobit.novacore.records.User.Role
import com.tecknobit.novacore.records.User.TOKEN_KEY
import com.tecknobit.novacore.records.project.Project
import com.tecknobit.novacore.records.project.Project.PROJECT_IDENTIFIER_KEY
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * The {@code MainActivity} activity is used to display and manage the user's projects and navigate
 * to the [ProfileActivity]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see NovaActivity
 * @see ListFetcher
 *
 */
class MainActivity : NovaActivity(), ListFetcher {

    companion object {

        /**
         * **projects** -> list of the user's projects
         */
        val projects = mutableStateListOf<Project>()

        /**
         * **notifications** -> list of the user's notifications
         */
        val notifications = mutableStateListOf<NovaNotification>()

        /**
         * **scanOptions** -> the options used when the scan to join in a [Project] starts
         */
        private var scanOptions = ScanOptions()
            .setBeepEnabled(false)
            .setOrientationLocked(false)

    }

    /**
     * **displayAddProject** -> whether display the button to add a project, this happens
     * when the current user is a [User.Role.Vendor]
     */
    private lateinit var displayAddProject: MutableState<Boolean>

    /**
     * **barcodeLauncher** -> the launcher used to start the scan and use the [scanOptions]
     */
    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> =
        registerForActivityResult(ScanContract()) { result ->
            val content = result.contents
            if(content != null) {
                val restartWorkflow = {
                    requester.setUserCredentials(
                        userId = activeLocalSession.id,
                        userToken = activeLocalSession.token
                    )
                    refreshList()
                }
                try {
                    val helper = JsonHelper(content)
                    val identifier = helper.getString(IDENTIFIER_KEY, null)
                    val hostAddress = helper.getString(HOST_ADDRESS_KEY, null)
                    if(identifier != null && hostAddress != null) {
                        val email = activeLocalSession.email
                        val name = activeLocalSession.name
                        val surname = activeLocalSession.surname
                        val password = activeLocalSession.password
                        requester = AndroidRequester(
                            host = hostAddress
                        )
                        requester.sendRequest(
                            request = {
                                requester.joinWithId(
                                    id = identifier,
                                    email = email,
                                    name = name,
                                    surname = surname,
                                    password = password
                                )
                            },
                            onSuccess = { response ->
                                val payloadResponse = response.getJSONObject(RESPONSE_MESSAGE_KEY)
                                val userIdentifier = response.getString(IDENTIFIER_KEY)
                                val token = response.getString(TOKEN_KEY)
                                if(payloadResponse.has(TOKEN_KEY)) {
                                    localSessionsHelper.insertSession(
                                        userIdentifier,
                                        token,
                                        response.getString(PROFILE_PIC_URL_KEY),
                                        name,
                                        surname,
                                        email,
                                        password,
                                        hostAddress,
                                        Role.valueOf(response.getString(User.ROLE_KEY)),
                                        activeLocalSession.language
                                    )
                                } else {
                                    if(activeLocalSession.id != userIdentifier)
                                        localSessionsHelper.changeActiveSession(userIdentifier)
                                }
                                activeLocalSession = localSessionsHelper.activeSession
                                restartWorkflow.invoke()
                            },
                            onFailure = { response ->
                                snackbarLauncher.showSnack(
                                    message = response.getString(RESPONSE_MESSAGE_KEY)
                                )
                                restartWorkflow.invoke()
                            }
                        )
                    } else
                        restartWorkflow.invoke()
                } catch (e : IllegalArgumentException) {
                    restartWorkflow.invoke()
                }
            } else
                refreshList()
        }

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
            NovaTheme {
                displayAddProject = remember { mutableStateOf(false) }
                scanOptions.setPrompt(LocalContext.current.getString(scan_to_join_in_a_project))
                InitLauncher()
                currentContext = LocalContext.current
                refreshList()
                Scaffold (
                    floatingActionButton = {
                        Column (
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if(activeLocalSession.isVendor) {
                                FloatingActionButton(
                                    onClick = {
                                        displayAddProject.value = true
                                        suspendRefresher()
                                    },
                                    containerColor = md_theme_light_primary
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )
                                }
                                AddProject()
                            }
                            FloatingActionButton(
                                onClick = {
                                    suspendRefresher()
                                    barcodeLauncher.launch(scanOptions)
                                },
                                containerColor = md_theme_light_primary
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    snackbarHost = {
                        snackbarLauncher.CreateSnackbarHost()
                    }
                ) {
                    Column (
                        modifier = Modifier
                            .background(md_theme_light_primary)
                            .fillMaxSize()
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .shadow(
                                        elevation = 5.dp,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                                    .clickable {
                                        startActivity(
                                            Intent(this@MainActivity, ProfileActivity::class.java)
                                        )
                                    }
                                    .size(150.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(activeLocalSession.profilePicUrl)
                                    .crossfade(500)
                                    // TODO: USE THE REAL LOGO 
                                    .error(R.drawable.ic_launcher_background)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    ),
                                text = activeLocalSession.role.name.uppercase(),
                                fontSize = 25.sp,
                                color = Color.White
                            )
                        }
                        Card (
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
                                                        intent.putExtra(PROJECT_IDENTIFIER_KEY, project.id)
                                                        startActivity(intent)
                                                    },
                                                colors = ListItemDefaults.colors(
                                                    containerColor = Color.White
                                                ),
                                                leadingContent = {
                                                    BadgedBox(
                                                        badge = {
                                                            val notifications = project.getNotifications(
                                                                notifications
                                                            )
                                                            if(notifications > 0) {
                                                                Badge {
                                                                    Text(
                                                                        text = "$notifications"
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    ) {
                                                        Logo(
                                                            url = getProjectLogoUrl(project)
                                                        )
                                                    }
                                                },
                                                headlineContent = {
                                                    Text(
                                                        text = project.name,
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                },
                                                supportingContent = {
                                                    val workingProgressVersionText = project.workingProgressVersion
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
                                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
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

    /**
     * Function to display the UI to add a new project
     *
     * No-any params required
     */
    @Composable
    private fun AddProject() {
        val context = LocalContext.current
        val projectName = remember { mutableStateOf("") }
        val isError = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }
        // TODO: USE THE NOVA PROJECT LOGO AS DEFAULT IMAGE
        var projectLogo by remember { mutableStateOf("https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg") }
        var selectedLogo = File(projectLogo)
        val resetLayout = {
            errorMessage.value = ""
            projectName.value = ""
            isError.value = false
            projectLogo = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg"
            selectedLogo = File(projectLogo)
            displayAddProject.value = false
            refreshList()
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
                        val photoPickerLauncher = rememberLauncherForActivityResult(
                            contract = PickVisualMedia(),
                            onResult = { uri ->
                                if(uri != null) {
                                    projectLogo = uri.toString()
                                    val filePath = getFilePath(
                                        context = context,
                                        uri = uri
                                    )!!
                                    projectLogo = filePath
                                    selectedLogo = File(filePath)
                                }
                            }
                        )
                        AsyncImage(
                            modifier = Modifier
                                .size(125.dp)
                                .clip(CircleShape),
                            model = projectLogo,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xD0DFD8D8))
                                .align(Alignment.BottomEnd),
                            onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly)) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                    NovaTextField(
                        value = projectName,
                        onValueChange = {
                            isError.value = !isProjectNameValid(it) && projectName.value.isNotEmpty()
                            checkToSetErrorMessage(
                                errorMessage = errorMessage,
                                errorMessageKey = R.string.name_is_not_valid,
                                error = isError
                            )
                            projectName.value = it
                        },
                        label = R.string.name,
                        errorMessage = errorMessage,
                        isError = isError
                    )
                }
            },
            dismissAction = resetLayout,
            confirmAction = {
                if(isProjectNameValid(projectName.value)) {
                    requester.sendRequest(
                        request = {
                            requester.addProject(
                                logoPic = selectedLogo,
                                projectName = projectName.value
                            )
                        },
                        onSuccess = {
                            resetLayout()
                        },
                        onFailure = { response ->
                            setErrorMessage(
                                errorMessage = errorMessage,
                                errorMessageValue = response.getString(RESPONSE_MESSAGE_KEY),
                                error = isError
                            )
                        }
                    )
                } else {
                    setErrorMessage(
                        errorMessage = errorMessage,
                        errorMessageKey = R.string.name_is_not_valid,
                        error = isError
                    )
                }
            }
        )
    }

    /**
     * Function to refresh the current [projects]
     *
     * Will be invoked the [canRefresherStart] function to check whether the [refreshRoutine] can start
     *
     * No-any params required
     */
    override fun refreshList() {
        if(canRefresherStart()) {
            restartRefresher()
            refreshRoutine.launch {
                while (continueToFetch(this@MainActivity)) {
                    requester.sendRequest(
                        request = { requester.listProjects() },
                        onSuccess = { response ->
                            val memberProjects = response.getJSONArray(PROJECTS_KEY)
                            projects.clear()
                            for(j in 0 until memberProjects.length())
                                projects.add(Project(memberProjects.getJSONObject(j)))
                        },
                        onFailure = { response ->
                            snackbarLauncher.showSnack(response.getString(RESPONSE_MESSAGE_KEY))
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