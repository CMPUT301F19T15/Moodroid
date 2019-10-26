package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;

public class MoodEventRepository extends BaseRepository {

    protected Class modelClass = MoodEventModel.class;

    public MoodEventRepository() {
        super("mood", MoodModel.class);

    }

}
