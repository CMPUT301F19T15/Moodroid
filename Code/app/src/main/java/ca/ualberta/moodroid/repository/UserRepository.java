package ca.ualberta.moodroid.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.model.UserModel;

/**
 * While user and authentication are two different things, we cannot store custom data in firebase auth, meaning we need to link that into a collection in the
 * firestore
 */
@Singleton
public class UserRepository extends BaseRepository {


    /**
     * Instantiates a new User repository.
     */
    @Inject
    public UserRepository() {
        super("user", UserModel.class);

    }

}
