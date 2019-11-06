package ca.ualberta.moodroid.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class MoodEventModel extends BaseModel {

    enum Situation {
        ALONE,
        ONE_PERSON,
        TWO_TO_SEVERAL,
        CROWD
    }


    private Date datetime;

    private String moodName;

    private String reasonText;

    private String reasonImageUrl;

    private Situation situation;

    private GeoPoint location;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getMoodName() { return moodName; }

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

    public Situation getSituation() {
        return situation;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public GeoPoint getLocation() { return location; }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
