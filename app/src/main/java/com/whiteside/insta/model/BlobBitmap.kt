package com.whiteside.insta.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.facebook.internal.Utility
import com.google.common.io.Resources
import com.google.firebase.firestore.Blob
import com.whiteside.insta.R
import java.io.ByteArrayOutputStream


class BlobBitmap {

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