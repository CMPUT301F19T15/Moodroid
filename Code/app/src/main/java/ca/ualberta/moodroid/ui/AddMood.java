package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;
import ca.ualberta.moodroid.service.ValidationService;

public class AddMood extends AppCompatActivity {

    MoodEventService moodEvents;
    GeolocationService geolocation;
    ValidationService validation;

    public AddMood(MoodEventService moodEventService, GeolocationService geolocationService, ValidationService validationService) {
        this.moodEvents = moodEventService;
        this.geolocation = geolocationService;
        this.validation = validationService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
    }
}
