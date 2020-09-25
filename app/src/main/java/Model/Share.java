package Model;

import com.google.firebase.Timestamp;

public class Share {

    String UID, shareCaption;
    Timestamp date;

    public Share() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getShareCaption() {
        return shareCaption;
    }

    public void setShareCaption(String shareCaption) {
        this.shareCaption = shareCaption;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
