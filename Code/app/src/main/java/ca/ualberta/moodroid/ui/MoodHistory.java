package ca.ualberta.moodroid.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;


public class MoodHistory extends AppCompatActivity {

    MoodEventService moodEvents;
    private static final int ACTIVITY_NUM=1;

    public MoodHistory(MoodEventService moodEventService) {
        this.moodEvents = moodEventService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mood_history);
        setContentView(R.layout.activity_main);

        bottomNavigationView();
        sviewPager();

    }

    private void sviewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MoodHistoryAddFragment());
        adapter.addFragment(new MoodHistoryFragment());
        ViewPager viewPager= findViewById(R.id.body);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Bottom Navigation view
    private void bottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomnav);
        BottomNavView.setupBottomNavView(bottomNavigationViewEx);
        BottomNavView.enableNav(MoodHistory.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
