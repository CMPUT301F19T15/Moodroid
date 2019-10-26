package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.BaseModel;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodModel;

public class FollowRequestRepository extends BaseRepository {

    protected Class modelClass = FollowRequestModel.class;

    public FollowRequestRepository() {

        super("mood", MoodModel.class);
    }

}
