@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.Project.PROJECT_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.RELEASE_KEY
import com.tecknobit.nova.helpers.toImportFromCoreLibrary.release.Release.ReleaseStatus
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.components.EmptyList
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.ReleaseStatusBadge
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_error
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
class ProjectActivity : ComponentActivity() {

    private lateinit var project: MutableState<Project>

    private lateinit var displayAddMembers: MutableState<Boolean>

    private lateinit var displayAddRelease: MutableState<Boolean>

    private lateinit var displayMembers: MutableState<Boolean>

    private var isProjectAuthor: Boolean = false

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
            // TODO: MAKE THE REAL WORKFLOW TO GET IF THE USER IS OR NOT THE PROJECT AUTHOR
            isProjectAuthor = Random().nextBoolean()
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
                                // TODO: MAKE THE WORKFLOW TO HIDE IF THE MEMBER IS NOT A VENDOR OR THE AUTHOR
                                IconButton(
                                    onClick = { displayAddMembers.value = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                CreateQrcode()
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
                                    onClick = { showWorkOnProject.value = true }
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
                                        // TODO: MAKE THE REQUEST THEN
                                        showWorkOnProject.value = false
                                        startActivity(navBackIntent)
                                    }
                                )
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { displayAddRelease.value = true },
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
                    val releases = project.value.releases
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
                                        MarkdownText(
                                            modifier = Modifier
                                                .padding(
                                                    top = 5.dp
                                                )
                                                .fillMaxWidth(),
                                            markdown = release.releaseNotes.content,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        EmptyList(
                            icon = painterResource(id = R.drawable.releases_foreground),
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

    @Composable
    private fun CreateQrcode() {
        val mailingList = remember { mutableStateOf("") }
        val mailingListIsError = remember { mutableStateOf(false) }
        var displayQrcode by remember { mutableStateOf(false) }
        val resetLayout = {
            mailingList.value = ""
            mailingListIsError.value = false
            displayQrcode = false
            displayAddMembers.value = false
        }
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
                                .size(175.dp),
                            painter = rememberQrBitmapPainter(
                                JSONObject().put("data", "datafromserver").toString()
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = mailingList.value,
                        onValueChange = {
                            mailingListIsError.value = it.isEmpty() && mailingList.value.isNotEmpty()
                            mailingList.value = it
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.separate_emails_with_a_comma),
                                fontSize = 14.sp
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.mailing_list)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { mailingList.value = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        },
                        isError = mailingListIsError.value
                    )
                }
            },
            dismissAction = resetLayout,
            confirmAction = {
                if(displayQrcode)
                    resetLayout()
                else {
                    if(mailingList.value.isNotEmpty()) {
                        mailingList.value = mailingList.value.replace(" ", "")
                        // TODO: MAKE REQUEST THEN
                        displayQrcode = true
                    } else
                        mailingListIsError.value = true
                }
            }
        )
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun AddRelease() {
        var releaseVersion by remember { mutableStateOf("") }
        var releaseVersionError by remember { mutableStateOf(false) }
        val releaseNotes = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        var releaseNotesError by remember { mutableStateOf(false) }
        val resetLayout = {
            releaseVersion = ""
            releaseVersionError = false
            releaseNotes.value = TextFieldValue("")
            releaseNotesError = false
            displayAddRelease.value = false
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
                    OutlinedTextField(
                        singleLine = true,
                        value = releaseVersion,
                        onValueChange = {
                            releaseVersionError = it.isEmpty() && releaseVersion.isNotEmpty()
                            releaseVersion = it
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.release_version)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { releaseVersion = "" }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        },
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
                                releaseNotesError = value.text.isEmpty()
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
                if(releaseVersion.isNotEmpty()) {
                    if(releaseNotes.value.text.isNotEmpty()) {
                        // TODO: MOVE THIS CHECK ON SERVER
                        releaseVersion = releaseVersion.removePrefix("v.")
                        if(!releaseVersion.startsWith(" "))
                            releaseVersion = " $releaseVersion"
                        Log.d("gagagagagaga", "v.$releaseVersion")
                        // TODO: MAKE THE REQUEST THEN
                        resetLayout()
                    } else
                        releaseNotesError = true
                } else
                    releaseVersionError = true
            }
        )
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
                                if(isProjectAuthor) {
                                    // TODO: MAKE THE REAL WORKFLOW TO HIDE THE POSSIBILITY TO BE REMOVED BY SELF
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
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }

    @Composable
    private fun rememberQrBitmapPainter(
        content: String,
        size: Dp = 150.dp,
        padding: Dp = 0.dp
    ): BitmapPainter {
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
        var bitmap by remember(content) { mutableStateOf<Bitmap?>(null) }
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
                        content, BarcodeFormat.QR_CODE,
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
                            md_theme_light_primary.toArgb()
                        else
                            Color.Transparent.toArgb()
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

}