package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.MoodEventModel;

public class MoodEventRepository extends BaseRepository {

//    protected Class modelClass = MoodEventModel.class;

    public MoodEventRepository() {
        super("moodEvent", MoodEventModel.class);

    }

}
