package com.tecknobit.nova.helpers.utils.download

import android.app.DownloadManager
import android.content.Context
import android.os.Environment.DIRECTORY_DOWNLOADS
import androidx.core.net.toUri

class AssetDownloader (
    context: Context
) {

    companion object {

        const val NOVA_ASSETS_PATH = "Nova/"

    }

    private val downloader = context.getSystemService(DownloadManager::class.java)

    var lastDownloadToWait = -1L

    fun downloadAsset(url: String) {
        val assetName = url.split("/").last()
        val request = DownloadManager.Request(url.toUri())
            .setTitle(assetName)
            //.addRequestHeader("", "")
            .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, "$NOVA_ASSETS_PATH$assetName")
        lastDownloadToWait = downloader.enqueue(request)
    }

}