package ca.ualberta.moodroid.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

@Singleton
public class MoodEventService {


    private AuthenticationInterface auth;
    private RepositoryInterface events;
    private RepositoryInterface requests;

    @Inject
    public MoodEventService() {
        this.auth = AuthenticationService.getInstance();
        this.events = new MoodEventRepository();
    }

    public Task<List<MoodEventModel>> getMyEvents() {
        return this.events.where("username", this.auth.getUsername()).get().continueWith(new Continuation<List<ModelInterface>, List<MoodEventModel>>() {
            @Override
            public List<MoodEventModel> then(@NonNull Task<List<ModelInterface>> task) throws Exception {
                List<MoodEventModel> results = new ArrayList<MoodEventModel>();
                if (task.isSuccessful()) {
                    for (ModelInterface m : task.getResult()) {
                        results.add((MoodEventModel) m);
                    }
                }

                return results;
            }
        });

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

//    public void createEvent(MoodEventModel moodEvent) {
//        this.events.create(moodEvent);
//    }
//
//    public void updateEvent(MoodEventModel moodEvent) {
//        this.events.update(moodEvent);
//    }

    public void deleteEvent(MoodEventModel moodEvent) {
        //this.events.delete(moodEvent);
    }


}
