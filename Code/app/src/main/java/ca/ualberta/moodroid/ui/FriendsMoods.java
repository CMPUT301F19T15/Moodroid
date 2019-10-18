package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.auth.User;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

public class FriendsMoods extends AppCompatActivity {

    UserService users;
    MoodEventService moodEvents;

    public FriendsMoods(UserService userService, MoodEventService moodEventService) {
        this.users = userService;
        this.moodEvents = moodEventService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_moods);
    }
}
