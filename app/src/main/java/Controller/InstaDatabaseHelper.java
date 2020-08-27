package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Database.Info;
import Model.Post;

public class InstaDatabaseHelper extends SQLiteOpenHelper {

    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    public InstaDatabaseHelper(Context context) {
        super(context, Info.DATABASE_NAME, null, Info.VERSION);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + Info.TABLE_USER_IMAGES + " ( " +
                Info.ID_COLUMN + " INTEGER PRIMARY KEY, " +
                Info.NAME_COLUMN + " TEXT, " +
                Info.DETAILS_COLUMN + " TEXT" +
                " );";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_USER_IMAGES;
        db.execSQL(query);
        onCreate(db);
    }

    public Boolean insertPost(Post post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Info.ID_COLUMN, post.getId());
        contentValues.put(Info.NAME_COLUMN, post.getTitle());
        contentValues.put(Info.DETAILS_COLUMN, post.getDetails());

       return db.insert(Info.TABLE_USER_IMAGES, null, contentValues) != -1;
    }

    public List<Post> getAllPosts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_USER_IMAGES, new String[]{Info.ID_COLUMN, Info.NAME_COLUMN, Info.DETAILS_COLUMN},
                null, null, null, null, null);

        final List<Post> list = new ArrayList();
        while (cursor != null && cursor.moveToNext()) {
            Post p = new Post();
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.ID_COLUMN)));
            p.setTitle(cursor.getString(cursor.getColumnIndex(Info.NAME_COLUMN)));
            p.setDetails(cursor.getString(cursor.getColumnIndex(Info.DETAILS_COLUMN)));
            list.add(p);
        }
        return list;
    }

    public Post getPosot(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_USER_IMAGES, new String[]{Info.ID_COLUMN, Info.NAME_COLUMN, Info.DETAILS_COLUMN},
                Info.ID_COLUMN + " = ? ", new String[]{id + ""}, null, null, null);

        Post p = new Post();
        if (cursor != null && cursor.moveToNext()) {
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.ID_COLUMN)));
            p.setTitle(cursor.getString(cursor.getColumnIndex(Info.NAME_COLUMN)));
            p.setDetails(cursor.getString(cursor.getColumnIndex(Info.DETAILS_COLUMN)));
        }
        return p;
    }

}
