package com.whiteside.insta.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import Model.Profile;

public class LoginViewModel extends ViewModel {
    MutableLiveData<Profile> profileGetter = new MutableLiveData<>();
    MutableLiveData<Profile> profileSetter = new MutableLiveData<>();

    void getProfile(String uid) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(Objects.requireNonNull(uid))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        profileGetter.setValue(documentSnapshot.toObject(Profile.class));
//                        assert profile != null;
//                        if(profile.getEmail() == null)
//                            addUserToDatabase();
//                        else
//                            successLogin();
                    }
                });
    }

    void setProfile(Profile profile, String uid) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid)
                .set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        profileSetter.setValue(profile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
