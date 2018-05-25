package com.panzhiev.instagram.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private val activity: Activity) {

    val REQUEST_CODE = 1
    var imageUri: Uri? = null
    private val simpleDataFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    fun takeCameraPicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            val imageFile = createImageFile()
            imageUri = FileProvider.getUriForFile(activity,
                    "com.panzhiev.instagram.fileprovider",
                    imageFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${simpleDataFormat.format(Date())}_", /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
    }
}