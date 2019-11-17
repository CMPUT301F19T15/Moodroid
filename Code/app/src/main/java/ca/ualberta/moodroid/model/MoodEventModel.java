package ca.ualberta.moodroid.model;

import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mood event implementation of a user added mood event. Includes all required
 * attributes of a mood event as per requested.
 */
public class MoodEventModel extends BaseModel {


    /**
     * datetime
     * A string value that represents the time of the users mood
     */
    private String datetime;

    /**
     * moodName
     * A string value that holds the mood name, which should be any of the following: Happy, Angry,
     * Scared, Sad, Annoyed, or Sick.
     */
    private String moodName;

    /**
     * reasonText
     * A string value that contains a user inputted reason for this current mood event
     */
    private String reasonText;

    /**
     * reasonImageUrl
     * String data containing the url for any image the user would like to associate with a
     * mood event
     */
    private String reasonImageUrl;

    /**
     * situation
     * <p>
     * String data containing the current situation of a mood such as "Alone" or "in a crowd"
     */

    private String situation;

    /**
     * location
     * <p>
     * map data for pinpointing a mood events location, which will be integrated into the
     * map feature of the app
     */

    private GeoPoint location;

    /**
     * username
     * <p>
     * String value for the username of the person making the mood event.
     */

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
        Date date = new SimpleDateFormat(MoodEventModel.DATE_FORMAT).parse(this.getDatetime());
        return date;
    }
}