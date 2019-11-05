package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.auth.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.UserService;

public class FriendsMoods extends BaseUIActivity {

    UserService users;
    MoodEventService moodEvents;
    private int ACTIVITY_NUM = 2;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_moods);

        bottomNavigationView();
        ButterKnife.bind(this);
        this.setTitle("Friends Mood");


        toolBarButtonRight.setImageResource(R.drawable.ic_menu_map_foreground);
        toolBarButtonLeft.setImageResource(R.drawable.ic_person_add_black_24dp);
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(FriendsMoods.this, AddFriend.class);
                startActivity(intent);
            }
        });
        toolBarButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(FriendsMoods.this, MoodMap.class);
                startActivity(intent);
            }
        });
    }
}
