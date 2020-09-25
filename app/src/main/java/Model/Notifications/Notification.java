package Model.Notifications;

import com.google.firebase.Timestamp;

public class Notification implements Comparable<Notification>{
    private Timestamp Date;
    private int type;

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Notification o) {
        return o.getDate().compareTo(getDate());
    }
}
