package ca.ualberta.moodroid.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;

/**
 * This activity follows immediately after AddMood. Once the User picks their emoji (which is
 * always associated with a particular mood) they are brought to this activity where they then fill
 * out the moods details. More information about what those details are below.
 */
public class AddMoodDetail extends AppCompatActivity {

    /**
     * The 3 UI elements below are used to display the moods unique colour,
     * title, and emoji using the info sent from AddMood via an intent.
     *
     *
     * NOT CERTAIN THIS METHOD OF DISPLAY WILL CONTINUE TO BE USED
     */

    private ImageView mood_img;
    private TextView mood_title;
    private RelativeLayout banner;

    /**
     * The mood repository is activated below.
     */
// creating the mood repo
    final MoodEventRepository mood = new MoodEventRepository();

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
     * The reason for the mood, given by the user.
     */
    @BindView(R.id.mood_detail_reason)
    protected EditText reason_text;

    /**
     * This is an array of situations, which are the only options the user gets to pick for the
     * moods "Social situation" data.
     */
    protected static String[] situations = new String[]{"Alone", "One Other Person", "Two to Several People", "Crowd"};


    /**
     * The confirm button. when this button is clicked, it adds the mood to the firebase repository,
     * and brings the user back to the activity MoodHistory.
     */
    @BindView(R.id.add_detail_confirm_btn)
    protected Button confirmBtn;

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
        this.date.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(new Date()));
        this.time.setText(new SimpleDateFormat("HH:mm", Locale.US).format(new Date()));


        // initializing the views that will be set from the last activity
        mood_img = findViewById(R.id.mood_img);
        mood_title = findViewById(R.id.mood_text);
        banner = findViewById(R.id.banner);
        social_situation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, AddMoodDetail.situations));

        // Below takes the intent from add_mood.java and displays the emoji, color and
        // mood title in the banner based off what the user chooses in that activity

        Intent intent = getIntent();

        String image_id = intent.getExtras().getString("image_id");
        String mood_name = intent.getExtras().getString("mood_name");
        String hex = intent.getExtras().getString("hex");

        int mood_imageRes = getResources().getIdentifier(image_id, null, getOpPackageName());
        Drawable res = getResources().getDrawable(mood_imageRes);

        mood_img.setImageDrawable(res);
        mood_title.setText(mood_name);
        banner.setBackgroundColor(Color.parseColor(hex));


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
        moodEvent.setSituation(social_situation.getSelectedItem().toString());
        moodEvent.setMoodName(mood_title.getText().toString());
        moodEvent.setUsername(AuthenticationService.getInstance().getUsername());
        mood.create(moodEvent).addOnSuccessListener(new OnSuccessListener<ModelInterface>() {
            @Override
            public void onSuccess(ModelInterface modelInterface) {
                Log.d("EVENT/CREATE", "Created new mood event: " + modelInterface.getInternalId());
                startActivity(new Intent(AddMoodDetail.this, MoodHistory.class));
            }
        });

    }


}
