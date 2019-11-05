package ca.ualberta.moodroid.model;

import android.location.Location;

import java.util.Date;

public class MoodEventModel extends BaseModel {


    private Date datetime;

    private String moodName;

    private String reasonText;

    private String reasonImageUrl;

    private String situation;

    private Location location;

    private String username;

    public Date getDatetime() {
        return new Date();
    }

    public void setDatetime(Date datetime) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
