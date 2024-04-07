package com.tecknobit.nova.ui.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tecknobit.nova.ui.activities.navigation.Splashscreen.Companion.activeLocalSession
import com.tecknobit.novacore.records.User
import com.tecknobit.novacore.records.project.Project
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.min


/**
 * Function to display an image as logo
 *
 * @param size: the size of the logo, default value 60.[dp]
 * @param url: the url of the image to display
 */
@Composable
fun Logo(
    size: Dp = 60.dp,
    url: String
) {
    AsyncImage(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .size(size),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(500)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

/**
 * Function to display an UI element when a list of values is empty
 *
 * @param icon: the icon to display
 * @param description: the description to display
 */
@Composable
fun EmptyList(
    icon: ImageVector,
    description: Int
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(125.dp),
            imageVector = icon,
            contentDescription = null,
            tint = Color(200, 203, 210)
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 10.dp
                ),
            text = stringResource(description),
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}


/**
 * Function to get the complete file path of an file
 *
 * @param context: the context where the file is needed
 * @param uri: the uri of the file
 * @return the path of the file
 */
fun getFilePath(
    context: Context,
    uri: Uri
): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex =  returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        val bufferSize = min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream?.close()
        outputStream.close()
    } catch (_: Exception) {
    } finally {
        returnCursor.close()
    }
    return file.path
}

/**
 * Function to assemble the project logo url complete (with the current [activeLocalSession].hostAddress)
 * to display
 *
 * @param project: the project from get the logo url path
 *
 * @return the project logo complete url to display as [String]
 *
 */
fun getProjectLogoUrl(
    project: Project
): String {
    return activeLocalSession.hostAddress + "/" + project.logoUrl
}

/**
 * Function to assemble the profile pic url complete (with the current [activeLocalSession].hostAddress)
 * to display
 *
 * @param member: the member from get the profile pic url path
 *
 * @return the profile pic complete url to display as [String]
 *
 */
fun getMemberProfilePicUrl(
    member: User
): String {
    return activeLocalSession.hostAddress + "/" + member.profilePicUrl
}

/**
 * Function to assemble the report from get the complete url path (with the current [activeLocalSession].hostAddress)
 * to display
 *
 * @param reportUrl: the report from get the complete url path
 *
 * @return the report from get the complete url path to display as [String]
 *
 */
fun getReportUrl(
    reportUrl: String
): String {
    return activeLocalSession.hostAddress + "/" + reportUrl
}

/**
 * Function to assemble the asset url path (with the current [activeLocalSession].hostAddress)
 * to display
 *
 * @param asset: the asset from get the complete url path
 *
 * @return the asset from get the complete url path to display as [String]
 *
 */
fun getAssetUrl(
    asset: String
): String {
    return activeLocalSession.hostAddress + "/" + asset
}