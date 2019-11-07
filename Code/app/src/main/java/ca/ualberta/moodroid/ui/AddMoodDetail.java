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
 * Insert a mood event for the logged in user after selecting their mood
 */
public class AddMoodDetail extends AppCompatActivity {

    private ImageView mood_img;
    private TextView mood_title;
    private RelativeLayout banner;

    /**
     * The Mood.
     */
// creating the mood repo
    final MoodEventRepository mood = new MoodEventRepository();

    /**
     * The Mood event.
     */
    MoodEventModel moodEvent = new MoodEventModel();

    /**
     * The Date.
     */
    @BindView(R.id.mood_detail_date)
    protected EditText date;

    /**
     * The Time.
     */
    @BindView(R.id.mood_detail_time)
    protected EditText time;

    /**
     * The Social situation.
     */
    @BindView(R.id.social_situation)
    protected Spinner social_situation;


    /**
     * The Reason text.
     */
    @BindView(R.id.mood_detail_reason)
    protected EditText reason_text;

    /**
     * The constant situations.
     */
    protected static String[] situations = new String[]{"Alone", "One Other Person", "Two to Several People", "Crowd"};


    /**
     * The Confirm btn.
     */
    @BindView(R.id.add_detail_confirm_btn)
    protected Button confirmBtn;

    /**
     * The Date dialog.
     */
    DatePickerDialog.OnDateSetListener dateDialog;
    /**
     * The Time dialog.
     */
    TimePickerDialog.OnTimeSetListener timeDialog;

    /**
     * The Calendar.
     */
    final Calendar calendar = Calendar.getInstance();

    /**
     * Setup the display to match the previously picked mood, and update the date fields with the current date and time
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
