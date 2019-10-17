package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;
import ca.ualberta.moodroid.service.ValidationService;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState, UserService userService, AuthenticationService authenticationService, ValidationService validationService) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
}
