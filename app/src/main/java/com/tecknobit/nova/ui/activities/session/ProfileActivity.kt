package com.tecknobit.nova.ui.activities.session

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tecknobit.nova.R
import com.tecknobit.nova.ui.activities.navigation.MainActivity
import com.tecknobit.nova.ui.activities.navigation.Splashscreen
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.user
import com.tecknobit.nova.ui.theme.NovaTheme
import com.tecknobit.nova.ui.theme.gray_background
import com.tecknobit.nova.ui.theme.md_theme_light_primary
import com.tecknobit.nova.ui.theme.thinFontFamily

class ProfileActivity : ComponentActivity() {

    companion object {

        private const val PASSWORD_HIDDEN = "****";

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaTheme {
                var userPassword by remember {
                    mutableStateOf(PASSWORD_HIDDEN)
                }
                Box {
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(275.dp)
                                .clickable {
                                    // TODO: CHOSE PIC THE MAKE THE REQUEST TO CHANGE IT
                                },
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.profilePicUrl)
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
                                editAction = {
                                    // TODO: CREATE THE ALERT TO MAKE THE REQUEST
                                }
                            )
                            UserInfo(
                                header = R.string.password,
                                info = userPassword,
                                onInfoClick = {
                                    userPassword = if(userPassword == PASSWORD_HIDDEN)
                                        user.password
                                    else
                                        PASSWORD_HIDDEN
                                },
                                editAction = {
                                    // TODO: CREATE THE ALERT TO MAKE THE REQUEST
                                }
                            )
                            UserInfo(
                                header = R.string.language,
                                info = user.language,
                                editAction = {
                                    // TODO: CREATE THE ALERT TO MAKE THE REQUEST
                                },
                                isLast = true
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            ActionButton(
                                action = {
                                    // TODO: MAKE REQUEST THEN
                                    startActivity(Intent(this@ProfileActivity, Splashscreen::class.java))
                                },
                                text = R.string.logout
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            ActionButton(
                                color = com.tecknobit.nova.ui.theme.tagstheme.bug.md_theme_light_primary,
                                action = {
                                    // TODO: MAKE REQUEST THEN
                                    startActivity(Intent(this@ProfileActivity, Splashscreen::class.java))
                                },
                                text = R.string.delete_account
                            )
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
            modifier = if(onInfoClick != null) {
                Modifier.clickable {
                    onInfoClick()
                }
            } else
                Modifier,
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

}