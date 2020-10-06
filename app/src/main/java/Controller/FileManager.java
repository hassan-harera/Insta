package Controller;

import android.content.ContentValues;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class FileManager extends FileProvider {

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return super.insert(uri, values);
    }
}
