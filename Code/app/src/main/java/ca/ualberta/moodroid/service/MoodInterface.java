package ca.ualberta.moodroid.service;

import java.util.List;

import ca.ualberta.moodroid.model.MoodModel;

public interface MoodInterface {

    /**
     * Get a list of all the moods in the application
     *
     * @return
     */
    public List<MoodModel> getAllMoods();
}
