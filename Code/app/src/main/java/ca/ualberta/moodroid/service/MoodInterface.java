package ca.ualberta.moodroid.service;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ca.ualberta.moodroid.model.MoodModel;

public interface MoodInterface {

    /**
     * Get a list of all the moods in the application
     *
     * @return
     */
    public Task<List<MoodModel>> getAllMoods();
}
