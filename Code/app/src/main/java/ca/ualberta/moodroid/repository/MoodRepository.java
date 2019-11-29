package ca.ualberta.moodroid.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.MoodModel;

/**
 * This repository stores all of our static moods, Happy, Angry, Sad, Annoyed, Sick, and Scared,
 * along with their unique data. Users use these mood concepts to make mood event which go
 * in that repository.
 */
@Singleton
public class MoodRepository extends BaseRepository {


    /**
     * Instantiates a new Mood repository.
     */
    @Inject
    public MoodRepository() {
        super("mood", MoodModel.class);
    }

}
