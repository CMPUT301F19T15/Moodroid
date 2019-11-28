package ca.ualberta.moodroid.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;

import static android.view.View.GONE;

/**
 * The edit mood is extremely similar to add mood detail, thus we will use all the same properties and methods to help us out
 */
public class EditMoodDetail extends AddMoodDetail {

    protected ImageView img;
    /**
     * Initialize the mood event service
     */
    @Inject
    MoodEventService moodEvents;


    @Inject
    AuthenticationService auth;


    /**
     * Setter for the event to be updated
     *
     * @param model
     */
    protected void setInitialMoodEvent(MoodEventModel model) {
        this.moodEvent = model;
    }

    /**
     * The initial UI is built here, using data from the last activity to dynamically display the
     * mood colour, emoji, and title (this method may change). the rest of the UI is made below,
     * where the user is prompted to fill in all of the details of a mood event which were explained
     * in the variables above.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextGrabber.get().di().inject(EditMoodDetail.this);

        setContentView(R.layout.activity_add_mood_detail);
        ButterKnife.bind(this);

        initializeViews();

        Intent intent = getIntent();

        String id = intent.getExtras().getString("eventId");
        String emoji = intent.getExtras().getString("emoji");
        String mood_name = intent.getExtras().getString("mood_name");
        String hex = intent.getExtras().getString("hex");

        initializeDialogs(emoji, mood_name, hex);

        // Set the design based on the mood
        mood_img.setText(intent.getExtras().getString("emoji"));
        mood_title.setText(mood_name);
        banner.setBackgroundColor(Color.parseColor(hex));


        /**
         * Once getting the event, set all the fields properly
         */
        moodEvents.getEventWithId(id).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
            @Override
            public void onSuccess(MoodEventModel moodEventModel) {
                try {
                    date.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(moodEventModel.dateObject()));
                    time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(moodEventModel.dateObject()));
                } catch (Exception e) {
                    Log.e("EDIT/DATE", "Issue setting the date from the mood..." + e.getMessage());
                }

                // setup the mood model
                setInitialMoodEvent(moodEventModel);
                //set social situation
                String val = moodEventModel.getSituation();
                if (!(val == null)) {
                    if (val.equals("Alone")) {
                        social_situation.setSelection(1);
                    } else if (val.equals("One Other Person")) {
                        social_situation.setSelection(2);
                    } else if (val.equals("Two to Several People")) {
                        social_situation.setSelection(3);
                    } else if (val.equals("Crowd")) {
                        social_situation.setSelection(4);
                    }
                }

                //set location
                if (moodEventModel.getLocation() != null) {
                    double latt = moodEventModel.getLocation().getLatitude();
                    double longt = moodEventModel.getLocation().getLongitude();
                    String loc = String.valueOf(latt) + ", " + String.valueOf(longt);
                    locationText.setText(loc);
                    removeLocationButton.setVisibility(View.VISIBLE);
                    moodLocation = moodEventModel.getLocation();
                    addLocationButton.setVisibility(GONE);

                } else {
                    removeLocationButton.setVisibility(GONE);
                    addLocationButton.setVisibility(View.VISIBLE);
                }
                //set reason
                if (moodEventModel.getReasonText().length() > 0) {
                    reason_text.setText(moodEventModel.getReasonText());
                }
                if (moodEventModel.getReasonImageUrl() != null) {
                    try {
                        Glide.with(EditMoodDetail.this)
                                .load(moodEventModel.getReasonImageUrl())
                                .into(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EditMoodDetail.this, "Error: Image cannot be displayed. " + moodEventModel.getReasonImageUrl(), Toast.LENGTH_SHORT).show();
                    }
                    url = moodEventModel.getReasonImageUrl();
                    removePhotoButton.setVisibility(View.VISIBLE);
                    addPhotoButton.setVisibility(GONE);

                } else {
                    removePhotoButton.setVisibility(GONE);
                    addPhotoButton.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    /**
     * Initialize the text and edit views on the display here
     */
    protected void initializeViews() {
        //initializing firebase storage
        // TODO use the storageService instead
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //initializing TextWatcher to check for invalid reason text input
        reason_text.addTextChangedListener(textWatcher);

        // initializing the views that will be set from the last activity
        img = findViewById(R.id.photoView);
        mood_img = findViewById(R.id.mood_img);
        mood_title = findViewById(R.id.mood_text);
        banner = findViewById(R.id.banner);
        social_situation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, EditMoodDetail.situations));


    }

    /**
     * Initialize all the custom dialogs and callbacks here
     *
     * @param emoji
     * @param mood_name
     * @param hex
     */
    protected void initializeDialogs(String emoji, String mood_name, String hex) {
        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateDateDisplay();
            }
        };

        timeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeDisplay();
            }
        };

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if a photo has previously been selected, delete that photo from Firestore before
                //choosing a new one
                if (hasPhoto) {
                    //delete current photo before proceeding
                    ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("DELETION/", "Photo deleted.");
                        }
                    });
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "choose photo"), 1);
            }
        });

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes the photo from Firestore and updates the imageView
                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETION/", "Photo deleted.");
                    }
                });
                addPhotoButton.setVisibility(View.VISIBLE);
                removePhotoButton.setVisibility(GONE);
                //clear the imageView
                photoView.setImageResource(0);

            }
        });

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMoodDetail.this, AddLocation.class);
                intent.putExtra("emoji", emoji);
                intent.putExtra("mood_name", mood_name);
                intent.putExtra("hex", hex);
                startActivityForResult(intent, 2);

            }
        });

        removeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes location from Firestore and updates imageView
                locationText.setText("");
                addLocationButton.setVisibility(View.VISIBLE);
                removeLocationButton.setVisibility(GONE);
                moodLocation = null;
            }
        });
    }


    /**
     * On click of the confirm button, update the mood event
     */
    @Override
    @OnClick(R.id.add_detail_confirm_btn)
    public void confirmClick() {
        //set moodEvent
        moodEvent.setReasonText(reason_text.getText().toString());
        moodEvent.setSituation(social_situation.getSelectedItem().toString());
        moodEvent.setMoodName(mood_title.getText().toString());
        moodEvent.setUsername(auth.getUsername());
        moodEvent.setLocation(moodLocation);
        moodEvent.setReasonImageUrl(url);


        //TODO not in the service
        MoodEventRepository moodEventRepository = new MoodEventRepository();
        moodEventRepository.update(moodEvent).addOnSuccessListener(new OnSuccessListener<ModelInterface>() {
            @Override
            public void onSuccess(ModelInterface modelInterface) {
                MoodEventModel m = (MoodEventModel) modelInterface;

                Log.d("MOODEVENT/EDIT", "Updated reason: " + m.getReasonText());
                finish();
                startActivity(new Intent(EditMoodDetail.this, MoodHistory.class));
            }
        });

    }
}

