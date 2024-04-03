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
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.text.intl.Locale.Companion.current
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.helpers.utils.ui.SnackbarLauncher
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.localSessionsHelper
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.requester
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.novacore.InputValidator.LANGUAGES_SUPPORTED
import com.tecknobit.novacore.InputValidator.isEmailValid
import com.tecknobit.novacore.InputValidator.isHostValid
import com.tecknobit.novacore.InputValidator.isNameValid
import com.tecknobit.novacore.InputValidator.isPasswordValid
import com.tecknobit.novacore.InputValidator.isServerSecretValid
import com.tecknobit.novacore.InputValidator.isSurnameValid
import com.tecknobit.novacore.helpers.LocalSessionUtils.NovaSession.LOGGED_AS_CUSTOMER_RECORD_VALUE
import com.tecknobit.novacore.helpers.Requester
import com.tecknobit.novacore.helpers.Requester.Companion.RESPONSE_MESSAGE_KEY
import com.tecknobit.novacore.records.NovaItem.IDENTIFIER_KEY
import com.tecknobit.novacore.records.User.PROFILE_PIC_URL_KEY
import com.tecknobit.novacore.records.User.ROLE_KEY
import com.tecknobit.novacore.records.User.Role
import com.tecknobit.novacore.records.User.Role.Vendor
import com.tecknobit.novacore.records.User.TOKEN_KEY
import java.util.UUID

/**
 * The {@code AuthActivity} activity is used to execute the auth operations sign up and sign in taking
 * the data from the related forms
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see ComponentActivity
 */
class AuthActivity : ComponentActivity() {

    /**
     * **snackbarLauncher** -> the launcher used to display the [Snackbar]
     */
    private lateinit var snackbarLauncher: SnackbarLauncher

    /**
     * **name** -> the state used to store the name of the user
     */
    private lateinit var name: MutableState<String>

    /**
     * **nameError** -> whether the name inserted is not valid
     */
    private lateinit var nameError: MutableState<Boolean>

    /**
     * **surname** -> the state used to store the surname of the user
     */
    private lateinit var surname: MutableState<String>

    /**
     * **surnameError** -> whether the surname inserted is not valid
     */
    private lateinit var surnameError: MutableState<Boolean>

    /**
     * **email** -> the state used to store the email of the user
     */
    private lateinit var email: MutableState<String>

    /**
     * **emailError** -> whether the email inserted is not valid
     */
    private lateinit var emailError: MutableState<Boolean>

    /**
     * **password** -> the state used to store the password of the user
     */
    private lateinit var password: MutableState<String>

    /**
     * **passwordError** -> whether the password inserted is not valid
     */
    private lateinit var passwordError: MutableState<Boolean>

    /**
     * **isPasswordHidden** -> whether the password is not displayed but hide -> ****
     */
    private lateinit var isPasswordHidden: MutableState<Boolean>

    /**
     * **isRegisterOpe** -> whether the current auth operation is the sign-up of the user
     */
    private lateinit var isRegisterOpe: MutableState<Boolean>

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
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var language = LANGUAGES_SUPPORTED[current.toLanguageTag().substringBefore("-")]
        if(language == null)
            language = "en"
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
                                                            email.value = email.value.lowercase()
                                                            requester = Requester(
                                                                host = host
                                                            )
                                                            requester.sendRequest(
                                                                request = {
                                                                    requester.signUp(
                                                                        serverSecret = serverSecret,
                                                                        name = name.value,
                                                                        surname = surname.value,
                                                                        email = email.value,
                                                                        password = password.value,
                                                                        language = language
                                                                    )
                                                                },
                                                                onSuccess = { response ->
                                                                    localSessionsHelper.insertSession(
                                                                        response.getString(IDENTIFIER_KEY),
                                                                        response.getString(TOKEN_KEY),
                                                                        "$host/${response.getString(PROFILE_PIC_URL_KEY)}",
                                                                        email.value,
                                                                        password.value,
                                                                        host,
                                                                        Vendor
                                                                    )
                                                                    activeLocalSession = localSessionsHelper.activeSession
                                                                    startActivity(
                                                                        Intent(this@AuthActivity,
                                                                            MainActivity::class.java)
                                                                    )
                                                                },
                                                                onFailure = { response ->
                                                                    snackbarLauncher.showSnack(
                                                                        message = response.getString(RESPONSE_MESSAGE_KEY)
                                                                    )
                                                                }
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
                                                            email.value = email.value.lowercase()
                                                            requester = Requester(
                                                                host = host
                                                            )
                                                            requester.sendRequest(
                                                                request = {
                                                                    requester.signIn(
                                                                        email = email.value,
                                                                        password = password.value
                                                                    )
                                                                },
                                                                onSuccess = { response ->
                                                                    localSessionsHelper.insertSession(
                                                                        response.getString(IDENTIFIER_KEY),
                                                                        response.getString(TOKEN_KEY),
                                                                        "$host/${response.getString(PROFILE_PIC_URL_KEY)}",
                                                                        email.value,
                                                                        password.value,
                                                                        host,
                                                                        Role.valueOf(response.getString(ROLE_KEY))
                                                                    )
                                                                    activeLocalSession = localSessionsHelper.activeSession
                                                                    startActivity(
                                                                        Intent(this@AuthActivity,
                                                                            MainActivity::class.java)
                                                                    )
                                                                },
                                                                onFailure = { response ->
                                                                    snackbarLauncher.showSnack(
                                                                        message = response.getString(RESPONSE_MESSAGE_KEY)
                                                                    )
                                                                }
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
                                                localSessionsHelper.insertSession(
                                                    UUID.randomUUID().toString().replace("-", ""),
                                                    LOGGED_AS_CUSTOMER_RECORD_VALUE,
                                                    LOGGED_AS_CUSTOMER_RECORD_VALUE,
                                                    email.value,
                                                    password.value,
                                                    LOGGED_AS_CUSTOMER_RECORD_VALUE,
                                                    Role.Customer
                                                )
                                                activeLocalSession = localSessionsHelper.activeSession
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

    /**
     * Function to display the auth form requested by the current operation
     *
     * @param subtitle: the supporting text of the title
     * @param content: the content to display
     */
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

    /**
     * Function to create and display a total auth form
     *
     * No-any params required
     */
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

    /**
     * Function to create and display an auth form with the email and password fields
     *
     * No-any params required
     */
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

    /**
     * Function to display the button to execute the auth operation
     *
     * @param authAction: the action to execute when the button is clicked
     * @param btnText: the text displayed on the button
     */
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

    /**
     * Function to execute the auth action requested
     *
     * @param authAction: the auth action to execute
     */
    private fun execAuth(
        authAction: () -> Unit
    ) {
        if(isNameValid(name.value)) {
            if(isSurnameValid(surname.value)) {
                if(isEmailValid(email.value)) {
                    if(isPasswordValid(password.value)) {
                        authAction()
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