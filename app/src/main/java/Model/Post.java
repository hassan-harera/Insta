package Model;

import android.graphics.Bitmap;

public class Post {

    private String Title, details;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return id +"";
    }
}
