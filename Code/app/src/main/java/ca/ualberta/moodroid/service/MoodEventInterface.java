package ca.ualberta.moodroid.service;

import java.util.List;

import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;

/**
 * We may want to eventually have a filter for geolocation.
 */
public interface MoodEventInterface {

    /**
     * Get a list of all your mood events.
     *
     * @return
     */
    public List<MoodEventModel> getMyEvents();

    /**
     * Get a list of all your mood events filtered by a mood
     *
     * @param mood
     * @return
     */
    public List<MoodEventModel> getMyEvents(MoodModel mood);

    /**
     * Get a list of all the mood events of people you are allowed to follow
     *
     * @return
     */
    public List<MoodEventModel> getAllFollowingEvents();

    /**
     * Create a new mood event for the current user
     *
     * @param moodEvent
     */
    public void createEvent(MoodEventModel moodEvent);

    /**
     * Update an existing mood event for the current user
     *
     * @param moodEvent
     */
    public void updateEvent(MoodEventModel moodEvent);

    /**
     * Delete a mood event for the current user. Be sure this person is allowed to actually delete the mood event.
     *
     * @param moodEvent
     */
    public void deleteEvent(MoodEventModel moodEvent);
}
