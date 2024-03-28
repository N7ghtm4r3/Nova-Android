package com.tecknobit.nova.ui.activities.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.utils.ui.SnackbarLauncher
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.novacore.InputValidator.isEmailValid
import com.tecknobit.novacore.InputValidator.isHostValid
import com.tecknobit.novacore.InputValidator.isNameValid
import com.tecknobit.novacore.InputValidator.isPasswordValid
import com.tecknobit.novacore.InputValidator.isServerSecretValid
import com.tecknobit.novacore.InputValidator.isSurnameValid

class AuthActivity : ComponentActivity() {

    private lateinit var snackbarLauncher: SnackbarLauncher

    private lateinit var name: MutableState<String>

    private lateinit var nameError: MutableState<Boolean>

    private lateinit var surname: MutableState<String>

    private lateinit var surnameError: MutableState<Boolean>

    private lateinit var email: MutableState<String>

    private lateinit var emailError: MutableState<Boolean>

    private lateinit var password: MutableState<String>

    private lateinit var passwordError: MutableState<Boolean>

    private lateinit var isPasswordHidden: MutableState<Boolean>

    private lateinit var isRegisterOpe: MutableState<Boolean>

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                snackbarLauncher = SnackbarLauncher(LocalContext.current)
                snackbarLauncher.InitSnackbarInstances()
                val pagerState = rememberPagerState(pageCount = { 2 })
                var host by remember { mutableStateOf("") }
                val hostError = remember { mutableStateOf(false) }
                var serverSecret by remember { mutableStateOf("") }
                val serverSecretError = remember { mutableStateOf(false) }
                name = remember { mutableStateOf("") }
                nameError = remember { mutableStateOf(false) }
                surname = remember { mutableStateOf("") }
                surnameError = remember { mutableStateOf(false) }
                email = remember { mutableStateOf("") }
                emailError = remember { mutableStateOf(false) }
                password = remember { mutableStateOf("") }
                passwordError = remember { mutableStateOf(false) }
                isPasswordHidden = remember { mutableStateOf(true) }
                isRegisterOpe = remember { mutableStateOf(true) }
                val clearInputs = {
                    host = ""
                    hostError.value = false
                    serverSecret = ""
                    serverSecretError.value = false
                    name.value = ""
                    nameError.value = false
                    surname.value = ""
                    surnameError.value = false
                    email.value = ""
                    emailError.value = false
                    password.value = ""
                    passwordError.value = false
                }
                Scaffold (
                    snackbarHost = { snackbarLauncher.CreateSnackbarHost() }
                ) {
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = pagerState
                    ) { page ->
                        if(page == 0) {
                            UIContainer(
                                subtitle = R.string.authenticate_on_a_server,
                                content = {
                                    OutlinedTextField(
                                        singleLine = true,
                                        value = host,
                                        onValueChange = {
                                            hostError.value = !isHostValid(it) && host.isNotEmpty()
                                            host = it
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(R.string.host)
                                            )
                                        },
                                        trailingIcon = {
                                            IconButton(
                                                onClick = { host = "" }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Clear,
                                                    contentDescription = null
                                                )
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        isError = hostError.value
                                    )
                                    if(isRegisterOpe.value) {
                                        OutlinedTextField(
                                            modifier = Modifier
                                                .padding(
                                                    top = 10.dp,
                                                ),
                                            singleLine = true,
                                            value = serverSecret,
                                            onValueChange = {
                                                serverSecretError.value = !isServerSecretValid(it)
                                                        && serverSecret.isNotEmpty()
                                                serverSecret = it
                                            },
                                            label = {
                                                Text(
                                                    text = stringResource(R.string.server_secret)
                                                )
                                            },
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = { serverSecret = "" }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Clear,
                                                        contentDescription = null
                                                    )
                                                }
                                            },
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                            isError = serverSecretError.value
                                        )
                                        Form()
                                    } else
                                        EmailPasswordForm()
                                    AuthButton(
                                        authAction = {
                                            if(isHostValid(host)) {
                                                if(isRegisterOpe.value) {
                                                    if(isServerSecretValid(serverSecret)) {
                                                        execAuth {
                                                            // TODO: MAKE REQUEST THEN
                                                            email.value = email.value.lowercase()
                                                            startActivity(
                                                                Intent(this@AuthActivity,
                                                                    MainActivity::class.java)
                                                            )
                                                        }
                                                    } else {
                                                        snackbarLauncher.showSnackError(
                                                            R.string.wrong_server_secret,
                                                            serverSecretError
                                                        )
                                                    }
                                                } else {
                                                    if(isEmailValid(email.value)) {
                                                        if(isPasswordValid(password.value)) {
                                                            // TODO: MAKE REQUEST THEN
                                                            startActivity(
                                                                Intent(this@AuthActivity,
                                                                    MainActivity::class.java)
                                                            )
                                                        } else {
                                                            snackbarLauncher.showSnackError(
                                                                R.string.wrong_password,
                                                                passwordError
                                                            )
                                                        }
                                                    } else {
                                                        snackbarLauncher.showSnackError(
                                                            R.string.wrong_email,
                                                            emailError
                                                        )
                                                    }
                                                }
                                            } else {
                                                snackbarLauncher.showSnackError(
                                                    R.string.wrong_host_address,
                                                    hostError
                                                )
                                            }
                                        },
                                        btnText = if(isRegisterOpe.value)
                                            R.string.sign_up
                                        else
                                            R.string.sign_in
                                    )
                                    Row (
                                        modifier = Modifier
                                            .padding(
                                                top = 10.dp
                                            ),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = stringResource(
                                                if(isRegisterOpe.value)
                                                    R.string.have_an_account
                                                else
                                                    R.string.are_you_new_to_nova
                                            ),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .clickable {
                                                    clearInputs()
                                                    isRegisterOpe.value = !isRegisterOpe.value
                                                },
                                            text = stringResource(
                                                if(isRegisterOpe.value)
                                                    R.string.sign_in
                                                else
                                                    R.string.sign_up
                                            ),
                                            fontSize = 14.sp,
                                            color = md_theme_light_primary
                                        )
                                    }
                                }
                            )
                        } else {
                            UIContainer(
                                subtitle = R.string.authenticate_as_a_customer,
                                content = {
                                    Form()
                                    AuthButton(
                                        authAction = {
                                            execAuth {
                                                // TODO: SAVE IN LOCAL THEN
                                                startActivity(
                                                    Intent(this@AuthActivity,
                                                        MainActivity::class.java)
                                                )
                                            }
                                        },
                                        btnText = R.string.confirm
                                    )
                                }
                            )
                        }
                        clearInputs()
                    }
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color = if (pagerState.currentPage == iteration)
                                gray_background
                            else
                                Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(color)
                                    .size(14.dp)
                            )
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

    @Composable
    private fun UIContainer(
        subtitle: Int,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(md_theme_light_primary)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = 50.dp,
                        start = 30.dp
                    ),
                text = stringResource(R.string.hello),
                fontSize = 45.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 30.dp
                    ),
                text = stringResource(subtitle),
                fontSize = 20.sp,
                color = Color.White
            )
            Card (
                modifier = Modifier
                    .padding(
                        top = 75.dp
                    ),
                shape = if(subtitle == R.string.authenticate_on_a_server) {
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 60.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 60.dp,
                        topEnd = 0.dp
                    )
                },
                colors = CardDefaults.cardColors(
                    containerColor = gray_background
                ),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 30.dp,
                            bottom = 25.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )
            }
        }
    }

    @Composable
    private fun Form() {
        OutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                ),
            singleLine = true,
            value = name.value,
            onValueChange = {
                nameError.value = !isNameValid(it) && name.value.isNotEmpty()
                name.value = it
            },
            label = {
                Text(
                    text = stringResource(R.string.name)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { name.value = "" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = nameError.value
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                ),
            singleLine = true,
            value = surname.value,
            onValueChange = {
                surnameError.value = !isSurnameValid(it) && surname.value.isNotEmpty()
                surname.value = it
            },
            label = {
                Text(
                    text = stringResource(R.string.surname)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { surname.value = "" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = surnameError.value
        )
        EmailPasswordForm()
    }

    @Composable
    private fun EmailPasswordForm() {
        OutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                ),
            singleLine = true,
            value = email.value,
            onValueChange = {
                emailError.value = !isEmailValid(it) && email.value.isNotEmpty()
                email.value = it
            },
            label = {
                Text(
                    text = stringResource(R.string.email)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { email.value = "" }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = emailError.value
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                ),
            singleLine = true,
            value = password.value,
            visualTransformation = if (isPasswordHidden.value)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            onValueChange = {
                passwordError.value = !isPasswordValid(it) && password.value.isNotEmpty()
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
            label = {
                Text(
                    text = stringResource(R.string.password)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordHidden.value = !isPasswordHidden.value }
                ) {
                    Icon(
                        imageVector = if(isPasswordHidden.value)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = passwordError.value
        )
    }

    @Composable
    private fun AuthButton(
        authAction: () -> Unit,
        btnText: Int
    ) {
        Button(
            modifier = Modifier
                .padding(
                    top = 15.dp
                )
                .width(280.dp)
                .height(60.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = authAction
        ) {
            Text(
                text = stringResource(btnText),
                fontSize = 18.sp
            )
        }
    }

    private fun execAuth(
        authAction: () -> Unit
    ) {
        if(isNameValid(name.value)) {
            if(isSurnameValid(surname.value)) {
                if(isEmailValid(email.value)) {
                    if(isPasswordValid(password.value))
                        authAction()
                    else {
                        snackbarLauncher.showSnackError(
                            R.string.wrong_password,
                            passwordError
                        )
                    }
                } else {
                    snackbarLauncher.showSnackError(
                        R.string.wrong_email,
                        emailError
                    )
                }
            } else {
                snackbarLauncher.showSnackError(
                    R.string.wrong_surname,
                    surnameError
                )
            }
        } else {
            snackbarLauncher.showSnackError(
                R.string.name_is_not_valid,
                nameError
            )
        }
    }

}