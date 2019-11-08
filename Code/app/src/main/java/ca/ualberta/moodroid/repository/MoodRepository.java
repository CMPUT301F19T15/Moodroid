package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.MoodModel;

/**
 * This repository stores all of our static moods, Happy, Angry, Sad, Annoyed, Sick, and Scared,
 * along with their unique data. Users use these mood concepts to make mood event which go
 * in that repository.
 */
public class MoodRepository extends BaseRepository {


    /**
     * Instantiates a new Mood repository.
     */
    public MoodRepository() {
        super("mood", MoodModel.class);
    }

}
