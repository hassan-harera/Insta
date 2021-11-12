package com.harera.base.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object ImageUtils {

    fun getImageFromUri(imageUri: Uri?, context: Context): Bitmap? {
        imageUri?.let {
            return if (Build.VERSION.SDK_INT < 28) {
                MediaStore
                    .Images
                    .Media
                    .getBitmap(context.contentResolver, imageUri)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return null
    }
}
