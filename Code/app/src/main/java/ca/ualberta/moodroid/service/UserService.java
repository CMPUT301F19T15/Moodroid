package ca.ualberta.moodroid.service;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.UserRepository;

public class UserService implements UserInterface {


    private AuthenticationInterface auth;
    private UserRepository users;
    private FollowRequestRepository requests;

    @Inject
    public UserService(AuthenticationService auth, UserRepository repo, FollowRequestRepository requests) {
        this.auth = auth;
        this.users = repo;
        this.requests = requests;
    }


    public void setCurrentUserUsername(String name) {
        // get current user, and set the username
    }

    public List<FollowRequestModel> getAllPendingFollowRequests() {

        final List<FollowRequestModel> returning = new ArrayList<>();

        return returning;
    }

    public FollowRequestModel createFollowRequest(UserModel user) {
        return new FollowRequestModel();
    }

    public void acceptFollowRequest(FollowRequestModel request) {
        // check to ensure the follow request is for you, then accept it

    }

    public void denyFollowRequest(FollowRequestModel request) {

    }

    public UserModel getUserByUsername(String username) {
        return new UserModel();
    }


}



