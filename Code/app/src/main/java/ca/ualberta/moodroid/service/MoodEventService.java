package ca.ualberta.moodroid.service;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.RepositoryInterface;

public class MoodEventService implements MoodEventInterface {


    private AuthenticationInterface auth;
    private RepositoryInterface events;
    private RepositoryInterface requests;


    public MoodEventService(AuthenticationService auth, MoodEventRepository moodEvents, FollowRequestRepository requests) {
        this.auth = auth;
        this.events = moodEvents;
        this.requests = requests;
    }

    public List<MoodEventModel> getMyEvents() {
        // get the user
        // get all mood events based on the user
        final List<MoodEventModel> returning = new ArrayList<>();

        return returning;

    }

    public List<MoodEventModel> getMyEvents(MoodModel mood) {
        // get the user
        // get the mood name
        // get all mood events based on the user and mood
        final List<MoodEventModel> returning = new ArrayList<>();

        return returning;
    }

    public List<MoodEventModel> getAllFollowingEvents() {

        // get all allowed users to follow
        // get all events that have those users
        final List<MoodEventModel> returning = new ArrayList<>();

        return returning;

    }

    public void createEvent(MoodEventModel moodEvent) {
        this.events.create(moodEvent);
    }

    public void updateEvent(MoodEventModel moodEvent) {
        this.events.update(moodEvent);
    }

    public void deleteEvent(MoodEventModel moodEvent) {
        this.events.delete(moodEvent);
    }


}
