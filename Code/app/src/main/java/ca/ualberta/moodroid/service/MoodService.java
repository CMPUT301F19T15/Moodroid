package ca.ualberta.moodroid.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.repository.UserRepository;

public class MoodService implements MoodInterface {


    private MoodRepository moods;

    @Inject
    public MoodService() {

        this.moods = new MoodRepository();
    }

    public Task<List<MoodModel>> getAllMoods() {

        return this.moods.get().continueWith(new Continuation<List<ModelInterface>, List<MoodModel>>() {
            @Override
            public List<MoodModel> then(@NonNull Task<List<ModelInterface>> task) throws Exception {
                List<MoodModel> data = new ArrayList<>();

                if (task.isSuccessful()) {
                    for (ModelInterface m : task.getResult()) {
                        data.add((MoodModel) m);

                    }
                }


                return data;
            }
        });
    }
}




