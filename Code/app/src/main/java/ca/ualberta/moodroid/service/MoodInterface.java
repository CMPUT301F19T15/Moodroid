package ca.ualberta.moodroid.service;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ca.ualberta.moodroid.model.MoodModel;

/**
 * The interface Mood interface.
 */
public interface MoodInterface {

    /**
     * Get a list of all the moods in the application
     *
     * @return all moods
     */
    public Task<List<MoodModel>> getAllMoods();
}
