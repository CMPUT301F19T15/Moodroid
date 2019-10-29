package ca.ualberta.moodroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class add_mood extends AppCompatActivity {

    private ImageButton annoyed_button;
    private ImageButton center_button;
    String annoyed_drawable_id = "@drawable/annoyed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);


        annoyed_button = findViewById(R.id.Annoyed);
        center_button = findViewById(R.id.centerButton);




        annoyed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int annoyed_imageRes = getResources().getIdentifier(annoyed_drawable_id, null, getOpPackageName());
                Drawable res = getResources().getDrawable(annoyed_imageRes);
                center_button.setImageDrawable(res);

            }
        });



    }





}
