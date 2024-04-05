package com.tecknobit.nova.helpers.utils

import com.tecknobit.novacore.helpers.Endpoints
import com.tecknobit.novacore.helpers.Requester
import com.tecknobit.novacore.records.User.NAME_KEY
import com.tecknobit.novacore.records.User.PROFILE_PIC_URL_KEY
import com.tecknobit.novacore.records.project.Project.LOGO_URL_KEY
import com.tecknobit.novacore.records.release.events.AssetUploadingEvent.AssetUploaded.ASSETS_UPLOADED_KEY
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Headers.Companion.toHeaders
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * The **AndroidRequester** class is useful to communicate with the Nova's backend
 *
 * @param host: the host where is running the Nova's backend
 * @param userId: the user identifier
 * @param userToken: the user token
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class AndroidRequester(
    host: String,
    userId: String? = null,
    userToken: String? = null
): Requester(host, userId, userToken) {

    init {
        changeHost(host)
        setUserCredentials(userId, userToken)
    }

    /**
     * Function to execute the request to change the profile pic of the user
     *
     * @param profilePic: the profile pic chosen by the user to set as the new profile pic
     *
     * @return the result of the request as [JSONObject]
     */
    override fun changeProfilePic(
        profilePic: File
    ): JSONObject {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                PROFILE_PIC_URL_KEY,
                profilePic.name,
                profilePic.readBytes().toRequestBody("*/*".toMediaType())
            )
            .build()
        return execMultipartRequest(
            endpoint = assembleUsersEndpointPath(Endpoints.CHANGE_PROFILE_PIC_ENDPOINT),
            body = body
        )
    }

    /**
     * Function to execute the request to add a new project
     *
     * @param logoPic: the project logo
     * @param projectName: the name of the project
     *
     * @return the result of the request as [JSONObject]
     */
    override fun addProject(
        logoPic: File,
        projectName: String
    ): JSONObject {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                LOGO_URL_KEY,
                logoPic.name,
                logoPic.readBytes().toRequestBody("*/*".toMediaType())
            )
            .addFormDataPart(
                NAME_KEY,
                projectName
            )
            .build()
        return execMultipartRequest(
            endpoint = assembleProjectsEndpointPath(),
            body = body
        )
    }

    /**
     * Function to execute the request to upload assets to a release
     *
     * @param projectId: the project identifier where the release is attached
     * @param releaseId: the release identifier where upload the assets
     * @param assets: the list of the assets to upload
     *
     * @return the result of the request as [JSONObject]
     */
    override fun uploadAsset(
        projectId: String,
        releaseId: String,
        assets: List<File>
    ): JSONObject {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
        assets.forEach { asset ->
            body.addFormDataPart(
                ASSETS_UPLOADED_KEY,
                asset.name,
                asset.readBytes().toRequestBody("*/*".toMediaType())
            )
        }
        return execMultipartRequest(
            endpoint = assembleReleasesEndpointPath(
                projectId = projectId,
                releaseId = releaseId
            ),
            body = body.build()
        )
    }

    /**
     * Function to exec a multipart body  request
     *
     * @param endpoint: the endpoint path of the url
     * @param body: the body payload of the request
     *
     * @return the result of the request as [JSONObject]
     */
    private fun execMultipartRequest(
        endpoint: String,
        body: MultipartBody
    ): JSONObject {
        val mHeaders = mutableMapOf<String, String>()
        headers.headersKeys.forEach { headerKey ->
            mHeaders[headerKey] = headers.getHeader(headerKey)
        }
        val request: Request = Request.Builder()
            .headers(mHeaders.toHeaders())
            .url("$host$endpoint")
            .post(body)
            .build()
        val client = validateSelfSignedCertificate(OkHttpClient())
        var response: JSONObject? = null
        runBlocking {
            try {
                async {
                    response = try {
                        client.newCall(request).execute().body?.string()?.let { JSONObject(it) }
                    } catch (e: IOException) {
                        JSONObject(connectionErrorMessage(SERVER_NOT_REACHABLE))
                    }
                }.await()
            } catch (e: Exception) {
                response = JSONObject(connectionErrorMessage(SERVER_NOT_REACHABLE))
            }
        }
        return response!!
    }

    /**
     * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
     * No-any params required
     *
     * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to use for test only or
     * in a private distribution on own infrastructure
     */
    private fun validateSelfSignedCertificate(
        okHttpClient: OkHttpClient
    ): OkHttpClient {
        if(mustValidateCertificates) {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
            })
            val builder = okHttpClient.newBuilder()
            try {
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustAllCerts, SecureRandom())
                builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _: String?, _: SSLSession? -> true }
                return builder.build()
            } catch (ignored: java.lang.Exception) {
            }
        }
        return OkHttpClient()
    }

}