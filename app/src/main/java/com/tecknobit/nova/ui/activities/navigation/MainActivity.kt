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
import androidx.compose.material.icons.filled.Clear
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
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.user
import com.tecknobit.nova.ui.activities.session.ProfileActivity
import com.tecknobit.nova.ui.activities.session.ProjectActivity
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.novacore.InputValidator.isProjectNameValid
import java.util.Random

/**
 * The {@code MainActivity} activity is used to display and manage the user's projects and navigate
 * to the [ProfileActivity]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
class MainActivity : ComponentActivity() {

    companion object {

        /**
         * **projects** -> list of the user's projects
         */
        // TODO: TO LOAD CORRECTLY
        private val projects = mutableListOf(
            Project("Nova"),
            Project("Pandoro", "1.0.1"),
            Project("Glider", "1.0.5")
        )

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
            localSessionHelper.insertSession(
                "Prova" + Random().nextInt(),
                "prprp",
                "https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg",
                "prova.mailokay8000@gmail.com",
                "prova1234",
                "https://192.168.1.8",
                listOf(
                    com.tecknobit.novacore.records.User.Role.Vendor,
                    com.tecknobit.novacore.records.User.Role.Customer
                )[Random().nextInt(2)],
            )
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
                Scaffold (
                    floatingActionButton = {
                        Column (
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            if(user.isVendor) {
                                FloatingActionButton(
                                    onClick = { displayAddProject.value = true },
                                    containerColor = md_theme_light_primary
                                ) {
                                    Icon(
                                        imageVector = if(user.isVendor)
                                            Icons.Default.Add
                                        else
                                            Icons.Default.QrCodeScanner,
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
                                    .data(user.profilePicUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp
                                    ),
                                text = user.role.name.uppercase(),
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
                                                        Logo(url = project.logoUrl)
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
                        var projectLogo by remember { mutableStateOf("https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/v1686795211/Space%20Connect/space-exploration-sc_fm1ysf.jpg") }
                        val photoPickerLauncher = rememberLauncherForActivityResult(
                            contract = PickVisualMedia(),
                            onResult = { uri ->
                                if(uri != null) {
                                    projectLogo = uri.toString()
                                    // TODO: MAKE THE REQUEST THEN
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
                    OutlinedTextField(
                        singleLine = true,
                        value = projectName,
                        onValueChange = {
                            projectNameError = !isProjectNameValid(it) && projectName.isNotEmpty()
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
                if(isProjectNameValid(projectName)) {
                    // TODO: MAKE REQUEST THEN
                    resetLayout()
                } else
                    projectNameError = true
            }
        )
    }

}