package ca.ualberta.moodroid.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;

/**
 * A simple way to get moods throughout the application
 */
@Singleton
public class MoodService implements MoodInterface {

    /**
     * The repository that stores all of the static mood concepts
     */
    private MoodRepository moods;

    /**
     * Initialize the mood repository
     *
     * @param moods the moods
     */
    @Inject
    public MoodService(MoodRepository moods) {

        this.moods = moods;
    }

    /**
     * Get the moods for the entire application, typed properly.
     *
     * @return
     */
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

    /**
     * Get a single mood by mood name.
     *
     * @param moodName
     * @return mood the mood model
     */
    public Task<MoodModel> getMood(String moodName) {
        return this.moods.where("name", moodName).one().continueWith(new Continuation<ModelInterface, MoodModel>() {
            @Override
            public MoodModel then(@NonNull Task<ModelInterface> task) throws Exception {
                MoodModel mood = null;
                if (task.isSuccessful()) {
                    ModelInterface m = task.getResult();
                    mood = (MoodModel) m;
                }
                return mood;
            }
        });
    }
}