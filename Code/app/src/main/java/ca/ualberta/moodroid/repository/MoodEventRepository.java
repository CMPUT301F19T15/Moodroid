package ca.ualberta.moodroid.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.MoodEventModel;

/**
 * This is the repository that stores all mood events created by a user. The user adds a mood
 * event from their app and all of its related data is stored in firebase in this repo.
 */
@Singleton
public class MoodEventRepository extends BaseRepository {

//    protected Class modelClass = MoodEventModel.class;

    /**
     * Instantiates a new Mood event in this repository
     */
    @Inject
    public MoodEventRepository() {
        super("moodEvent", MoodEventModel.class);

    }

}
