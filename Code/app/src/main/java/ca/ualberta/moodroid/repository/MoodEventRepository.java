package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.MoodEventModel;

/**
 * Gets all mood event related documents.
 */
public class MoodEventRepository extends BaseRepository {

//    protected Class modelClass = MoodEventModel.class;

    /**
     * Instantiates a new Mood event repository.
     */
    public MoodEventRepository() {
        super("moodEvent", MoodEventModel.class);

    }

}
