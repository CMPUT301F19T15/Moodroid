package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.GeolocationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;
import ca.ualberta.moodroid.service.ValidationService;

public class AddMood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState, MoodEventService moodEventService, GeolocationService geolocationService, ValidationService validationService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
    }
}
