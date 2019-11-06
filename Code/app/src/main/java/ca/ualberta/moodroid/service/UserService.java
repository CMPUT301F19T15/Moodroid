package ca.ualberta.moodroid.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
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
    public UserService() {
        this.auth = AuthenticationService.getInstance();
        this.users = new UserRepository();
        this.requests = new FollowRequestRepository();
    }


    public void setCurrentUserUsername(String name) {
        // get current user, and set the username
    }

    public Task<List<FollowRequestModel>> getAllFollowRequests() {

        return this.requests.where("requesteeUsername", this.auth.getUsername()).get().continueWith(new Continuation<List<ModelInterface>, List<FollowRequestModel>>() {
            @Override
            public List<FollowRequestModel> then(@NonNull Task<List<ModelInterface>> task) throws Exception {

                List<FollowRequestModel> data = new ArrayList<FollowRequestModel>();
                if (task.isSuccessful()) {
                    for (ModelInterface m : task.getResult()) {
                        data.add((FollowRequestModel) m);
                    }
                }

                return data;
            }
        });
    }

    public Task<List<FollowRequestModel>> getAllUsersIFollow() {
        return this.requests.where("requesterUsername", this.auth.getUsername()).where("state", FollowRequestModel.ACCEPT_STATE).get().continueWith(new Continuation<List<ModelInterface>, List<FollowRequestModel>>() {
            @Override
            public List<FollowRequestModel> then(@NonNull Task<List<ModelInterface>> task) throws Exception {
                List<FollowRequestModel> data = new ArrayList<FollowRequestModel>();
                if (task.isSuccessful()) {
                    for (ModelInterface m : task.getResult()) {
                        data.add((FollowRequestModel) m);
                    }
                }

                return data;
            }
        });
    }

    public FollowRequestModel createFollowRequest(UserModel user) {
        return new FollowRequestModel();
    }

    public Task<Boolean> acceptFollowRequest(FollowRequestModel request) {
        return this.requests.update(request).continueWith(new Continuation<ModelInterface, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<ModelInterface> task) throws Exception {
                return ((FollowRequestModel) task.getResult()).getState().equals(FollowRequestModel.ACCEPT_STATE);
            }
        });
    }

    public Task<Boolean> denyFollowRequest(FollowRequestModel request) {
        return this.requests.update(request).continueWith(new Continuation<ModelInterface, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<ModelInterface> task) throws Exception {
                return ((FollowRequestModel) task.getResult()).getState().equals(FollowRequestModel.DENY_STATE);
            }
        });
    }

    public UserModel getUserByUsername(String username) {
        return new UserModel();
    }


}



