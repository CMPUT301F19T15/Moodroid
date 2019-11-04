package ca.ualberta.moodroid.ui;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ca.ualberta.moodroid.R;

public class BottomNavView {

    public static void setupBottomNavView (BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }
    public static void enableNav(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.ic_notif:
                        Intent intent0 = new Intent(context, Notifications.class);
                        context.startActivity(intent0);
                        break;
                    case R.id.ic_moods:
                        Intent intent1 = new Intent(context, MoodHistory.class);
                        context.startActivity(intent1);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

                return false;
            }
        });
    }

}
