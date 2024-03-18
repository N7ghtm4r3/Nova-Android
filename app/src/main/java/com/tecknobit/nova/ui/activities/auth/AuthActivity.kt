package com.tecknobit.nova.ui.activities.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.nova.R
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                var isRegisterOpe by remember { mutableStateOf(true) }
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
                        text = stringResource(R.string.authenticate_on_a_server),
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Card (
                        modifier = Modifier
                            .padding(
                                top = 75.dp
                            ),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 60.dp
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
                                    top = 30.dp,
                                    bottom = 25.dp
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var host by remember { mutableStateOf("") }
                            var hostError by remember { mutableStateOf(false) }
                            var serverSecret by remember { mutableStateOf("") }
                            var serverSecretError by remember { mutableStateOf(false) }
                            var name by remember { mutableStateOf("") }
                            var nameError by remember { mutableStateOf(false) }
                            var surname by remember { mutableStateOf("") }
                            var surnameError by remember { mutableStateOf(false) }
                            var email by remember { mutableStateOf("") }
                            var emailError by remember { mutableStateOf(false) }
                            var password by remember { mutableStateOf("") }
                            var passwordError by remember { mutableStateOf(false) }
                            var isPasswordHidden by remember { mutableStateOf(true) }
                            OutlinedTextField(
                                singleLine = true,
                                value = host,
                                onValueChange = {
                                    hostError = it.isEmpty() && host.isNotEmpty()
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
                                isError = hostError
                            )
                            if(isRegisterOpe) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .padding(
                                            top = 10.dp,
                                        ),
                                    singleLine = true,
                                    value = serverSecret,
                                    onValueChange = {
                                        serverSecretError = it.isEmpty() && serverSecret.isNotEmpty()
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
                                    isError = serverSecretError
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .padding(
                                            top = 10.dp,
                                        ),
                                    singleLine = true,
                                    value = name,
                                    onValueChange = {
                                        nameError = it.isEmpty() && name.isNotEmpty()
                                        name = it
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(R.string.name)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { name = "" }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    isError = nameError
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .padding(
                                            top = 10.dp,
                                        ),
                                    singleLine = true,
                                    value = surname,
                                    onValueChange = {
                                        surnameError = it.isEmpty() && surname.isNotEmpty()
                                        surname = it
                                    },
                                    label = {
                                        Text(
                                            text = stringResource(R.string.surname)
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            onClick = { surname = "" }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    isError = surnameError
                                )
                            }
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                    ),
                                singleLine = true,
                                value = email,
                                onValueChange = {
                                    emailError = it.isEmpty() && email.isNotEmpty()
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
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                isError = emailError
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp,
                                    ),
                                singleLine = true,
                                value = password,
                                visualTransformation = if (isPasswordHidden)
                                    VisualTransformation.None
                                else
                                    PasswordVisualTransformation(),
                                onValueChange = {
                                    passwordError = it.isEmpty() && password.isNotEmpty()
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
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                isError = passwordError
                            )
                            Button(
                                modifier = Modifier
                                    .padding(
                                        top = 15.dp
                                    )
                                    .width(280.dp)
                                    .height(60.dp),
                                shape = RoundedCornerShape(10.dp),
                                onClick = { /*TODO*/ }
                            ) {
                                Text(
                                    text = stringResource(
                                        if(isRegisterOpe)
                                            R.string.sign_up
                                        else
                                            R.string.sign_in
                                    ),
                                    fontSize = 18.sp
                                )
                            }
                            Row (
                                modifier = Modifier
                                    .padding(
                                        top = 10.dp
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        if(isRegisterOpe)
                                            R.string.have_an_account
                                        else
                                            R.string.are_you_new_to_nova
                                    ),
                                    fontSize = 14.sp
                                )
                                Text(
                                    modifier = Modifier
                                        .clickable { isRegisterOpe = !isRegisterOpe},
                                    text = stringResource(
                                        if(isRegisterOpe)
                                            R.string.sign_in
                                        else
                                            R.string.sign_up
                                    ),
                                    fontSize = 14.sp,
                                    color = md_theme_light_primary
                                )
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

}