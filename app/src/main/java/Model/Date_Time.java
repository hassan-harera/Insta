package Model;

import com.google.firebase.Timestamp;

public class Date_Time {

    public static String timeFromNow(Timestamp timestamp) {
        long seconds = (Timestamp.now().getSeconds() - timestamp.getSeconds());

        if (seconds >= 31557600) {
            long years = (seconds / 31557600);
            return years + " years ago";
        } else if (seconds >= 2592000) {
            long months = (seconds / 2592000);
            return months + " months ago";
        } else if (seconds >= 86400) {
            long days = (seconds / 86400);
            return days + " days ago";
        } else if (seconds >= 3600) {
            long hours = (seconds / 3600);
            return hours + " hours ago";
        } else if (seconds >= 60) {
            long minutes = (seconds / 60);
            return minutes + " minutes ago";
        } else {
            return "seconds ago";
        }
    }
}
