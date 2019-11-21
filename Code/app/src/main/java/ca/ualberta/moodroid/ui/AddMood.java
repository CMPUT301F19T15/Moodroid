package ca.ualberta.moodroid.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import ca.ualberta.moodroid.R;

/**
 * This activity presents the initial UI for creating a mood event. It starts at
 * a screen where each mood emoji is displayed in a circle around a center button. The user will
 * select a mood from the circle, and the mood will be displayed in the center. To confirm the
 * mood selection, the user will tap the mood in the center and be brought to the AddMoodDetail
 * activity.
 */
//TODO: Dynamically grab mood from Firestore
public class AddMood extends AppCompatActivity {


    /**
     * All buttons below represent each mood button in the circle, represented by their
     * corresponding emojis
     */

    private ImageButton center_button;
    private ImageButton annoyed_button;
    private ImageButton happy_button;
    private ImageButton sad_button;
    private ImageButton mad_button;
    private ImageButton scared_button;
    private ImageButton sick_button;

    /**
     * All Strings below represent the emojis resource ID's which are used for display purposes
     *
     * NOT SURE IF WE ARE KEEPING THIS METHOD OF DISPLAY
     */
    String annoyed_drawable_id = "@drawable/annoyed";
    String happy_drawable_id = "@drawable/happy";
    String sad_drawable_id = "@drawable/sad";
    String mad_drawable_id = "@drawable/mad";
    String scared_drawable_id = "@drawable/scared";
    String sick_drawable_id = "@drawable/sick";

    /**
     * This boolean indicates whether the center of the mood circle has been filled or not. The
     * user will not be able to proceed to the next activity if this boolean is false.
     */

    private boolean center_filled = false;


    /**
     * the string data for the selected image from the wheel, which will be one of the drawable Id's
     */
    String selected_img;
    /**
     * The string data for the name of the mood.
     */
    String mood_name;
    /**
     * The hex colour code for the corresponding mood
     */
    String hex;

    /**
     * Set a listener for each button, and on click but the drawable in the center. When the center is clicked, go to the mood detail screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        /**
         * change color of the status bar
         */
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }


        center_button = findViewById(R.id.centerButton);
        annoyed_button = findViewById(R.id.Annoyed);
        happy_button = findViewById(R.id.Happy);
        sad_button = findViewById(R.id.Sad);
        mad_button = findViewById(R.id.Mad);
        scared_button = findViewById(R.id.Scared);
        sick_button = findViewById(R.id.Sick);

        /*
         * The purpose of this code is to simply display the mood you tap in the center
         * of the screen, and then take that mood's image, color, and name and bring it to the next screen
         * where you will be able to set the details. Below are a few onclick listeners for
         * each mood button in the wheel. When tapped, they access their own image from the
         * Drawable folder and draw that image in the center.
         */

        annoyed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int annoyed_imageRes = getResources().getIdentifier(annoyed_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(annoyed_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = annoyed_drawable_id;
                mood_name = "Annoyed";
                hex = "#830678";


            }
        });
        happy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int happy_imageRes = getResources().getIdentifier(happy_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(happy_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = happy_drawable_id;
                mood_name = "Happy";
                hex = "#F3CA3E";
            }
        });
        sad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sad_imageRes = getResources().getIdentifier(sad_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(sad_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = sad_drawable_id;
                mood_name = "Sad";
                hex = "#1C1EEB";
            }
        });

        mad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mad_imageRes = getResources().getIdentifier(mad_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(mad_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = mad_drawable_id;
                mood_name = "Mad";
                hex = "#D61F1F";
            }
        });
        scared_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scared_imageRes = getResources().getIdentifier(scared_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(scared_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = scared_drawable_id;
                mood_name = "Scared";
                hex = "#C5FFFF";
            }
        });

        sick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sick_imageRes = getResources().getIdentifier(sick_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(sick_imageRes);
                center_button.setImageDrawable(res);
                center_filled = true;
                selected_img = sick_drawable_id;
                mood_name = "Sick";
                hex = "#97BD00";
            }
        });


        center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (center_filled) {
                    Intent intent = new Intent(AddMood.this, AddMoodDetail.class);
                    intent.putExtra("emoji", getMoodEmoji(centerMood));
                    intent.putExtra("mood_name", centerMood);
                    intent.putExtra("hex", getMoodColor(centerMood));
                    startActivity(intent);
                    startActivity(intent);

                }


            }
        });


    }


}
