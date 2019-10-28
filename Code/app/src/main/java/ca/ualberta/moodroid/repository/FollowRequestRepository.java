package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.FollowRequestModel;

public class FollowRequestRepository extends BaseRepository {

    protected Class modelClass = FollowRequestModel.class;

    public FollowRequestRepository() {

        super("followRequest", FollowRequestModel.class);
    }

}
