package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;


import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

import static android.graphics.Color.parseColor;

public class ViewMoodDetail extends BaseUIActivity {
/**
 * This activity lets the user view all available details of a specific mood event.
 * The mood event to be displayed is picked by the user in the MoodHistory activity
 * by clicking on the event.
 */

    /**
     * The mood event service.
     */
    private MoodEventService eventService;

    /**
     * The mood event model.
     */
    private MoodEventModel event;

    /**
     * The internal id of the mood event.
     */
    private String eventInternalId;

    /**
     * The banner at the top of the screen displaying
     * the emoji and the mood name.
     */
    private RelativeLayout banner;

    /**
     * The mood name.
     */
    private TextView moodText;

    /**
     * The mood emoji.
     */
    private TextView emoji;

    /**
     * The text view displaying the time of the event.
     */
    private TextView timeText;

    /**
     * The text view displaying the date of the event.
     */
    private TextView dateText;

    /**
     * The text view displaying the reason for the mood event.
     */
    private TextView reasonText;

    /**
     * The text view displaying the socail situation.
     */
    private TextView situationText;

    /**
     * The text view displaying the location.
     */
    private TextView locationText; 

    /**
     * The image view displaying the mood event image.
     */
    private ImageView reasonImage;

    /**
     * The back button to return to the history activity.
     */
    private ImageButton backButton;

    /**
     * The scroll view.
     */
    ScrollView scrollView;

    /**
     * The mood event location.
     */
    private GeoPoint location;

    /**
     * The caller activity.
     */
    String callerActivity;

    /**
     * The friend's user name.
     */
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood_detail);

        //get the internal id for the mood to be displayed from the intent
        Intent intent = getIntent();
        eventInternalId = intent.getStringExtra("eventId");
        callerActivity = intent.getStringExtra("caller");



        eventService = new MoodEventService();

        //initialize all views
        emoji = findViewById(R.id.mood_img);
        banner = findViewById(R.id.banner);
        moodText = findViewById(R.id.mood_text);
        timeText = findViewById(R.id.mood_detail_time);
        dateText = findViewById(R.id.mood_detail_date);
        reasonText = findViewById(R.id.mood_detail_reason);
        situationText = findViewById(R.id.show_social_situation);
        userName = findViewById(R.id.test);
        reasonImage = findViewById(R.id.photoView);
        locationText = findViewById(R.id.show_location);
        backButton = findViewById(R.id.detail_view_back_button);
        scrollView = findViewById(R.id.view_detail_layout);

        //only show username for friend's moods
        if(callerActivity.equals(FriendsMoods.class.toString())){
            userName.setVisibility(View.VISIBLE);
        }

        backButton.setVisibility(View.VISIBLE);
        banner.setVisibility(View.INVISIBLE);
        scrollView.setVisibility(View.INVISIBLE);

        MoodService moods = new MoodService();

        //get the mood event model to be displayed
        eventService.getEventWithId(eventInternalId).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
            @Override
            public void onSuccess(MoodEventModel moodEventModel) {
                Log.d("VIEWMOODDETAIL/GET", "Got mood Event: " + eventInternalId);
                moods.getMood(moodEventModel.getMoodName()).addOnSuccessListener(new OnSuccessListener<MoodModel>() {
                    @Override
                    public void onSuccess(MoodModel moodModel) {
                        event = moodEventModel;
                        //set the values for all views
                        MoodModel mood = moodModel;
                        String emojiString = mood.getEmoji();
                        emoji.setText(emojiString);
                        banner.setBackgroundColor(parseColor(mood.getColor()));
                        moodText.setText(mood.getName());
                        timeText.setText(event.getDatetime().split(" ")[0]);
                        dateText.setText(event.getDatetime().split(" ")[1]);
                        userName.setText("@" + event.getUsername());
                        if (event.getSituation() != null) {
                            situationText.setText(event.getSituation());
                        } else {
                            situationText.setText("not specified");
                        }
                        if (!event.getReasonText().equals("")) {
                            reasonText.setText(event.getReasonText());
                        } else {
                            reasonText.setText("not specified");
                        }
                        //only try to set the image if the event has an image...
                        if (event.getReasonImageUrl() != null) {
//                            //update photo view
                            try {
                                Glide.with(ViewMoodDetail.this)
                                        .load(event.getReasonImageUrl())
                                        .into(reasonImage);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ViewMoodDetail.this, "Error: Image cannot be displayed. " + event.getReasonImageUrl(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        //only show the location if the event has a location
                        if (event.getLocation() != null) {
                            GeoPoint geoPoint = event.getLocation();
                            String latitude = String.valueOf(geoPoint.getLatitude());
                            String longitude = String.valueOf(geoPoint.getLongitude());
                            locationText.setText(latitude + "," + longitude);
                        }
                        //change status bar color
                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(Color.parseColor(moodModel.getColor()));
                        }
                        banner.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        //return to mood history activity on click of button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}