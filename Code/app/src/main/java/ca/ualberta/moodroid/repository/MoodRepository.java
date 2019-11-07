package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.MoodModel;

/**
 * Gets all moods
 */
public class MoodRepository extends BaseRepository {


    /**
     * Instantiates a new Mood repository.
     */
    public MoodRepository() {
        super("mood", MoodModel.class);
    }

}
