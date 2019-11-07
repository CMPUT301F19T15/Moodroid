package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ca.ualberta.moodroid.R;

/**
 * Display for selecting a mood when first creating a mood event
 */
//TODO: Dynamically grab mood from Firestore
public class AddMood extends AppCompatActivity {

    private ImageButton center_button;
    private ImageButton annoyed_button;
    private ImageButton happy_button;
    private ImageButton sad_button;
    private ImageButton mad_button;
    private ImageButton scared_button;
    private ImageButton sick_button;
    /**
     * The Annoyed drawable id.
     */
    String annoyed_drawable_id = "@drawable/annoyed";
    /**
     * The Happy drawable id.
     */
    String happy_drawable_id = "@drawable/happy";
    /**
     * The Sad drawable id.
     */
    String sad_drawable_id = "@drawable/sad";
    /**
     * The Mad drawable id.
     */
    String mad_drawable_id = "@drawable/mad";
    /**
     * The Scared drawable id.
     */
    String scared_drawable_id = "@drawable/scared";
    /**
     * The Sick drawable id.
     */
    String sick_drawable_id = "@drawable/sick";
    private boolean center_filled = false;
    /**
     * The Selected img.
     */
    String selected_img;
    /**
     * The Mood name.
     */
    String mood_name;
    /**
     * The Hex.
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

                    intent.putExtra("image_id", selected_img);
                    intent.putExtra("mood_name", mood_name);
                    intent.putExtra("hex", hex);
                    startActivity(intent);

                }


            }
        });


    }


}
