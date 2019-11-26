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
import ca.ualberta.moodroid.ui.FollowListAdapter;
import ca.ualberta.moodroid.ui.FriendMap;
import ca.ualberta.moodroid.ui.FriendsMoods;
import ca.ualberta.moodroid.ui.Map;
import ca.ualberta.moodroid.ui.MoodHistory;
import ca.ualberta.moodroid.ui.MoodHistoryAddFragment;
import ca.ualberta.moodroid.ui.MoodListAdapter;
import ca.ualberta.moodroid.ui.MoodMap;
import ca.ualberta.moodroid.ui.Notifications;
import ca.ualberta.moodroid.ui.Profile;
import ca.ualberta.moodroid.ui.SignUp;
import ca.ualberta.moodroid.ui.ViewMoodDetail;
import dagger.Component;

@Component(modules = ServiceModule.class)
@Singleton
public interface ServiceComponent {
    AuthenticationService AuthenticationService();

    MoodEventRepository MoodEventRepository();

    FollowRequestRepository FollowRequestRepository();

    MoodRepository MoodRepository();

    UserRepository UserRepository();

    /**
     * fixes any di that isnt declared, although DI won't work
     **/
    void inject(AppCompatActivity appCompatActivity);

    void inject(MainActivity mainActivity);

    void inject(BaseUIActivity baseUIActivity);


    /**
     * These function calls allow us to implement "fake" dependency injection for android activities
     */


    void inject(AddFriend addFriend);

    void inject(AddLocation addLocation);

    void inject(AddMood addMood);

    void inject(AddMoodDetail addMoodDetail);

    void inject(EditDeleteFragment editDeleteFragment);

    void inject(FollowListAdapter followListAdapter);

    void inject(FriendMap friendMap);

    void inject(FriendsMoods friendsMoods);

    void inject(Map map);

    void inject(MoodHistory moodHistory);

    void inject(MoodListAdapter moodListAdapter);

    void inject(MoodMap moodMap);

    void inject(Notifications notifications);

    void inject(Profile profile);

    void inject(SignUp signUp);

    void inject(ViewMoodDetail viewMoodDetail);


}

