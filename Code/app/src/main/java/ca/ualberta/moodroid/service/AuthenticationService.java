package ca.ualberta.moodroid.service;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ca.ualberta.moodroid.repository.UserRepository;

public class AuthenticationService implements AuthenticationInterface {


    private FirebaseAuth auth;
    private UserRepository users;

    @Inject
    public AuthenticationService(UserRepository users) {
        this.auth = FirebaseAuth.getInstance();
        this.users = users;
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


