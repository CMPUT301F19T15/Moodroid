package ca.ualberta.moodroid.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.repository.UserRepository;

public class MoodService implements MoodInterface {


    private MoodRepository moods;

    @Inject
    public MoodService(MoodRepository moods) {
        this.moods = moods;
    }

    public List<MoodModel> getAllMoods() {
        final List<MoodModel> returning = new ArrayList<>();

        return returning;
    }
}




