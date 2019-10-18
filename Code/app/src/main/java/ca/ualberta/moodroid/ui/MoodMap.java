package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;

public class MoodMap extends AppCompatActivity {

    MoodEventService moodEvents;
    GeolocationService geolocation;

    public MoodMap(MoodEventService moodEventService, GeolocationService geolocationService) {
        this.moodEvents = moodEventService;
        this.geolocation = geolocationService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mood_map);
    }
}
