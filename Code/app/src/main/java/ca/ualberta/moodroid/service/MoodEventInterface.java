package ca.ualberta.moodroid.service;

import com.google.android.gms.tasks.Task;

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
     * @return my events
     */
    public Task<List<MoodEventModel>> getMyEvents();
    
    /**
     * Get a list of all your mood events filtered by a mood
     *
     * @param moodName the mood name
     * @return my events
     */
    public Task<List<MoodEventModel>> getMyEvents(String moodName);

    /**
     * Get a single mood event by internal id
     * @param eventId the internal id
     * @return the single event
     */
    public Task<MoodEventModel> getEventWithId(String eventId);

    /**
     * Create a new mood event for the current user
     *
     * @param moodEvent the mood event
     */
    public Task<MoodEventModel> createEvent(MoodEventModel moodEvent);

    /**
     * Update an existing mood event for the current user
     *
     * @param moodEvent the mood event
     */
    public void updateEvent(MoodEventModel moodEvent);

    /**
     * Delete a mood event for the current user. Be sure this person is allowed to actually delete the mood event.
     *
     * @param moodEvent the mood event
     */
    public void deleteEvent(MoodEventModel moodEvent);
}
