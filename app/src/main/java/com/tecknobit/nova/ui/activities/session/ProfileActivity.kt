package com.tecknobit.nova.ui.activities.session

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.tecknobit.nova.R
import com.tecknobit.nova.R.string.please_enter_the_new_email_address
import com.tecknobit.nova.R.string.wrong_email
import com.tecknobit.nova.R.string.wrong_password
import com.tecknobit.nova.ui.activities.NovaActivity
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionsHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.requester
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.NovaTextField
import com.tecknobit.nova.ui.components.UserRoleBadge
import com.tecknobit.nova.ui.components.getFilePath
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily
import com.tecknobit.novacore.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.novacore.InputValidator.isEmailValid
import com.tecknobit.novacore.InputValidator.isPasswordValid
import com.tecknobit.novacore.helpers.LocalSessionUtils
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.records.User
import com.tecknobit.novacore.records.User.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * The {@code ProfileActivity} activity is used to manage and display the [User] details and execute
 * the operations like the change of the profile pic, change of the email, change of the password
 * and the change of the current local session
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 * @see NovaActivity
 */
class ProfileActivity : NovaActivity() {

    companion object {

        /**
         * **PASSWORD_HIDDEN** -> constant value for an hidden password
         */
        private const val PASSWORD_HIDDEN = "****"

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
                currentContext = LocalContext.current
                var profilePic by remember { mutableStateOf(activeLocalSession.profilePicUrl) }
                var currentEmail by remember { mutableStateOf(activeLocalSession.email) }
                var userPassword by remember { mutableStateOf(PASSWORD_HIDDEN) }
                val showChangeEmail = remember { mutableStateOf(false) }
                val showChangePassword = remember { mutableStateOf(false) }
                val showChangeLanguage = remember { mutableStateOf(false) }
                InitLauncher()
                val mySessions = remember { mutableStateListOf<LocalSessionUtils.NovaSession>() }
                mySessions.addAll(localSessionsHelper.sessions)
                Scaffold (
                    snackbarHost = {
                        snackbarLauncher.CreateSnackbarHost()
                    }
                ) {
                    Box {
                        Box {
                            val photoPickerLauncher = rememberLauncherForActivityResult(
                                contract = PickVisualMedia(),
                                onResult = { imageUri ->
                                    if(imageUri != null) {
                                        var imagePath: String? = null
                                        runBlocking {
                                            async {
                                                imagePath = getFilePath(
                                                    context = this@ProfileActivity,
                                                    uri = imageUri
                                                )
                                            }.await()
                                            if(imagePath != null) {
                                                requester.sendRequest(
                                                    request = {
                                                        requester.changeProfilePic(
                                                            File(imagePath!!)
                                                        )
                                                    },
                                                    onSuccess = { response ->
                                                        profilePic = response.getString(PROFILE_PIC_URL_KEY)
                                                        localSessionsHelper.changeProfilePic(
                                                            profilePic
                                                        )
                                                        activeLocalSession.profilePicUrl =
                                                            localSessionsHelper.activeSession.profilePicUrl
                                                        profilePic = activeLocalSession.profilePicUrl
                                                    },
                                                    onFailure = { response ->
                                                        snackbarLauncher.showSnack(
                                                            response.getString(RESPONSE_MESSAGE_KEY)
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(275.dp)
                                    .clickable (
                                        enabled = activeLocalSession.isHostSet
                                    ) {
                                        photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly))
                                    },
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(profilePic)
                                        // TODO: USE THE REAL LOGO
                                        .error(R.drawable.ic_launcher_background)
                                        .crossfade(500)
                                        .build()
                                ),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = {
                                    startActivity(
                                        Intent(this@ProfileActivity, MainActivity::class.java)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomStart)
                                    .padding(
                                        start = 10.dp,
                                        bottom = 60.dp
                                    ),
                                horizontalArrangement = Arrangement.Absolute.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = activeLocalSession.name,
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = activeLocalSession.surname,
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Card (
                            modifier = Modifier
                                .padding(
                                    top = 225.dp
                                )
                                .fillMaxSize(),
                            shape = RoundedCornerShape(
                                topEnd = 55.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = gray_background
                            ),
                            elevation = CardDefaults.cardElevation(10.dp),
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(15.dp)
                            ) {
                                UserInfo(
                                    header = R.string.uid,
                                    info = activeLocalSession.id
                                )
                                UserInfo(
                                    header = R.string.email,
                                    info = currentEmail,
                                    editAction = { showChangeEmail.value = true }
                                )
                                if(showChangeEmail.value) {
                                    val email = remember { mutableStateOf("") }
                                    val emailError = remember { mutableStateOf(false) }
                                    val emailErrorMessage = remember { mutableStateOf("") }
                                    NovaAlertDialog(
                                        show = showChangeEmail,
                                        icon = Icons.Default.Email,
                                        title = stringResource(R.string.change_email),
                                        message = {
                                            Column {
                                                Text(
                                                    text = stringResource(please_enter_the_new_email_address)
                                                )
                                                NovaTextField(
                                                    value = email,
                                                    onValueChange = {
                                                        emailError.value = !isEmailValid(it) &&
                                                                email.value.isNotEmpty()
                                                        checkToSetErrorMessage(
                                                            errorMessage = emailErrorMessage,
                                                            errorMessageKey = wrong_email,
                                                            error = emailError
                                                        )
                                                        email.value = it
                                                    },
                                                    label = R.string.email,
                                                    keyboardType = KeyboardType.Email,
                                                    errorMessage = emailErrorMessage,
                                                    isError = emailError
                                                )
                                            }
                                        },
                                        confirmAction = {
                                            if(isEmailValid(email.value)) {
                                                email.value = email.value.lowercase()
                                                requester.sendRequest(
                                                    request = {
                                                        requester.changeEmail(
                                                            newEmail = email.value
                                                        )
                                                    },
                                                    onSuccess = {
                                                        localSessionsHelper.changeEmail(email.value)
                                                        currentEmail = email.value
                                                        showChangeEmail.value = false
                                                    },
                                                    onFailure = {
                                                        setErrorMessage(
                                                            errorMessage = emailErrorMessage,
                                                            errorMessageKey = wrong_email,
                                                            error = emailError
                                                        )
                                                    }
                                                )
                                            } else {
                                                setErrorMessage(
                                                    errorMessage = emailErrorMessage,
                                                    errorMessageKey = wrong_email,
                                                    error = emailError
                                                )
                                            }
                                        }
                                    )
                                }
                                UserInfo(
                                    header = R.string.password,
                                    info = userPassword,
                                    onInfoClick = {
                                        userPassword = if(userPassword == PASSWORD_HIDDEN)
                                            activeLocalSession.password
                                        else
                                            PASSWORD_HIDDEN
                                    },
                                    editAction = { showChangePassword.value = true }
                                )
                                if(showChangePassword.value) {
                                    val password = remember { mutableStateOf("") }
                                    val passwordErrorMessage = remember { mutableStateOf("") }
                                    val passwordError = remember { mutableStateOf(false) }
                                    var isPasswordHidden by remember { mutableStateOf(true) }
                                    NovaAlertDialog(
                                        show = showChangePassword,
                                        icon = Icons.Default.Password,
                                        title = stringResource(R.string.change_password),
                                        message = {
                                            Column {
                                                Text(
                                                    text = stringResource(R.string.please_enter_the_new_password)
                                                )
                                                NovaTextField(
                                                    value = password,
                                                    visualTransformation = if (isPasswordHidden)
                                                        PasswordVisualTransformation()
                                                    else
                                                        VisualTransformation.None,
                                                    onValueChange = {
                                                        passwordError.value = !isPasswordValid(it) &&
                                                                password.value.isNotEmpty()
                                                        checkToSetErrorMessage(
                                                            errorMessage = passwordErrorMessage,
                                                            errorMessageKey = wrong_password,
                                                            error = passwordError
                                                        )
                                                        password.value = it
                                                    },
                                                    leadingIcon = {
                                                        IconButton(
                                                            onClick = { password.value = "" }
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Clear,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    },
                                                    label = R.string.password,
                                                    trailingIcon = {
                                                        IconButton(
                                                            onClick = { isPasswordHidden = !isPasswordHidden }
                                                        ) {
                                                            Icon(
                                                                imageVector = if(isPasswordHidden)
                                                                    Icons.Default.Visibility
                                                                else
                                                                    Icons.Default.VisibilityOff,
                                                                contentDescription = null
                                                            )
                                                        }
                                                    },
                                                    keyboardType = KeyboardType.Password,
                                                    errorMessage = passwordErrorMessage,
                                                    isError = passwordError
                                                )
                                            }
                                        },
                                        confirmAction = {
                                            if(isPasswordValid(password.value)) {
                                                requester.sendRequest(
                                                    request = {
                                                        requester.changePassword(
                                                            newPassword = password.value
                                                        )
                                                    },
                                                    onSuccess = {
                                                        localSessionsHelper.changePassword(password.value)
                                                        if(userPassword != PASSWORD_HIDDEN)
                                                            userPassword = password.value
                                                        activeLocalSession.password = password.value
                                                        showChangePassword.value = false
                                                    },
                                                    onFailure = {
                                                        setErrorMessage(
                                                            errorMessage = passwordErrorMessage,
                                                            errorMessageKey = wrong_password,
                                                            error = passwordError
                                                        )
                                                    }
                                                )
                                            } else {
                                                setErrorMessage(
                                                    errorMessage = passwordErrorMessage,
                                                    errorMessageKey = wrong_password,
                                                    error = passwordError
                                                )
                                            }
                                        }
                                    )
                                }
                                val language = activeLocalSession.language
                                UserInfo(
                                    header = R.string.language,
                                    info = language,
                                    editAction = { showChangeLanguage.value = true },
                                    isLast = true
                                )
                                if(showChangeLanguage.value) {
                                    var selectedLanguage by remember { mutableStateOf(language) }
                                    NovaAlertDialog(
                                        show = showChangeLanguage,
                                        icon = Icons.Default.Language,
                                        title = stringResource(R.string.change_language),
                                        message = {
                                            Column {
                                                LazyColumn (
                                                    modifier = Modifier
                                                        .height(150.dp)
                                                        .fillMaxWidth()
                                                ) {
                                                    items(
                                                        key = { it },
                                                        items = LANGUAGES_SUPPORTED.values.toList()
                                                    ) { language ->
                                                        Row (
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            RadioButton(
                                                                selected = language == selectedLanguage,
                                                                onClick = { selectedLanguage = language }
                                                            )
                                                            Text(
                                                                text = language
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        confirmAction = {
                                            requester.sendRequest(
                                                request = {
                                                    requester.changeLanguage(
                                                        newLanguage = selectedLanguage
                                                    )
                                                },
                                                onSuccess = {
                                                    localSessionsHelper.changeLanguage(selectedLanguage)
                                                    activeLocalSession.language = selectedLanguage
                                                    showChangeLanguage.value = false
                                                    navToSplashscreen()
                                                },
                                                onFailure = { showChangeLanguage.value = false }
                                            )
                                        }
                                    )
                                }
                                if(mySessions.size > 1) {
                                    Text(
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp
                                            ),
                                        text = stringResource(R.string.my_sessions),
                                        fontSize = 22.sp
                                    )
                                    LazyColumn (
                                        modifier = Modifier
                                            .padding(
                                                top = 5.dp
                                            ),
                                        verticalArrangement = Arrangement.spacedBy(10.dp),
                                        contentPadding = PaddingValues(
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                    ) {
                                        items(
                                            key = { it.id },
                                            items = mySessions
                                        ) { session ->
                                            val isCurrentSession = session.isActive
                                            if(session.isHostSet) {
                                                ListItem(
                                                    modifier = Modifier
                                                        .shadow(
                                                            elevation = 5.dp,
                                                            shape = RoundedCornerShape(15.dp)
                                                        )
                                                        .clickable(!isCurrentSession) {
                                                            localSessionsHelper.changeActiveSession(
                                                                session.id
                                                            )
                                                            navToSplashscreen()
                                                        }
                                                        .clip(RoundedCornerShape(15.dp)),
                                                    colors = ListItemDefaults.colors(
                                                        containerColor = Color.White
                                                    ),
                                                    leadingContent = {
                                                        Logo(
                                                            size = 80.dp,
                                                            url = session.profilePicUrl
                                                        )
                                                    },
                                                    overlineContent = {
                                                        Row (
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                        ) {
                                                            UserRoleBadge(
                                                                background = Color.White,
                                                                role = session.role
                                                            )
                                                            if(isCurrentSession) {
                                                                Text(
                                                                    text = stringResource(R.string.current)
                                                                )
                                                            }
                                                        }
                                                    },
                                                    headlineContent = {
                                                        Text(
                                                            text = session.hostAddress,
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    },
                                                    supportingContent = {
                                                        Text(
                                                            text = session.email,
                                                            fontSize = 13.sp,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    },
                                                    trailingContent = {
                                                        if(!isCurrentSession) {
                                                            IconButton(
                                                                modifier = Modifier
                                                                    .size(22.dp),
                                                                onClick = {
                                                                    localSessionsHelper.deleteSession(session.id)
                                                                    mySessions.remove(session)
                                                                }
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.Delete,
                                                                    contentDescription = null
                                                                )
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                        item {
                                            ActionButtons()
                                        }
                                    }
                                } else
                                    ActionButtons()
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to display a specific [user] detail
     *
     * @param header: the header of the section, so the detail displayed
     * @param editAction: the edit action to execute, if is an editable data
     * @param info: the info detail to display
     * @param onInfoClick: the action to execute when the info is clicked, for example hidden/unhidden
     * the password data
     * @param isLast: whether the section displayed is the last to display
     */
    @Composable
    private fun UserInfo(
        header: Int,
        editAction: (() -> Unit)? = null,
        info: String,
        onInfoClick: (() -> Unit)? = null,
        isLast: Boolean = false
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ){
            Text(
                text = stringResource(header),
                fontFamily = thinFontFamily
            )
            if(editAction != null && activeLocalSession.isHostSet) {
                Button(
                    modifier = Modifier
                        .height(25.dp),
                    onClick = editAction,
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(
                        start = 10.dp,
                        end = 10.dp,
                        top = 0.dp,
                        bottom = 0.dp
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit),
                        fontSize = 12.sp
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .clickable(
                    enabled = onInfoClick != null
                ) {
                    onInfoClick!!()
                },
            text = info,
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            color = Color.LightGray
        )
        if(!isLast)
            Spacer(modifier = Modifier.height(5.dp))
    }

    /**
     * Function to create and display a button to execute an action like logout and delete of the account
     *
     * @param action: the action to execute when the button is clicked
     * @param text: the text to display on the button
     * @param color: the color of the button, default value [md_theme_light_primary]
     */
    @Composable
    private fun ActionButton(
        action: () -> Unit,
        text: Int,
        color: Color = md_theme_light_primary
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(
                    elevation = 5.dp,
                    shape = RoundedCornerShape(10.dp)
                ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = color
            ),
            onClick = action
        ) {
            Text(
                text = stringResource(text),
                fontSize = 18.sp
            )
        }
    }

    /**
     * Function to create and display the action buttons section
     *
     * No-any params required
     */
    @Composable
    private fun ActionButtons() {
        val logout = remember { mutableStateOf(false) }
        val deleteAccount = remember { mutableStateOf(false) }
        Spacer(modifier = Modifier.height(10.dp))
        ActionButton(
            action = { logout.value = true },
            text = R.string.logout
        )
        NovaAlertDialog(
            show = logout,
            icon = Icons.AutoMirrored.Filled.Logout,
            title = R.string.logout,
            message = R.string.account_logout_message,
            dismissAction = { logout.value = false },
            confirmAction = {
                localSessionsHelper.deleteAllSessions()
                requester.setUserCredentials(null, null)
                navToSplashscreen()
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        ActionButton(
            color = com.tecknobit.nova.ui.theme.tagstheme.bug.md_theme_light_primary,
            action = { deleteAccount.value = true },
            text = R.string.delete_account
        )
        NovaAlertDialog(
            show = deleteAccount,
            icon = Icons.Default.Clear,
            title = R.string.delete_account,
            message = R.string.account_deletion_message,
            dismissAction = { deleteAccount.value = false },
            confirmAction = {
                val deleteSuccessAction = {
                    localSessionsHelper.deleteSession(activeLocalSession.id)
                    val sessions = localSessionsHelper.sessions
                    if(sessions.isNotEmpty())
                        localSessionsHelper.changeActiveSession(sessions.first().id)
                    deleteAccount.value = false
                    navToSplashscreen()
                }
                if(activeLocalSession.isHostSet) {
                    requester.sendRequest(
                        request = { requester.deleteAccount() },
                        onSuccess = {
                            requester.setUserCredentials(null, null)
                            deleteSuccessAction.invoke()
                        },
                        onFailure = { response ->
                            deleteAccount.value = false
                            snackbarLauncher.showSnack(response.getString(RESPONSE_MESSAGE_KEY))
                        }
                    )
                } else
                    deleteSuccessAction.invoke()
            }
        )
    }

    /**
     * Function to navigate to the [Splashscreen]
     *
     * No-any params required
     */
    private fun navToSplashscreen() {
        startActivity(Intent(this@ProfileActivity, Splashscreen::class.java))
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