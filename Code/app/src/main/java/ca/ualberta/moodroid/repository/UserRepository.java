package ca.ualberta.moodroid.repository;

import ca.ualberta.moodroid.model.UserModel;

/**
 * While user and authentication are two different things, we cannot store custom data in firebase auth, meaning we need to link that into a collection in the
 * firestore
 */
public class UserRepository extends BaseRepository {


    public UserRepository() {
        super("user", UserModel.class);

    }

}
