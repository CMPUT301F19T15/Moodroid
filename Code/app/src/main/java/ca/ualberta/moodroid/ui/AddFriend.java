package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.UserService;

public class AddFriend extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState, UserService userService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }
}
