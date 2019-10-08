package ca.ualberta.moodroid.service;

import java.util.List;

import ca.ualberta.moodroid.model.MoodEventModel;

/**
 *
 */
public interface MoodEventInterface {

    public List<MoodEventModel> getMyMoods();

    public List<MoodEventModel> getAllFollowingMoods();

    public void createNewMoodEvent(MoodEventModel moodEvent);

    public void updateMoodEvent(MoodEventModel moodEvent);

    public void deleteMoodEvent(MoodEventModel moodEvent);
}
