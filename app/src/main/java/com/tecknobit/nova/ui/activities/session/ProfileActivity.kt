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
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.user
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

class ProfileActivity : ComponentActivity() {

    companion object {

        private const val PASSWORD_HIDDEN = "****";

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                var profilePic by remember { mutableStateOf(user.profilePicUrl) }
                val showChangeEmail = remember { mutableStateOf(false) }
                val showChangePassword = remember { mutableStateOf(false) }
                val showChangeLanguage = remember { mutableStateOf(false) }
                var userPassword by remember { mutableStateOf(PASSWORD_HIDDEN) }
                val mySessions = remember { mutableStateListOf<LocalSessionUtils.NovaSession>() }
                mySessions.addAll(localSessionHelper.sessions)
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
                                text = user.name,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user.surname,
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
                                info = user.id
                            )
                            UserInfo(
                                header = R.string.email,
                                info = user.email,
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
                                            // TODO: MAKE REQUEST THEN
                                            email = email.lowercase()
                                            showChangeEmail.value = false
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
                                        user.password
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
                                                    VisualTransformation.None
                                                else
                                                    PasswordVisualTransformation(),
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
                                            // TODO: MAKE REQUEST THEN
                                            showChangePassword.value = false
                                        } else
                                            passwordError = true
                                    }
                                )
                            }
                            UserInfo(
                                header = R.string.language,
                                info = user.language,
                                editAction = { showChangeLanguage.value = true },
                                isLast = true
                            )
                            if(showChangeLanguage.value) {
                                var selectedLanguage by remember { mutableStateOf(user.language) }
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
                                                    items = LANGUAGES_SUPPORTED
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
                                        // TODO: MAKE REQUEST THEN
                                        showChangeLanguage.value = false
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
                                        val isCurrentSession = user.hostAddress == session.hostAddress
                                        ListItem(
                                            modifier = Modifier
                                                .shadow(
                                                    elevation = 5.dp,
                                                    shape = RoundedCornerShape(15.dp)
                                                )
                                                .clickable(!isCurrentSession) {
                                                    // TODO: SELECT NEW SESSION THEN
                                                    startActivity(
                                                        Intent(
                                                            this@ProfileActivity,
                                                            Splashscreen::class.java
                                                        )
                                                    )
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
                                                IconButton(
                                                    modifier = Modifier
                                                        .size(22.dp),
                                                    onClick = {
                                                        localSessionHelper.deleteSession(session.id)
                                                        mySessions.remove(session)
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = null
                                                    )
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

    @Composable
    private fun ActionButtons() {
        Spacer(modifier = Modifier.height(10.dp))
        ActionButton(
            action = {
                // TODO: MAKE REQUEST THEN
                localSessionHelper.deleteAllSessions()
                startActivity(Intent(this@ProfileActivity, Splashscreen::class.java))
            },
            text = R.string.logout
        )
        Spacer(modifier = Modifier.height(10.dp))
        ActionButton(
            color = com.tecknobit.nova.ui.theme.tagstheme.bug.md_theme_light_primary,
            action = {
                // TODO: MAKE REQUEST THEN
                localSessionHelper.deleteAllSessions()
                startActivity(Intent(this@ProfileActivity, Splashscreen::class.java))
            },
            text = R.string.delete_account
        )
    }

}