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
import java.util.Date;
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
        createPostsTable(db);
        createUserTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropPostsTable(db);
        dropUserTable(db);
        onCreate(db);
    }

    private void dropPostsTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_POSTS;
        db.execSQL(query);
    }

    private void dropUserTable(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + Info.TABLE_USER;
        db.execSQL(query);
    }

    public void insertPost(Post post) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Info.POST_CAPTION_COLUMN, post.getCaption());
        contentValues.put(Info.POST_ID_COLUMN, post.getId());
        contentValues.put(Info.POST_USER_ID_COLUMN, post.getUID());
        contentValues.put(Info.POST_LIKED_COLUMN, post.getLiked() ? 1 : 0);
        contentValues.put(Info.POST_DATE_COLUMN, post.getDate().toString());
        contentValues.put(Info.POST_LIKES_COLUMN, post.getLikes());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        contentValues.put(Info.POST_IMAGE_COLUMN, stream.toByteArray());

        db.insert(Info.TABLE_POSTS, null, contentValues);
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

    public List<Post> getUserPosts(String UID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_POSTS, new String[]{
                        Info.POST_ID_COLUMN,
                        Info.POST_USER_ID_COLUMN,
                        Info.POST_CAPTION_COLUMN,
                        Info.POST_IMAGE_COLUMN,
                        Info.POST_LIKED_COLUMN,
                        Info.POST_LIKES_COLUMN,
                        Info.POST_DATE_COLUMN},
                Info.POST_USER_ID_COLUMN + " = ?",
                new String[]{UID}, null,
                null, null);

        final List<Post> list = new ArrayList();
        while (cursor != null && cursor.moveToNext()) {
            Post p = new Post();
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.POST_ID_COLUMN)));
            p.setUID(cursor.getString(cursor.getColumnIndex(Info.POST_USER_ID_COLUMN)));
            p.setDate(cursor.getString(cursor.getColumnIndex(Info.POST_DATE_COLUMN)));
            p.setLikes(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKES_COLUMN)));
            p.setLiked(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKED_COLUMN)) == 1);
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.POST_CAPTION_COLUMN)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);

            list.add(p);
        }
        return list;
    }

    public List<Post> getPosts() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_POSTS, new String[]{
                        Info.POST_CAPTION_COLUMN,
                        Info.POST_DATE_COLUMN,
                        Info.POST_ID_COLUMN,
                        Info.POST_LIKED_COLUMN,
                        Info.POST_USER_ID_COLUMN,
                        Info.POST_LIKES_COLUMN,
                        Info.POST_IMAGE_COLUMN,},
                null, null, null,
                null, null);

        final List<Post> list = new ArrayList();
        while (cursor != null && cursor.moveToNext()) {
            Post p = new Post();
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.POST_ID_COLUMN)));
            p.setUID(cursor.getString(cursor.getColumnIndex(Info.POST_USER_ID_COLUMN)));
            p.setDate(cursor.getString(cursor.getColumnIndex(Info.POST_DATE_COLUMN)));
            p.setLikes(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKES_COLUMN)));
            p.setLiked(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKED_COLUMN)) == 1);
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.POST_CAPTION_COLUMN)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);
            list.add(p);
        }
        return list;
    }

    public Post getPost(String UID, int ID) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Info.TABLE_POSTS, new String[]{Info.POST_CAPTION_COLUMN,
                        Info.POST_DATE_COLUMN,
                        Info.POST_ID_COLUMN,
                        Info.POST_LIKES_COLUMN,
                        Info.POST_LIKED_COLUMN,
                        Info.POST_USER_ID_COLUMN,
                        Info.POST_IMAGE_COLUMN,},
                Info.POST_USER_ID_COLUMN + " = ? and " + Info.POST_ID_COLUMN + " = ?",
                new String[]{UID, ID + ""}, null,
                null, null);

        final Post p = new Post();
        if (cursor != null && cursor.moveToNext()) {
            p.setId(cursor.getInt(cursor.getColumnIndex(Info.POST_ID_COLUMN)));
            p.setUID(cursor.getString(cursor.getColumnIndex(Info.POST_USER_ID_COLUMN)));
            p.setDate(cursor.getString(cursor.getColumnIndex(Info.POST_DATE_COLUMN)));
            p.setLikes(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKES_COLUMN)));
            p.setLiked(cursor.getInt(cursor.getColumnIndex(Info.POST_LIKED_COLUMN)) == 1);
            p.setCaption(cursor.getString(cursor.getColumnIndex(Info.POST_CAPTION_COLUMN)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Info.POST_IMAGE_COLUMN));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            p.setBitmap(bitmap);
        }
        return p;
    }

    public Boolean checkPost(String UID, int ID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Info.TABLE_POSTS + " WHERE " + Info.POST_USER_ID_COLUMN + " = ? AND " + Info.POST_ID_COLUMN+ " = ?";
        return db.rawQuery(query, new String[]{UID, ID+""}).moveToFirst();
    }

    public Boolean checkUser(String UID) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + Info.TABLE_USER + " WHERE " + Info.USER_UID_COLUMN + " = ? ;";
        return db.rawQuery(query, new String[]{UID}).moveToFirst();
    }

    public void createPostsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE " + Info.TABLE_POSTS + "("
                + Info.POST_ID_COLUMN + " INT NOT NULL,"
                + Info.POST_USER_ID_COLUMN + " TEXT NOT NULL,"
                + Info.POST_LIKED_COLUMN + " INT NOT NULL,"
                + Info.POST_CAPTION_COLUMN + " TEXT NOT NULL,"
                + Info.POST_LIKES_COLUMN + " INTEGER NOT NULL,"
                + Info.POST_DATE_COLUMN + " TEXT NOT NULL,"
                + Info.POST_IMAGE_COLUMN + " BITMAP NOT NULL"
                + ");";
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

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Info.TABLE_POSTS, null, null);
        db.delete(Info.TABLE_USER, null, null);
    }

    public void updatePost(Post p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Info.POST_LIKED_COLUMN, p.getLiked());
        values.put(Info.POST_CAPTION_COLUMN, p.getCaption());
        values.put(Info.POST_DATE_COLUMN, p.getDate());
        values.put(Info.POST_LIKES_COLUMN, p.getLikes());
        db.update(Info.TABLE_POSTS, values, Info.POST_ID_COLUMN + " = ? AND " + Info.POST_USER_ID_COLUMN + " = ?",
                new String[]{p.getId()+"", p.getUID()});
    }
}