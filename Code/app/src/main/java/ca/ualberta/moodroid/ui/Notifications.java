package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.UserService;

public class Notifications extends AppCompatActivity {

    UserService users;


    public Notifications(UserService userService) {
        this.users = userService;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        // all where state = undecided and requestee = me
    }
}
