package ca.ualberta.moodroid.ui;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;
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
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.StorageService;

import static android.view.View.GONE;

/**
 * This activity follows immediately after AddMood. Once the User picks their emoji (which is
 * always associated with a particular mood) they are brought to this activity where they then fill
 * out the moods details. More information about what those details are below.
 */
public class AddMoodDetail extends AppCompatActivity {

    /**
     * The imageView.
     */
    protected TextView mood_img;

    /**
     * The mood title for the banner.
     */
    protected TextView mood_title;

    /**
     * The banner.
     */
    protected RelativeLayout banner;

    /**
     * The URI file path to the library photo .
     */
    protected Uri filePath;

    /**
     * The url for the photo as a String.
     */
    protected String url;

    /**
     * A boolean. True indicates that a photo has been uploaded.
     */
    protected boolean hasPhoto;

    /**
     * The Firestore Uri for the photo.
     */
    protected Uri urll;

    /**
     * The firebase storage references.
     */

    /**
     * The mood repository is activated below.
     */
    @Inject
    AuthenticationService auth;

    /**
     * The Mood event service.
     */
    @Inject
    MoodEventService moodEventService;

    /**
     * The Storage service.
     */
    @Inject
    StorageService storageService;
    /**
     * The mood event model is created below. This is essentially the frame for a mood event
     * it is an object that stores all details filled out by the user in this activity, and
     * is what the main mood feed in MoodHistory is made of.
     */
    MoodEventModel moodEvent = new MoodEventModel();

    /**
     * The date of the mood, given by the user.
     */
    @BindView(R.id.mood_detail_date)
    protected EditText date;

    /**
     * The time of the mood, given by the user
     */
    @BindView(R.id.mood_detail_time)
    protected EditText time;

    /**
     * The social situation of the mood, given by the user.
     */
    @BindView(R.id.social_situation)
    protected Spinner social_situation;

    /**
     * The add photo button.
     */
    @BindView(R.id.add_photo_button)
    protected Button addPhotoButton;

    /**
     * The remove photo button.
     */
    @BindView(R.id.remove_photo_button)
    protected Button removePhotoButton;

    /**
     * Add location button
     */
    @BindView(R.id.add_location_button)
    protected Button addLocationButton;

    /**
     * remove location button
     */
    @BindView(R.id.remove_location_button)
    protected Button removeLocationButton;
    /**
     * location text
     */
    @BindView(R.id.mood_detail_location)
    protected TextView locationText;

    /**
     * The Mood location.
     */
    protected GeoPoint moodLocation;


    /**
     * The reason for the mood, given by the user.
     */
    @BindView(R.id.mood_detail_reason)
    protected EditText reason_text;

    /**
     * This is an array of situations, which are the only options the user gets to pick for the
     * moods "Social situation" data.
     */
    protected static String[] situations = new String[]{"Please select... (optional)", "Alone", "One Other Person", "Two to Several People", "Crowd"};

    /**
     * The confirm button. when this button is clicked, it adds the mood to the firebase repository,
     * and brings the user back to the activity MoodHistory.
     */
    @BindView(R.id.add_detail_confirm_btn)
    protected Button confirmBtn;

    /**
     * The Reason image.
     */
    @BindView(R.id.photoView)
    protected ImageView photoView;

    /**
     * UI element, a tool used for picking dates. More specifically, used for picking the
     * moods date.
     */
    DatePickerDialog.OnDateSetListener dateDialog;
    /**
     * Same as above, but for time.
     */
    TimePickerDialog.OnTimeSetListener timeDialog;

    /**
     * A calendar object, used to store dates and times.
     */
    final Calendar calendar = Calendar.getInstance();

    /**
     * new intent
     */
    protected Intent intent;

    /**
     * The location's latitude and longitude.
     */
    String lat;
    /**
     * The Lon.
     */
    String lon;
    /**
     * The Lat lng.
     */
    String latLng;

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
        setContentView(R.layout.activity_add_mood_detail);
        ButterKnife.bind(this);
        ContextGrabber.get().di().inject(AddMoodDetail.this);

        this.date.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(new Date()));
        this.time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));

        //initializing TextWatcher to check for invalid reason text input
        reason_text.addTextChangedListener(textWatcher);

        // initializing the views that will be set from the last activity
        mood_img = findViewById(R.id.mood_img);
        mood_title = findViewById(R.id.mood_text);
        banner = findViewById(R.id.banner);
        social_situation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AddMoodDetail.situations));
        removePhotoButton.setVisibility(GONE);
        addPhotoButton.setVisibility(View.VISIBLE);
        removeLocationButton.setVisibility(GONE);
        addLocationButton.setVisibility(View.VISIBLE);


        // Below takes the intent from add_mood.java and displays the emoji, color and
        // mood title in the banner based off what the user chooses in that activity

        Intent intent = getIntent();

        String emoji = intent.getExtras().getString("emoji");
        String mood_name = intent.getExtras().getString("mood_name");
        String hex = intent.getExtras().getString("hex");

//        int mood_imageRes = getResources().getIdentifier(emoji, null, getOpPackageName());
//        Drawable res = getResources().getDrawable(mood_imageRes);

        mood_img.setText(emoji);
        mood_title.setText(mood_name);
        banner.setBackgroundColor(Color.parseColor(hex));


        /**
         * change status bar color
         */
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor(hex));
        }

        /**
         * below are the listeners that the user will interact with to set all of the specific
         * details of their mood event.
         */
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
                    storageService.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("DELETION/", "Photo deleted.");
                            hasPhoto = false;
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
                storageService.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DELETION/", "Photo deleted.");
                        hasPhoto = false;
                        addPhotoButton.setVisibility(View.VISIBLE);
                        removePhotoButton.setVisibility(GONE);
                        //clear the imageView
                        photoView.setImageResource(0);
                    }
                });
            }
            });

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMoodDetail.this, AddLocation.class);
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
     * Update date display.
     */
    public void updateDateDisplay() {
        this.date.setText(this.getDateString());
    }

    /**
     * Gets date string.
     *
     * @return the date string
     */
    public String getDateString() {
        return new SimpleDateFormat("MM/dd/yy", Locale.US).format(calendar.getTime());
    }

    /**
     * Update time display.
     */
    public void updateTimeDisplay() {
        this.time.setText(this.getTimeString());
    }

    /**
     * Gets time string.
     *
     * @return the time string
     */
    public String getTimeString() {
        return new SimpleDateFormat("HH:mm", Locale.US).format(calendar.getTime());
    }

    /**
     * Show the date picker dialog
     */
    @OnClick(R.id.mood_detail_date)
    public void dateClick() {
        Log.d("MOODDETAIL/DATE", "Date clicked!");
        new DatePickerDialog(AddMoodDetail.this, dateDialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    /**
     * Show the time picker dialog
     */
    @OnClick(R.id.mood_detail_time)
    public void timeClick() {
        Log.d("MOODDETAIL/DATE", "Time clicked!");
        new TimePickerDialog(AddMoodDetail.this, timeDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    /**
     * On click of the confirm button, create the new mood event and direct the user to the mood history view
     */
    @OnClick(R.id.add_detail_confirm_btn)
    public void confirmClick() {
        moodEvent.setDatetime(this.getDateString() + " " + this.getTimeString());
        moodEvent.setReasonText(reason_text.getText().toString());
        //social situation is optional
        if (social_situation.getSelectedItemPosition() == 0) {  //position 0 = none selected
            moodEvent.setSituation(null);
        } else {
            moodEvent.setSituation(social_situation.getSelectedItem().toString());
        }
        moodEvent.setMoodName(mood_title.getText().toString());
        moodEvent.setUsername(auth.getUsername());
        moodEvent.setReasonImageUrl(url);
        moodEvent.setLocation(moodLocation);

        moodEventService.createEvent(moodEvent).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
            @Override
            public void onSuccess(MoodEventModel moodEventModel) {
                Log.d("EVENT/CREATE", "Created new mood event: " + moodEventModel.getInternalId());
                finish();
                startActivity(new Intent(AddMoodDetail.this, MoodHistory.class));
            }
        });
    }


    /**
     * After choosing a photo from the library, this displays the image in the photoView field,
     * and saves the path to the photo in the filePath variable.
     * <p>
     * After adding a location this will display remove location button
     * and get rid of add location button
     * it will also turn the lat and lon to a geopoint
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            //update photo view
            try {
                Glide.with(AddMoodDetail.this)
                        .load(filePath)
                        .into(photoView);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AddMoodDetail.this, "Error: Image cannot be displayed." + url, Toast.LENGTH_SHORT).show();
            }
            //upload photo to firestore
            uploadPhoto();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("lon");
            //Toast.makeText(this, "lat is " + lat + "lon is " + lon, Toast.LENGTH_SHORT).show();    ////delete this :)
            //latLng = data.getStringExtra("latlng");
            String loc = lat + "," + lon;
            locationText.setText(loc);

            Double lati = Double.parseDouble(lat);
            Double longi = Double.parseDouble(lon);
            moodLocation = new GeoPoint(lati, longi);

            addLocationButton.setVisibility(GONE);
            removeLocationButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This uploads the photo the user picked from their library to the firebase storage.
     */
    protected void uploadPhoto() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            //create random name starting with user's internal id

            //add file to Firebase storage, get url
            String random = UUID.randomUUID().toString();
            storageService.addFile(FirebaseAuth.getInstance().getCurrentUser().getUid() + random, filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageService.getUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(uri != null){
                                urll = uri;
                                url = urll.toString();
                                //confirm upload to user
                                progressDialog.dismiss();
                                Toast.makeText(AddMoodDetail.this, "Image saved. ", Toast.LENGTH_SHORT).show();
                                hasPhoto = true;
                                addPhotoButton.setVisibility(GONE);
                                removePhotoButton.setVisibility(View.VISIBLE);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddMoodDetail.this, "Image could not be saved. ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddMoodDetail.this, "Image could not be saved. ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
//On Progress listener not working anymore
                    //show upload progress on the screen
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
//                        }
//                    });
        }

    }


    /**
     * When the user presses the phone's back button before saving the moodEvent, delete the photo from Firestore.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (hasPhoto) {
            storageService.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("DELETION/", "Photo deleted.");
                    hasPhoto = false;
                }
            });
        }
    }


    /**
     * Checks for valid input for the reason_text field. If more than 3 words are entered, it will
     * disable the confirm button.
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //check for >3 words or > 20 chars
            //show Done button if valid, else show cxl button
            String inputString;
            String[] inputList;
            int wordCount;
            inputString = reason_text.getText().toString();
            inputList = inputString.split("\\s+");  /*match one or more whitespaces*/
            wordCount = inputList.length;
            if (wordCount > 3) {
                confirmBtn.setClickable(false);
                reason_text.setError("Please enter up to 3 words or 20 characters.");
            } else {
                confirmBtn.setClickable(true);

            }
        }
    };

}
