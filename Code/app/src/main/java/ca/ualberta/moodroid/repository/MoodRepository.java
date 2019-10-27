package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.MoodModel;

public class MoodRepository extends BaseRepository {


    public MoodRepository() {
        super("mood", MoodModel.class);
    }

}
