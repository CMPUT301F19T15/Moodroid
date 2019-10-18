package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.BaseModel;
import ca.ualberta.moodroid.model.FollowRequestModel;

public class FollowRequestRepository extends BaseRepository {

    protected String collectionName = "followRequest";
    protected Class modelClass = FollowRequestModel.class;

}
