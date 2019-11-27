package ca.ualberta.moodroid.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.UserRepository;

/**
 * This class implements user operations such as handling username functions as well as
 * dealing with follow requests from other users, and sending your own follow request.
 */
@Singleton
public class UserService implements UserInterface {


private AuthenticationInterface auth;
private FollowRequestRepository requests;
private UserRepository users;

/**
 * Initialize all required services
 */
@Inject
public UserService(AuthenticationService auth, FollowRequestRepository requests, UserRepository users) {
        this.auth = auth;
        this.requests = requests;
        this.users = users;
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
         * Get a follow request by requestee username.
         * @param username
         * @return
         */
        public Task<FollowRequestModel> getFollowRequest(String username){
        return this.requests.where("requesteeUsername", username).where("requesterUsername", this.auth.getUsername()).one().continueWith(new Continuation<ModelInterface, FollowRequestModel>() {
                @Override
                public FollowRequestModel then(@NonNull Task<ModelInterface> task) throws Exception {
                        FollowRequestModel followRequestModel = null;
                        if(task.isSuccessful()){
                                followRequestModel = (FollowRequestModel) task.getResult();
                        }
                        return followRequestModel;

                }
        });


}


/**
 * Get all requests I sent out to see the status
 *
 * @return
 */
public Task<List<FollowRequestModel>> getAllFollowingRequests() {
        Log.d("USERSERVICE/FOLLOWING", "Username=" + this.auth.getUsername());
        return this.requests.where("requesterUsername", this.auth.getUsername()).get().continueWith(new Continuation<List<ModelInterface>, List<FollowRequestModel>>() {
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
 * @param request
 * @return
 */
public Task<FollowRequestModel> createFollowRequest(FollowRequestModel request) {
        return this.requests.create(request).continueWith(new Continuation<ModelInterface, FollowRequestModel>() {
                @Override
                public FollowRequestModel then(@NonNull Task<ModelInterface> task) throws Exception {
                        if (task.isSuccessful()) {
                                return (FollowRequestModel) task.getResult();
                        }
                        Log.d("FOLLOWREQUESTMODEL/CREATE", "Not yet successful...");
                        return request;
                }
        });

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
 * Finds and returns a user by username.
 *
 * @param username
 * @return
 */
public Task<UserModel> getUserByUsername(String username) {
        return this.users.where("username", username).one().continueWith(new Continuation<ModelInterface, UserModel>() {
                @Override
                public UserModel then(@NonNull Task<ModelInterface> task) throws Exception {
                        UserModel user = null;
                        if(task.isSuccessful()){
                                ModelInterface m = task.getResult();
                                user = (UserModel) m;
                        }
                        return user;
                }
        });
}

        }