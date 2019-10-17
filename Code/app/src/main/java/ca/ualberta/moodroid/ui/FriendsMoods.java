package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

public class FriendsMoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState, UserService userService, MoodEventService moodEventService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_moods);
    }
}
