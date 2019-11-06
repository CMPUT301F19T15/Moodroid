package ca.ualberta.moodroid.model;

import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MoodEventModel extends BaseModel {


    private String datetime;

    private String moodName;

    private String reasonText;

    private String reasonImageUrl;

    private String situation;

    private GeoPoint location;

    private String username;

    public static String DATE_FORMAT = "mm/dd/yyyy HH:mm";

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getMoodName() {
        return moodName;
    }

    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public String getReasonImageUrl() {
        return reasonImageUrl;
    }

    public void setReasonImageUrl(String reasonImageUrl) {
        this.reasonImageUrl = reasonImageUrl;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }


    public Date dateObject() throws Exception {
        return new SimpleDateFormat(MoodEventModel.DATE_FORMAT).parse(this.getDatetime());
    }
}
