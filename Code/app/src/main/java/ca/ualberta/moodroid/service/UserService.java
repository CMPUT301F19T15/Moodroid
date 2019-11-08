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

/**
 * This class implements user operations such as handling username functions as well as
 * dealing with follow requests from other users, and sending your own follow request.
 */
public class UserService implements UserInterface {


    private AuthenticationInterface auth;
    private UserRepository users;
    private FollowRequestRepository requests;

    /**
     * Initialize all required services
     */
    public UserService() {
        this.auth = AuthenticationService.getInstance();
        this.users = new UserRepository();
        this.requests = new FollowRequestRepository();
    }


    public void setCurrentUserUsername(String name) {
        // get current user, and set the username
    }

    /**
     * Get all follow requests where they are being requested to be followed
     *
     * @return
     */
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

    /**
     * Return a list of follow requests of the users that the signed in user currently follows
     *
     * @return all users i follow
     */
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

    /**
     * Not implemented
     *
     * @param user
     * @return
     */
    public FollowRequestModel createFollowRequest(UserModel user) {
        return new FollowRequestModel();
    }

    /**
     * Accept a follow request
     *
     * @param request
     * @return
     */
    public Task<Boolean> acceptFollowRequest(FollowRequestModel request) {
        request.setState(FollowRequestModel.ACCEPT_STATE);
        return this.requests.update(request).continueWith(new Continuation<ModelInterface, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<ModelInterface> task) throws Exception {
                return ((FollowRequestModel) task.getResult()).getState().equals(FollowRequestModel.ACCEPT_STATE);
            }
        });
    }

    /**
     * Deny a follow request
     *
     * @param request
     * @return
     */
    public Task<Boolean> denyFollowRequest(FollowRequestModel request) {
        request.setState(FollowRequestModel.DENY_STATE);
        return this.requests.update(request).continueWith(new Continuation<ModelInterface, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<ModelInterface> task) throws Exception {
                return ((FollowRequestModel) task.getResult()).getState().equals(FollowRequestModel.DENY_STATE);
            }
        });
    }

    /**
     * Not implemented
     *
     * @param username
     * @return
     */
    public UserModel getUserByUsername(String username) {
        return new UserModel();
    }


}



