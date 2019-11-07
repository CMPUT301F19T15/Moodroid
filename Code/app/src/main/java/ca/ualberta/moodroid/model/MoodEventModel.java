package ca.ualberta.moodroid.model;

import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mood event implementation of a given mood event history item.
 */
public class MoodEventModel extends BaseModel {


    private String datetime;

    private String moodName;

    private String reasonText;

    private String reasonImageUrl;

    private String situation;

    private GeoPoint location;

    private String username;

    /**
     * The constant DATE_FORMAT.
     */
    public static String DATE_FORMAT = "mm/dd/yyyy HH:mm";

    /**
     * Gets datetime.
     *
     * @return the datetime
     */
    public String getDatetime() {
        return this.datetime;
    }

    /**
     * Sets datetime.
     *
     * @param datetime the datetime
     */
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    /**
     * Gets mood name.
     *
     * @return the mood name
     */
    public String getMoodName() {
        return moodName;
    }

    /**
     * Sets mood name.
     *
     * @param moodName the mood name
     */
    public void setMoodName(String moodName) {
        this.moodName = moodName;
    }

    /**
     * Gets reason text.
     *
     * @return the reason text
     */
    public String getReasonText() {
        return reasonText;
    }

    /**
     * Sets reason text.
     *
     * @param reasonText the reason text
     */
    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    /**
     * Gets reason image url.
     *
     * @return the reason image url
     */
    public String getReasonImageUrl() {
        return reasonImageUrl;
    }

    /**
     * Sets reason image url.
     *
     * @param reasonImageUrl the reason image url
     */
    public void setReasonImageUrl(String reasonImageUrl) {
        this.reasonImageUrl = reasonImageUrl;
    }

    /**
     * Gets situation.
     *
     * @return the situation
     */
    public String getSituation() {
        return situation;
    }

    /**
     * Sets situation.
     *
     * @param situation the situation
     */
    public void setSituation(String situation) {
        this.situation = situation;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }


    /**
     * Date object date.
     *
     * @return the date
     * @throws Exception the exception
     */
    public Date dateObject() throws Exception {
        return new SimpleDateFormat(MoodEventModel.DATE_FORMAT).parse(this.getDatetime());
    }
}
