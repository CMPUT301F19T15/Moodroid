package ca.ualberta.moodroid.service;

import java.util.List;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.UserModel;

public interface UserInterface {

    public List<FollowRequestModel> getAllPendingFollowRequests();

    public List<UserModel> findUsers();

}
