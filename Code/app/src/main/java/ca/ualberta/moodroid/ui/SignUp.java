package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.auth.User;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;
import ca.ualberta.moodroid.service.ValidationService;

public class SignUp extends AppCompatActivity {


    UserService users;
    AuthenticationService auth;
    ValidationService validation;

    public SignUp(UserService userService, AuthenticationService authenticationService, ValidationService validationService) {
        this.users = userService;
        this.auth = authenticationService;
        this.validation = validationService;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
}
