package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState, AuthenticationService authenticationService, UserService userService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
