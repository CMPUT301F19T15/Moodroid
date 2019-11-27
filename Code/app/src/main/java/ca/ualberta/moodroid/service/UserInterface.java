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
     * Get a follow request by requestee username.
     * @param username
     * @return
     */
    public Task<FollowRequestModel> getFollowRequest(String username);


    /**
     * Create a new follow request for a specified user
     *
     * @param request the user
     * @return the follow request model
     */
    public Task<FollowRequestModel> createFollowRequest(FollowRequestModel request);

    /**
     * Update an existing follow request.
     */
    public Task<FollowRequestModel> updateFollowRequest(FollowRequestModel request);

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


    /**
     * Find User by Id.
     * @param userId
     * @return
     */
    public Task<UserModel> getUserById(String userId);

    /**
     * Create new user.
     * @param user
     * @param userId
     * @return
     */
    public Task<UserModel> createNewUser(UserModel user, String userId);

}
