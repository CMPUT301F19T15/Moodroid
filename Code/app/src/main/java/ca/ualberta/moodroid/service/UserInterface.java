package ca.ualberta.moodroid.service;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.UserModel;

/**
 * The interface User interface.
 */
public interface UserInterface {

    /**
     * Set the username of the current user, useful for a one time
     *
     * @param name the name
     */
    public void setCurrentUserUsername(String name);


    /**
     * Get a list of all your pending follow requests
     *
     * @return all follow requests
     */
    public Task<List<FollowRequestModel>> getAllFollowRequests();

    /**
     * Create a new follow request for a specified user
     *
     * @param user the user
     * @return the follow request model
     */
    public FollowRequestModel createFollowRequest(UserModel user);

    /**
     * Accept a follow request from another user
     *
     * @param request the request
     * @return the task
     */
    public Task<Boolean> acceptFollowRequest(FollowRequestModel request);

    /**
     * Deny a follow request from another user
     *
     * @param request the request
     * @return the task
     */
    public Task<Boolean> denyFollowRequest(FollowRequestModel request);

    /**
     * Get a user by his username and return a full UserModel
     *
     * @param username the username
     * @return user by username
     */
    public Task<UserModel> getUserByUsername(String username);

}
