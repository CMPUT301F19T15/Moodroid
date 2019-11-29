package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.service.MoodService;

/**
 * This activity presents the initial UI for creating a mood event. It starts at
 * a screen where each mood emoji is displayed in a circle around a center button. The user will
 * select a mood from the circle, and the mood will be displayed in the center. To confirm the
 * mood selection, the user will tap the mood in the center and be brought to the AddMoodDetail
 * activity.
 */

public class AddMood extends AppCompatActivity {


    /**
     * All buttons below represent each mood button in the circle, represented by their
     * corresponding emojis
     */

    private Button center_button;
    private Button annoyed_button;
    private Button happy_button;
    private Button sad_button;
    private Button mad_button;
    private Button scared_button;
    private Button sick_button;


    /**
     * This boolean indicates whether the center of the mood circle has been filled or not. The
     * user will not be able to proceed to the next activity if this boolean is false.
     */

    private String centerMood = null;

    /**
     * The mood Service.
     */
    @Inject
    MoodService moodService;

    /**
     * The list of mood models.
     */
    ArrayList<MoodModel> allMoods;


    /**
     * Set a listener for each button, and on click but the drawable in the center. When the center is clicked, go to the mood detail screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);
        ContextGrabber.get().di().inject(AddMood.this);

        /**
         * change color of the status bar
         */
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //initialize the views
        center_button = findViewById(R.id.centerButton);
        annoyed_button = findViewById(R.id.Annoyed);
        happy_button = findViewById(R.id.Happy);
        sad_button = findViewById(R.id.Sad);
        mad_button = findViewById(R.id.Mad);
        scared_button = findViewById(R.id.Scared);
        sick_button = findViewById(R.id.Sick);

        /**
         * The purpose of this code is to simply display the mood you tap in the center
         * of the screen, and then take that mood's image, color, and name and bring it to the next screen
         * where you will be able to set the details. Below are a few onclick listeners for
         * each mood button in the wheel. When tapped, they access their own image from the
         * Drawable folder and draw that image in the center.
         */

        //get mood models
        allMoods = new ArrayList<>();
        moodService.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
            @Override
            public void onSuccess(List<MoodModel> moodModels) {
                List<MoodModel> m = moodModels;
                allMoods.addAll(m);
                for (MoodModel moodModel : moodModels) {
                    switch (moodModel.getName()) {
                        case "Annoyed":
                            annoyed_button.setText(moodModel.getEmoji());
                            break;
                        case "Scared":
                            scared_button.setText(moodModel.getEmoji());
                            break;
                        case "Happy":
                            happy_button.setText(moodModel.getEmoji());
                            break;
                        case "Sad":
                            sad_button.setText(moodModel.getEmoji());
                            break;
                        case "Sick":
                            sick_button.setText(moodModel.getEmoji());
                            break;
                        case "Mad":
                            mad_button.setText(moodModel.getEmoji());
                            break;
                    }
                }

            }
        });

        annoyed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Annoyed"));
                centerMood = "Annoyed";
            }
        });
        happy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Happy"));
                centerMood = "Happy";
            }
        });
        sad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Sad"));
                centerMood = "Sad";
            }
        });

        mad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Mad"));
                centerMood = "Mad";
            }
        });
        scared_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Scared"));
                centerMood = "Scared";
            }
        });

        sick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                center_button.setText(getMoodEmoji("Sick"));
                centerMood = "Sick";
            }
        });


        center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (centerMood != null) {
                    Intent intent = new Intent(AddMood.this, AddMoodDetail.class);
                    intent.putExtra("emoji", getMoodEmoji(centerMood));
                    intent.putExtra("mood_name", centerMood);
                    intent.putExtra("hex", getMoodColor(centerMood));
                    finish();
                    startActivity(intent);
                }


            }
        });

    }

    /**
     * Return the correct emoji corresponding to the moodName.
     *
     * @param moodName
     * @return emoji String
     */
    private String getMoodEmoji(String moodName) {
        for (MoodModel moodModel : allMoods) {
            if (moodModel.getName().equals(moodName)) {
                return moodModel.getEmoji();
            }
        }
        return null;
    }

    /**
     * Return the correct color for the mood.
     *
     * @param moodName
     * @return color String
     */
    private String getMoodColor(String moodName) {
        for (MoodModel moodModel : allMoods) {
            if (moodModel.getName().equals(moodName)) {
                return moodModel.getColor();
            }
        }
        return null;
    }
}