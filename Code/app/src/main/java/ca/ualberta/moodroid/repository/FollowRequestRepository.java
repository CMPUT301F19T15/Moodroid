package ca.ualberta.moodroid.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.FollowRequestModel;

/**
 * Gets all follow request related documents. See BaseRepository for implementation
 */
@Singleton
public class FollowRequestRepository extends BaseRepository {

    /**
     * The Model class.
     */
    protected Class modelClass = FollowRequestModel.class;

    /**
     * Instantiates a new Follow request repository.
     */
    @Inject
    public FollowRequestRepository() {

        super("followRequest", FollowRequestModel.class);
    }

}
