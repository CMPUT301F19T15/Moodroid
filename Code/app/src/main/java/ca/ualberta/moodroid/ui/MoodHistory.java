package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

public class MoodHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState, MoodEventService moodEventService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
    }
}
