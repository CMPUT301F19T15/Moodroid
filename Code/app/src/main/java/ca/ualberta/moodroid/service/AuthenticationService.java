package ca.ualberta.moodroid.service;

import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationService implements AuthenticationInterface {


    private FirebaseAuth auth;

    public AuthenticationService(FirebaseAuth auth) {

        this.auth = auth;
    }

    public boolean login() {

        return true;
    }

    public boolean isLoggedIn() {

        return true;
    }

    public boolean isNewUser() {

        return true;
    }
}


