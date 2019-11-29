package ca.ualberta.moodroid.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;

import java.util.List;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.UserModel;

/**
 * The interface User interface.
 * Implemented in UserService
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
     * Get a document reference for pending follow requests.
     *
     * @param state the state
     * @return follow requests reference
     */
    public Query getFollowRequestsReference(String state);

    /**
     * Get a follow request by requestee username.
     *
     * @param username the username
     * @return follow request
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
     *
     * @param request the request
     * @return the task
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
     *
     * @param userId the user id
     * @return user by id
     */
    public Task<UserModel> getUserById(String userId);

    /**
     * Create new user.
     *
     * @param user   the user
     * @param userId the user id
     * @return task
     */
    public Task<UserModel> createNewUser(UserModel user, String userId);

}
