package ca.ualberta.moodroid.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.RepositoryInterface;

/**
 * This service allows us to get many different mood event information that's important to us.
 */
public class MoodEventService implements MoodEventInterface {


    private AuthenticationInterface auth;
    private MoodEventRepository events;
    private UserService requests;

    /**
     * Initiate all required services
     */
    public MoodEventService() {
        this.auth = AuthenticationService.getInstance();
        this.events = new MoodEventRepository();
        this.requests = new UserService();
    }

    /**
     * Get all the mood events of a logged in user
     *
     * @return my events
     */
    public Task<List<MoodEventModel>> getMyEvents() {
        return this.getEventsForUser(AuthenticationService.getInstance().getUsername());

    }

    /**
     * Get all the mood events of any user, used for 05.03.01
     *
     * @param username the username
     * @return events for user
     */
    public Task<List<MoodEventModel>> getEventsForUser(String username) {
        return this.events.where("username", username).get().continueWith(new Continuation<List<ModelInterface>, List<MoodEventModel>>() {
            @Override
            public List<MoodEventModel> then(@NonNull Task<List<ModelInterface>> task) throws Exception {
                List<MoodEventModel> results = new ArrayList<MoodEventModel>();
                if (task.isSuccessful()) {
                    Log.d("MOODEVENT", "Task was successful");
                    for (ModelInterface m : task.getResult()) {
                        Log.d("MOODEVENT/GETALL", "Got model: " + m.getInternalId());
                        results.add((MoodEventModel) m);
                    }
                } else {
                    Log.d("MOODEVENT", "Task was not successful: " + task.getException().getMessage());

                }

                return results;
            }
        });

    }

    /**
     * Not Implemented, used to get all a user's events filtered by a single mood.
     *
     * @param mood the mood
     * @return my events
     */
    public List<MoodEventModel> getMyEvents(MoodModel mood) {
        // get the user
        // get the mood name
        // get all mood events based on the user and mood
        final List<MoodEventModel> returning = new ArrayList<>();

        return returning;
    }


    /**
     * Create a mood event via a model.
     *
     * @param moodEvent the mood event
     * @return task
     */
    public Task<MoodEventModel> createEvent(MoodEventModel moodEvent) {
        moodEvent.setUsername(this.auth.getUsername());
        return this.events.create(moodEvent).continueWith(new Continuation<ModelInterface, MoodEventModel>() {
            @Override
            public MoodEventModel then(@NonNull Task<ModelInterface> task) throws Exception {
                if (task.isSuccessful()) {
                    return (MoodEventModel) task.getResult();
                }
                Log.d("MOODEVENT/CREATE", "Not yet successful....");
                return moodEvent;
            }
        });
    }

    /**
     * Not yet implemented, update a mood event after input is given
     *
     * @param moodEvent the mood event
     */
    public void updateEvent(MoodEventModel moodEvent) {
//        this.events.update(moodEvent);
    }

    /**
     * Not yet implemented, delete a mood event
     *
     * @param moodEvent the mood event
     */
    public void deleteEvent(MoodEventModel moodEvent) {
        //this.events.delete(moodEvent);
    }


}
