package com.harera.base.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.Blob
import java.io.ByteArrayOutputStream


class ImageUtils {

    companion object {
        fun convertBlobToBitmap(blob: Blob?): Bitmap? {
            if (blob != null) {
                return BitmapFactory.decodeByteArray(blob.toBytes(), 0, blob.toBytes().size)
            }
//            else {
//                return BitmapFactory.decodeResource(ResourcesCompat.getDrawable(resource), R.drawable.loading)
//            }
            return null
        }

        fun convertBitmapToBlob(bitmap: Bitmap): Blob {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return Blob.fromBytes(stream.toByteArray())
        }
    }
}