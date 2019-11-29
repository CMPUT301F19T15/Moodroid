package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

/**
 * The type Map.
 */
public class FriendMap extends Map {

    /**
     * Map activity where it creates the map with the
     * desired markers on the map and sets the camera view
     * <p>
     * TO DO
     * - Make the marker look better and easier to see
     * - add icon generator to do that
     * - change the map view to current location
     * - implement the current location on the map
     * - add an on long click listener to the marker
     * to take the user to view all of the mood details for that mood
     * - if time then add clusters
     */
    public FriendMap() {

    }

    /**
     * setting the tag
     */
    private static final String TAG = "Friend maps activity";

    @Inject
    UserService users;


    @Inject
    MoodEventService events;


    /**
     * New instance map.
     *
     * @return the map
     */
    public static FriendMap newInstance() {
        return new FriendMap();
    }

    /**
     * when the user clicks on the
     * map icon it will create
     * all the buttons
     * and any text views
     * that need to be changed
     * <p>
     * also sets the on click listeners
     * for each button
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextGrabber.get().di().inject(FriendMap.this);

        toolBarText = "Friends Moods";
        toolBarTextView.setText(toolBarText);


        /**
         * when the user clicks on the tool bar left button it will
         * navigate the user back to their profile page
         */
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to back to profile activity
                intent = new Intent(FriendMap.this, FriendsMoods.class);
                finish();
                startActivity(intent);
            }
        });

        /**
         *
         * when the user clicks on the tool bar right button it will
         * add all of their friends moods to the map
         * or navigate to another page where it has all
         * of the users moods and their friends moods
         *
         * need to decide on whats best
         *
         */
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to the map with all my moods and all friends moods
                //
                //
                // ADD HERE
                //
                //
            }
        });


    }


    /**
     * this is to add the map markers to the map
     * <p>
     * it creates an icon generator
     * <p>
     * pulls all of the Mood events for that user
     * <p>
     * loops through all of the event mood model
     * and then adds it by calling the addicon
     * <p>
     * each mood will have
     * - emoji
     * - location
     * - date time as title
     * - situation as snippit
     */
    @Override
    protected void addMapMarkers() {

        // creating new icon generator
        final IconGenerator iconFactory = new IconGenerator(this);

        users.getAllUsersIFollow().addOnSuccessListener(new OnSuccessListener<List<FollowRequestModel>>() {
            @Override
            public void onSuccess(List<FollowRequestModel> followRequestModels) {
                mMap.clear();
                ArrayList<Task<List<MoodEventModel>>> taskList = new ArrayList<>();
                final int totalUsers = followRequestModels.size();
                for (FollowRequestModel user : followRequestModels) {
                    Log.d("FRIENDSMOOD/FRIEND", "Got friend: " + user.getRequesteeUsername());
                    events.getEventsForUser(user.getRequesteeUsername()).addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
                        @Override
                        public void onSuccess(List<MoodEventModel> moodEventModels) {
                            for (MoodEventModel event : moodEventModels) {
                                try {
                                    // This could have a possible race condition
                                    for (MoodModel mood : moods) {
                                        if (mood.getName().equals(event.getMoodName())) {
                                            addIcon(iconFactory, mood.getEmoji(), new LatLng(event.getLocation().getLatitude(), event.getLocation().getLongitude()), event.getUsername(), event.getDatetime() + ", " + event.getSituation());
                                        }
                                    }

                                    // just catching any locations that have null values for them
                                } catch (NullPointerException e) {
                                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

}
