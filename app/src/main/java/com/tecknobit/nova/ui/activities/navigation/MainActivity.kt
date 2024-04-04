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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.tecknobit.nova.R
import com.tecknobit.nova.R.string.scan_to_join_in_a_project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
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
import com.tecknobit.novacore.InputValidator.*
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.helpers.Requester.ListFetcher
import com.tecknobit.novacore.records.User.AUTHORED_PROJECTS_KEY
import com.tecknobit.novacore.records.User.PROJECTS_KEY
import com.tecknobit.novacore.records.project.Project
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Random

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
class MainActivity : NovaActivity(), ListFetcher<Project> {

    companion object {

        /**
         * **projects** -> list of the user's projects
         */
        private val projects = mutableStateListOf<Project>()

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
            // TODO: MAKE THE REAL WORKFLOW

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
                refreshRoutine = rememberCoroutineScope()
                refreshList()
                Scaffold (
                    floatingActionButton = {
                        Column (
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if(activeLocalSession.isVendor) {
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
                            FloatingActionButton(
                                onClick = { barcodeLauncher.launch(scanOptions) },
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
                                                        intent.putExtra(PROJECT_KEY, project)
                                                        startActivity(intent)
                                                    },
                                                colors = ListItemDefaults.colors(
                                                    containerColor = Color.White
                                                ),
                                                leadingContent = {
                                                    BadgedBox(
                                                        badge = {
                                                            // TODO: USE THE REAL NOTIFICATIONS LIST OF THE user
                                                            // val notifications = project.getNotifications()
                                                            val notifications = Random().nextInt(22)
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
        var projectName = remember { mutableStateOf("") }
        var isError = remember { mutableStateOf(false) }
        var errorMessage = remember { mutableStateOf("") }
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
                            // TODO: TO USE IN SOME WAY
                            // response.getString(RESPONSE_MESSAGE_KEY)
                            setErrorMessage(
                                errorMessage = errorMessage,
                                errorMessageKey = R.string.name_is_not_valid,
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

    override fun refreshList() {
        refreshRoutine.launch {
            while (continueToFetch(this@MainActivity)) {
                requester.sendRequest(
                    request = { requester.listProjects() },
                    onSuccess = { response ->
                        val projectsRefreshed = arrayListOf<Project>()
                        val memberProjects = response.getJSONArray(PROJECTS_KEY)
                        val authoredProjects = response.getJSONArray(AUTHORED_PROJECTS_KEY)
                        for(j in 0 until memberProjects.length())
                            projectsRefreshed.add(Project(memberProjects.getJSONObject(j)))
                        for(j in 0 until authoredProjects.length())
                            projectsRefreshed.add(Project(authoredProjects.getJSONObject(j)))
                        projects.clear()
                        projects.addAll(projectsRefreshed)
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