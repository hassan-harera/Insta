package com.whiteside.insta;

import static org.junit.Assert.assertTrue;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkUserAddedToCloudFirestore() {
        assertTrue(FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("")
                .child("")
                .child("")
                .push()
                .setValue("dwdwdw")
                .isSuccessful());
    }
}