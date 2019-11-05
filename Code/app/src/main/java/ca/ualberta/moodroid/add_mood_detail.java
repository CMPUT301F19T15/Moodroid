package ca.ualberta.moodroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;

public class add_mood_detail extends AppCompatActivity {

    private ImageView mood_img;
    private TextView mood_title;
    private RelativeLayout banner;

    // creating the mood repo
    final MoodEventRepository mood = new MoodEventRepository();

    MoodEventModel moodEvent = new MoodEventModel();





    private TextView dateInput;
    private DatePickerDialog.OnDateSetListener dateListener;

    private Button confirm_button;


    @OnClick(R.id.confirm_button)
    public void confirmClick(){

        mood.create(moodEvent).addOnSuccessListener(new OnSuccessListener<ModelInterface>() {
            @Override
            public void onSuccess(ModelInterface modelInterface) {
                final MoodEventModel m = (MoodEventModel) modelInterface;
                Log.d("RESULT/CREATE", m.getInternalId());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood_detail);
        ButterKnife.bind(this);


        confirm_button = findViewById(R.id.confirm_button);




        // initializing the views that will be set from the last activity
        mood_img = findViewById(R.id.mood_img);
        mood_title = findViewById(R.id.mood_text);
        banner = findViewById(R.id.banner);

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

        // Now, after initializing the activity with the right appearance, grabbing the date from
        // the user with a date picker and displaying it while adding it to a mood event






        moodEvent.setMoodName(mood_name);
        // TODO: TAYLOR add username to mood event.


        dateInput = findViewById(R.id.date_time);
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calendar objects
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // the date picker window is created below
                DatePickerDialog dialog = new DatePickerDialog(add_mood_detail.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Date theDate = cal.getTime();
                moodEvent.setDatetime(theDate);
                String date = year + "-" + month + "-" + day;
                dateInput.setText(date);
            }
        });
        // after the date is selected, close the window and display the selected date, while also
        // making that the date for the current ride being added
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
              //  month += 1;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                Date theDate = cal.getTime();
                moodEvent.setDatetime(theDate);
                String date = year + "-" + month + "-" + day;
                dateInput.setText(date);



            }
        };




        confirmClick();




    }
}