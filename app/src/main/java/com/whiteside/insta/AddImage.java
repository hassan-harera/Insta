package com.whiteside.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import Controller.Image;
import Model.Post;

public class AddImage extends Fragment {

    public static final int ADD_IMAGE_REQUEST = 1025;
    public static final int TAKE_PICTURE = 2015;
    public static final int PERMISSION_REQUEST_CODE = 123;

    ImageView image;
    EditText caption;
    Button add;

    FirebaseAuth auth;
    Uri uri;

    private ProgressBar progressBar;
    private View view;
    private Bitmap reducedBitmap;


    public AddImage() {
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_image, container, false);

        image = view.findViewById(R.id.image_add);
        caption = view.findViewById(R.id.caption);
        add = view.findViewById(R.id.add_add_image);
        progressBar = view.findViewById(R.id.progress_bar);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageClicked();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClicked(v);
            }
        });

        return view;
    }

    private void verifyPermission() {
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PICTURE);
        } else {
            getActivity().requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            verifyPermission();
        }
    }

    public void addImageClicked() {
        image.setEnabled(false);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Select photo way")
                .setPositiveButton("Take a picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verifyPermission();
                    }
                })
                .setNegativeButton("Select a picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, ADD_IMAGE_REQUEST);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        image.setEnabled(true);
                    }
                })
                .show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        add.setEnabled(true);
        caption.setEnabled(true);
        image.setEnabled(true);

        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == ADD_IMAGE_REQUEST && resultCode == -1) {
            uri = data.getData();
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    reducedBitmap = Image.getReducedBitmap(bitmap, 512);
                    Log.d("Image Uploaded", String.valueOf(reducedBitmap.getByteCount()));
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (data != null && requestCode == TAKE_PICTURE && resultCode == -1) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            reducedBitmap = bitmap;
            image.setImageBitmap(bitmap);
        }
    }

    public void addClicked(View view) {
        if (reducedBitmap != null) {
            add.setEnabled(false);
            caption.setEnabled(false);
            image.setEnabled(false);
        } else {
            Toast.makeText(view.getContext(), "Add an image firstly", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        Post post = new Post();
        post.setCaption(caption.getText().toString());
        post.setTime(Timestamp.now());
        post.setLikes(new HashMap());
        post.setComments(new HashMap());
        post.setShares(new HashMap());
        post.setLiked(false);
        post.setUID(auth.getUid());
        post.setID(String.valueOf(Timestamp.now().getSeconds()));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        reducedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        post.setPostImage(Blob.fromBytes(byteArray));

        Log.d("byteArray", byteArray.length + "");

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(Objects.requireNonNull(auth.getUid()))
                .collection("Posts")
                .document(post.getID()).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "The image added Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Failed to upload the image", Toast.LENGTH_LONG).show();
                }

                caption.setText("");
                image.setImageResource(R.drawable.add_image);
                image.setEnabled(true);
                add.setEnabled(true);
                reducedBitmap = null;
                caption.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}