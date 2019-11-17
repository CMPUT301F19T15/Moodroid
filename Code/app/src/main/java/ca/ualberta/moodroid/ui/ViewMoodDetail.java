package ca.ualberta.moodroid.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Authentication;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;


import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;

import static android.graphics.Color.parseColor;

public class ViewMoodDetail extends BaseUIActivity {
/**
 * This activity lets the user view all available details of a specific mood event.
 * The mood event to be displayed is picked by the user in the MoodHistory activity
 * by clicking on the event.
 */

///////////////////internalId not updated?
        //////////////////////show location
        

        MoodEventService eventService;
        MoodEventModel event;
        String eventInternalId;
        RelativeLayout banner;
        TextView moodText;
        TextView emoji;
        TextView timeText;
        TextView dateText;
        TextView reasonText;
        TextView situationText;
        ImageView reasonImage;
        ImageButton backButton;
        String imageReasonUrl;
        GeoPoint location;


//        @BindView(R.id.mood_img)
//        protected TextView emoji;

    //    /**
//     * The initial UI is built here, using data from the last activity to dynamically display the
//     * mood colour, emoji, and title (this method may change). the rest of the UI is made below,
//     * where the user is prompted to fill in all of the details of a mood event which were explained
//     * in the variables above.
//     *
//     * @param savedInstanceState
//     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood_detail);

        //get the internal id for the mood to be displayed from the intent
        Intent intent = getIntent();
        eventInternalId = intent.getStringExtra("eventId");

        eventService = new MoodEventService();

        emoji = findViewById(R.id.mood_img);
        banner = findViewById(R.id.banner);
        moodText = findViewById(R.id.mood_text);
        timeText = findViewById(R.id.mood_detail_time);
        dateText = findViewById(R.id.mood_detail_date);
        reasonText = findViewById(R.id.mood_detail_reason);
        situationText = findViewById(R.id.show_social_situation);
        reasonImage = findViewById(R.id.photoView);
        backButton = findViewById(R.id.detail_view_back_button);

        ArrayList<MoodEventModel> moodList;
        MoodService moods = new MoodService();
        moodList = new ArrayList<>();
        eventService.getEventWithId(eventInternalId).addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
            @Override
            public void onSuccess(List<MoodEventModel> moodEventModels) {
                Log.d("MOODHISTORY/GET", "Got mood Events: " + moodEventModels.size());
                moods.getMood(moodEventModels.get(0).getMoodName()).addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
                    @Override
                    public void onSuccess(List<MoodModel> moodModels) {
                        moodList.addAll(moodEventModels);
                        event = moodList.get(0);
                        //set the values for all views
                        MoodModel moodModel = moodModels.get(0);
                        String emojiString = moodModel.getEmoji();
                        emoji.setText(emojiString);
                        banner.setBackgroundColor(parseColor(moodModel.getColor()));
                        moodText.setText(moodModel.getName());
                        timeText.setText(event.getDatetime().split(" ")[0]);
                        dateText.setText(event.getDatetime().split(" ")[1]);
                        if(event.getSituation() != null){
                            situationText.setText(event.getSituation());
                        }
                        if(event.getReasonText() != null){
                            reasonText.setText(event.getReasonText());
                        }
                        //only try to set the image if the event has an image...
                        if(event.getReasonImageUrl() != null) {
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
                        if(event.getLocation() != null){
                            //TO DO: ADD LOCATION STUFF...mini MAP??? or ADDRESS???
                            //
                            //
                            //
                            //
                        }

                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



}