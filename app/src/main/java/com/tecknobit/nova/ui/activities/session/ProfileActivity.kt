package com.tecknobit.nova.ui.activities.session

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.nova.R
import com.tecknobit.nova.R.string.please_enter_the_new_email_address
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionsHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.requester
import com.tecknobit.nova.ui.components.Logo
import com.tecknobit.nova.ui.components.NovaAlertDialog
import com.tecknobit.nova.ui.components.UserRoleBadge
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily
import com.tecknobit.novacore.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.novacore.InputValidator.isEmailValid
import com.tecknobit.novacore.InputValidator.isPasswordValid
import com.tecknobit.novacore.helpers.LocalSessionUtils

/**
 * The {@code ProfileActivity} activity is used to manage and display the [User] details and execute
 * the operations like the change of the profile pic, change of the email, change of the password
 * and the change of the current local session
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
class ProfileActivity : ComponentActivity() {

    companion object {

        /**
         * **PASSWORD_HIDDEN** -> constant value for an hidden password
         */
        private const val PASSWORD_HIDDEN = "****";

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                var profilePic by remember { mutableStateOf(activeLocalSession.profilePicUrl) }
                var currentEmail by remember { mutableStateOf(activeLocalSession.email) }
                var userPassword by remember { mutableStateOf(PASSWORD_HIDDEN) }
                val showChangeEmail = remember { mutableStateOf(false) }
                val showChangePassword = remember { mutableStateOf(false) }
                val showChangeLanguage = remember { mutableStateOf(false) }
                val mySessions = remember { mutableStateListOf<LocalSessionUtils.NovaSession>() }
                mySessions.addAll(localSessionsHelper.sessions)
                Box {
                    Box {
                        val photoPickerLauncher = rememberLauncherForActivityResult(
                            contract = PickVisualMedia(),
                            onResult = { uri ->
                                if(uri != null) {
                                    profilePic = uri.toString()
                                    // TODO: MAKE THE REQUEST THEN
                                }
                            }
                        )
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(275.dp)
                                .clickable {
                                    photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly))
                                },
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profilePic)
                                .crossfade(true)
                                .build(),
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
                                var email by remember { mutableStateOf("") }
                                var emailError by remember { mutableStateOf(false) }
                                NovaAlertDialog(
                                    show = showChangeEmail,
                                    icon = Icons.Default.Email,
                                    title = stringResource(R.string.change_email),
                                    message = {
                                        Column {
                                            Text(
                                                text = stringResource(please_enter_the_new_email_address)
                                            )
                                            OutlinedTextField(
                                                singleLine = true,
                                                value = email,
                                                onValueChange = {
                                                    emailError = !isEmailValid(it) &&
                                                            email.isNotEmpty()
                                                    email = it
                                                },
                                                label = {
                                                    Text(
                                                        text = stringResource(R.string.email)
                                                    )
                                                },
                                                trailingIcon = {
                                                    IconButton(
                                                        onClick = { email = "" }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Clear,
                                                            contentDescription = null
                                                        )
                                                    }
                                                },
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Email
                                                ),
                                                isError = emailError
                                            )
                                        }
                                    },
                                    confirmAction = {
                                        if(isEmailValid(email)) {
                                            email = email.lowercase()
                                            requester.sendRequest(
                                                request = {
                                                    requester.changeEmail(
                                                        newEmail = email
                                                    )
                                                },
                                                onSuccess = {
                                                    localSessionsHelper.changeEmail(email)
                                                    currentEmail = email
                                                    showChangeEmail.value = false
                                                },
                                                onFailure = { emailError = true }
                                            )
                                        } else
                                            emailError = true
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
                                var password by remember { mutableStateOf("") }
                                var passwordError by remember { mutableStateOf(false) }
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
                                            OutlinedTextField(
                                                singleLine = true,
                                                value = password,
                                                visualTransformation = if (isPasswordHidden)
                                                    PasswordVisualTransformation()
                                                else
                                                    VisualTransformation.None ,
                                                onValueChange = {
                                                    passwordError = !isPasswordValid(it) &&
                                                            password.isNotEmpty()
                                                    password = it
                                                },
                                                leadingIcon = {
                                                    IconButton(
                                                        onClick = { password = "" }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Clear,
                                                            contentDescription = null
                                                        )
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        text = stringResource(R.string.password)
                                                    )
                                                },
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
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Password
                                                ),
                                                isError = passwordError
                                            )
                                        }
                                    },
                                    confirmAction = {
                                        if(isPasswordValid(password)) {
                                            requester.sendRequest(
                                                request = {
                                                    requester.changePassword(
                                                        newPassword = password
                                                    )
                                                },
                                                onSuccess = {
                                                    localSessionsHelper.changePassword(password)
                                                    if(userPassword != PASSWORD_HIDDEN)
                                                        userPassword = password
                                                    activeLocalSession.password = password
                                                    showChangePassword.value = false
                                                },
                                                onFailure = { passwordError = true }
                                            )
                                        } else
                                            passwordError = true
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
                                            onFailure = {showChangeLanguage.value = false }
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
                                        ListItem(
                                            modifier = Modifier
                                                .shadow(
                                                    elevation = 5.dp,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .clickable(!isCurrentSession) {
                                                    localSessionsHelper.changeActiveSession(session.id)
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
            if(editAction != null) {
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
                localSessionsHelper.sessions.forEach { session ->
                    val sessionId = session.id
                    requester.setUserCredentials(session.id, session.token)
                    requester.changeHost(session.hostAddress)
                    requester.sendRequest(
                        request = { requester.deleteAccount() },
                        onSuccess = { localSessionsHelper.deleteSession(sessionId) },
                        onFailure = { localSessionsHelper.changeActiveSession(sessionId) }
                    )
                }
                deleteAccount.value = false
                navToSplashscreen()
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

}