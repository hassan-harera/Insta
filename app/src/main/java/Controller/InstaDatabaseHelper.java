package Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import Database.Info;
import Model.Post;
import Model.User;

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
        createFeedTable(db);
        createProfileTable(db);
        createUserTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropProfileTable(db);
        dropFeedTable(db);
        dropUserTable(db);
        onCreate(db);
    }

    private void dropFeedTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_FEED_POSTS;
        db.execSQL(query);
    }

    private void dropProfileTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_PROFILE_POSTS;
        db.execSQL(query);
    }

    private void dropUserTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_USER;
        db.execSQL(query);
    }

    public Boolean insertProfilePost(Post post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Info.PROFILE_POST_ID_COLUMN, post.getId());
        contentValues.put(Info.PROFILE_POST_CAPTION_COLUMN, post.getCaption());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        contentValues.put(Info.PROFILE_POST_IMAGE_COLUMN, stream.toByteArray());

        return db.insert(Info.TABLE_PROFILE_POSTS, null, contentValues) != -1;
    }

    public Boolean insertFeedPost(Post post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Info.FEED_POST_ID_COLUMN, post.getId());
        contentValues.put(Info.FEED_POST_CAPTION_COLUMN, post.getCaption());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        contentValues.put(Info.FEED_POST_IMAGE_COLUMN, stream.toByteArray());

        return db.insert(Info.TABLE_FEED_POSTS, null, contentValues) != -1;
    }

    public Boolean insertUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Info.USER_UID_COLUMN, user.getUid());
        contentValues.put(Info.USER_NAME_COLUMN, user.getName());
        contentValues.put(Info.USER_EMAIL_COLUMN, user.getEmail());
        contentValues.put(Info.USER_BIO_COLUMN, user.getBio());

        if (user.getProfilePic() != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            user.getProfilePic().compress(Bitmap.CompressFormat.PNG, 100, stream);
            contentValues.put(Info.USER_IMAGE_COLUMN, stream.toByteArray());
        }
        return db.insert(Info.TABLE_USER, null, contentValues) != -1;
    }

    public void updateUserName(String name, String uid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Info.USER_NAME_COLUMN, name);
        db.update(Info.TABLE_USER, values, Info.USER_UID_COLUMN + " = ? ", new String[]{uid});
    }

    public void updateUserBio(String bio, String uid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Info.USER_BIO_COLUMN, bio);
        db.update(Info.TABLE_USER, values, Info.USER_UID_COLUMN + " = ? ", new String[]{uid});
    }

    public void updateUserProfilePic(byte[] profilePic, String uid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Info.USER_IMAGE_COLUMN, profilePic);
        db.update(Info.TABLE_USER, values, Info.USER_UID_COLUMN + " = ? ", new String[]{uid});
    }



    public User getUser(String uid) {
        User user = new User();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_USER, new String[]{Info.USER_UID_COLUMN,
                        Info.USER_NAME_COLUMN, Info.USER_BIO_COLUMN,
                        Info.USER_EMAIL_COLUMN, Info.USER_IMAGE_COLUMN},
                Info.USER_UID_COLUMN + " = ?", new String[]{uid},
                null, null, null);


        if (cursor != null && cursor.moveToNext()) {
            user.setUid(cursor.getString(cursor.getColumnIndex(Info.USER_UID_COLUMN)));
            user.setBio(cursor.getString(cursor.getColumnIndex(Info.USER_BIO_COLUMN)));
            user.setName(cursor.getString(cursor.getColumnIndex(Info.USER_NAME_COLUMN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(Info.USER_EMAIL_COLUMN)));

            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.USER_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            user.setProfilePic(bitmap);
        }
        return user;
    }

    public User getUserName(String uid) {
        User user = new User();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_USER, new String[]{Info.USER_UID_COLUMN,
                        Info.USER_NAME_COLUMN, Info.USER_BIO_COLUMN,
                        Info.USER_EMAIL_COLUMN, Info.USER_IMAGE_COLUMN},
                "WHERE " + Info.USER_UID_COLUMN + " = ?", new String[]{uid},
                null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            user.setUid(cursor.getString(cursor.getColumnIndex(Info.USER_UID_COLUMN)));
            user.setBio(cursor.getString(cursor.getColumnIndex(Info.USER_BIO_COLUMN)));
            user.setName(cursor.getString(cursor.getColumnIndex(Info.USER_NAME_COLUMN)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(Info.USER_EMAIL_COLUMN)));

            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.USER_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            user.setProfilePic(bitmap);
        }
        return user;
    }

    public List<Post> getFeedPosts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_FEED_POSTS, new String[]{Info.FEED_POST_ID_COLUMN,
                        Info.FEED_POST_CAPTION_COLUMN,
                        Info.FEED_POST_IMAGE_COLUMN}, null, null, null,
                null, null);

        final List<Post> list = new ArrayList();
        while (cursor != null && cursor.moveToNext()) {
            Post p = new Post();
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.FEED_POST_ID_COLUMN)));
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.FEED_POST_CAPTION_COLUMN)));

            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.FEED_POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);

            list.add(p);
        }
        return list;
    }

    public Post getFeedPost(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_FEED_POSTS, new String[]{Info.FEED_POST_ID_COLUMN,
                        Info.FEED_POST_CAPTION_COLUMN,
                        Info.FEED_POST_IMAGE_COLUMN}, Info.FEED_POST_ID_COLUMN + " = ? ", new String[]{id + ""},
                null, null, null);

        Post p = new Post();
        if (cursor != null && cursor.moveToNext()) {
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.FEED_POST_ID_COLUMN)));
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.FEED_POST_CAPTION_COLUMN)));

            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.FEED_POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);
        }
        return p;
    }

    public Post getProfilePost(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_PROFILE_POSTS, new String[]{Info.PROFILE_POST_ID_COLUMN,
                        Info.PROFILE_POST_CAPTION_COLUMN,
                        Info.PROFILE_POST_IMAGE_COLUMN}, Info.PROFILE_POST_ID_COLUMN + " = ? ", new String[]{id + ""},
                null, null, null);

        Post p = new Post();
        if (cursor != null && cursor.moveToNext()) {
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.FEED_POST_ID_COLUMN)));
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.FEED_POST_CAPTION_COLUMN)));

            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.FEED_POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);
        }
        return p;
    }

    public Boolean checkFeedPost(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + Info.TABLE_FEED_POSTS + " where " + Info.FEED_POST_ID_COLUMN + " = ? ;";
        return db.rawQuery(query, new String[]{id + ""}).moveToFirst();
    }

    public Boolean checkProfilePost(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + Info.TABLE_PROFILE_POSTS + " where " + Info.PROFILE_POST_ID_COLUMN + " = ? ;";
        return db.rawQuery(query, new String[]{id + ""}).moveToFirst();
    }

    public void createProfileTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Info.TABLE_PROFILE_POSTS + "("
                + Info.PROFILE_POST_ID_COLUMN + " INT PRIMARY KEY,"
                + Info.PROFILE_POST_CAPTION_COLUMN + " TEXT,"
                + Info.PROFILE_POST_IMAGE_COLUMN + " BITMAP"
                + ");";
        db.execSQL(query);
    }


    public void createFeedTable(SQLiteDatabase db) {
        String query = " CREATE TABLE " + Info.TABLE_FEED_POSTS + " ( " +
                Info.FEED_POST_ID_COLUMN + " INTEGER PRIMARY KEY, " +
                Info.FEED_POST_CAPTION_COLUMN + " TEXT, " +
                Info.FEED_POST_IMAGE_COLUMN + " bitmap" +
                " );";
        db.execSQL(query);
    }

    public void createUserTable(SQLiteDatabase db) {
        String query = " CREATE TABLE " + Info.TABLE_USER + " ( " +
                Info.USER_UID_COLUMN + " TEXT PRIMARY KEY, " +
                Info.USER_NAME_COLUMN + " TEXT, " +
                Info.USER_BIO_COLUMN + " TEXT, " +
                Info.USER_EMAIL_COLUMN + " TEXT, " +
                Info.USER_IMAGE_COLUMN + " bitmap" +
                " );";
        db.execSQL(query);
    }

    public List<Post> getProfilePosts() {
        List<Post> list = new ArrayList();
        String query = "SELECT * FROM " + Info.TABLE_PROFILE_POSTS;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor != null && cursor.moveToNext()) {
            Post p = new Post();
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.PROFILE_POST_ID_COLUMN)));
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.PROFILE_POST_CAPTION_COLUMN)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.PROFILE_POST_IMAGE_COLUMN));
            p.setBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            list.add(p);
        }
        return list;
    }

    public boolean checkUser(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + Info.TABLE_USER + " where " + Info.USER_UID_COLUMN + " = ? ;";
        return db.rawQuery(query, new String[]{uid + ""}).moveToFirst();
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Info.TABLE_FEED_POSTS, null, null);
        db.delete(Info.TABLE_USER, null, null);
        db.delete(Info.TABLE_PROFILE_POSTS, null, null);
    }
}