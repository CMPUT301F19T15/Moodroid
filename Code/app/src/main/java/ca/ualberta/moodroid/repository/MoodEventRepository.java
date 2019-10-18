package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;

public class MoodEventRepository extends BaseRepository {

    protected String collectionName = "moodEvent";
    protected Class modelClass = MoodEventModel.class;

}
