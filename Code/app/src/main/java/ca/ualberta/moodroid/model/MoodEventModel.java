package ca.ualberta.moodroid.model;

import android.location.Location;

import java.sql.Time;
import java.util.Date;

public class MoodEventModel {

    enum Situation {
        ALONE,
        ONE_PERSON,
        TWO_TO_SEVERAL,
        CROUD
    }


    private Date datetime;

    private String moodName;

    private String reasonText;

    private String reasonImageUrl;

    private Situation situation;

    private Location location;


}
