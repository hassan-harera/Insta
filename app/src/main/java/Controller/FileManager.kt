package Controller

import android.content.ContentValues
import android.net.Uri
import androidx.core.content.FileProvider

class FileManager : FileProvider() {
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return super.insert(uri, values)
    }
}