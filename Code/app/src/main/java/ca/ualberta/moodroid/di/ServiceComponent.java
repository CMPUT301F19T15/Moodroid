package ca.ualberta.moodroid.di;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Singleton;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.ui.AddFriend;
import ca.ualberta.moodroid.ui.AddLocation;
import ca.ualberta.moodroid.ui.AddMood;
import ca.ualberta.moodroid.ui.AddMoodDetail;
import ca.ualberta.moodroid.ui.BaseUIActivity;
import ca.ualberta.moodroid.ui.EditDeleteFragment;
import ca.ualberta.moodroid.ui.EditMoodDetail;
import ca.ualberta.moodroid.ui.FollowListAdapter;
import ca.ualberta.moodroid.ui.FriendMap;
import ca.ualberta.moodroid.ui.FriendsMoods;
import ca.ualberta.moodroid.ui.Map;
import ca.ualberta.moodroid.ui.MoodHistory;
import ca.ualberta.moodroid.ui.MoodListAdapter;
import ca.ualberta.moodroid.ui.MoodMap;
import ca.ualberta.moodroid.ui.Notifications;
import ca.ualberta.moodroid.ui.Profile;
import ca.ualberta.moodroid.ui.SignUp;
import ca.ualberta.moodroid.ui.ViewMoodDetail;
import dagger.Component;

/**
 * The interface Service component.
 */
@Component(modules = ServiceModule.class)
@Singleton
public interface ServiceComponent {
    /**
     * Authentication service authentication service.
     *
     * @return the authentication service
     */
    AuthenticationService AuthenticationService();

    /**
     * Mood event repository mood event repository.
     *
     * @return the mood event repository
     */
    MoodEventRepository MoodEventRepository();

    /**
     * Follow request repository follow request repository.
     *
     * @return the follow request repository
     */
    FollowRequestRepository FollowRequestRepository();

    /**
     * Mood repository mood repository.
     *
     * @return the mood repository
     */
    MoodRepository MoodRepository();

    /**
     * User repository user repository.
     *
     * @return the user repository
     */
    UserRepository UserRepository();

    /**
     * fixes any di that isnt declared, although DI won't work
     *
     * @param appCompatActivity the app compat activity
     */
    void inject(AppCompatActivity appCompatActivity);

    /**
     * Inject.
     *
     * @param mainActivity the main activity
     */
    void inject(MainActivity mainActivity);

    /**
     * Inject.
     *
     * @param baseUIActivity the base ui activity
     */
    void inject(BaseUIActivity baseUIActivity);


    /**
     * These function calls allow us to implement "fake" dependency injection for android activities
     *
     * @param addFriend the add friend
     */
    void inject(AddFriend addFriend);

    /**
     * Inject.
     *
     * @param addLocation the add location
     */
    void inject(AddLocation addLocation);

    /**
     * Inject.
     *
     * @param addMood the add mood
     */
    void inject(AddMood addMood);

    /**
     * Inject.
     *
     * @param addMoodDetail the add mood detail
     */
    void inject(AddMoodDetail addMoodDetail);

    /**
     * Inject.
     *
     * @param editDeleteFragment the edit delete fragment
     */
    void inject(EditDeleteFragment editDeleteFragment);

    /**
     * Inject.
     *
     * @param editMoodDetail the edit mood detail
     */
    void inject(EditMoodDetail editMoodDetail);

    /**
     * Inject.
     *
     * @param followListAdapter the follow list adapter
     */
    void inject(FollowListAdapter followListAdapter);

    /**
     * Inject.
     *
     * @param friendMap the friend map
     */
    void inject(FriendMap friendMap);

    /**
     * Inject.
     *
     * @param friendsMoods the friends moods
     */
    void inject(FriendsMoods friendsMoods);

    /**
     * Inject.
     *
     * @param map the map
     */
    void inject(Map map);

    /**
     * Inject.
     *
     * @param moodHistory the mood history
     */
    void inject(MoodHistory moodHistory);

    /**
     * Inject.
     *
     * @param moodListAdapter the mood list adapter
     */
    void inject(MoodListAdapter moodListAdapter);

    /**
     * Inject.
     *
     * @param moodMap the mood map
     */
    void inject(MoodMap moodMap);

    /**
     * Inject.
     *
     * @param notifications the notifications
     */
    void inject(Notifications notifications);

    /**
     * Inject.
     *
     * @param profile the profile
     */
    void inject(Profile profile);

    /**
     * Inject.
     *
     * @param signUp the sign up
     */
    void inject(SignUp signUp);

    /**
     * Inject.
     *
     * @param viewMoodDetail the view mood detail
     */
    void inject(ViewMoodDetail viewMoodDetail);


}

