package ca.ualberta.moodroid.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import javax.inject.Inject;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.repository.UserRepository;

public class AuthenticationService implements AuthenticationInterface {


    private FirebaseAuth auth;
    private UserRepository users;

    public AuthenticationService() {
        this.auth = FirebaseAuth.getInstance();
        this.users = new UserRepository();
    }

    public boolean login() {

        return true;
    }

    public boolean isLoggedIn() {
        FirebaseUser user = this.auth.getCurrentUser();
        return true;
    }

    public boolean isNewUser() {
        return true;
        // TODO: needs to handle no results
//        return this.users.find(this.auth.getCurrentUser().getUid()).continueWith(new Continuation<ModelInterface, boolean>() {
//            @Override
//            public boolean then(@NonNull Task<ModelInterface> task) throws Exception {
//                return true;
//            }
//        });
    }
}


