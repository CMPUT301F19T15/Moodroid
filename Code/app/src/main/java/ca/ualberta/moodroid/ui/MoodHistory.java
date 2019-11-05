package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

import java.util.List;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.R;
//import ca.ualberta.moodroid.model.ModelInterface;
//import ca.ualberta.moodroid.model.MoodEventModel;
//import ca.ualberta.moodroid.model.MoodModel;

import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;

import ca.ualberta.moodroid.service.MoodEventService;


public class MoodHistory extends BaseUIActivity implements MoodListAdapter.OnListListener {

    MoodEventService moodEvents;
    private int ACTIVITY_NUM = 1;
    private Intent intent;
    private RecyclerView moodListRecyclerView;
    private RecyclerView.Adapter moodListAdapter;
    private RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    ArrayList<MoodEventModel> moodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        moodEvents = new MoodEventService();
        ButterKnife.bind(this);

        //Bottom Navigation Bar Listener
        bottomNavigationView();
        setTitle("Mood History");


        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();
        moodEvents.getMyEvents().addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
            @Override
            public void onSuccess(List<MoodEventModel> moodEventModels) {
                Log.d("MOODHISTORY/GET", "Got mood Events: " + moodEventModels.size());
                moodList.addAll(moodEventModels);
                reverseSort();
                updateListView();
            }
        });
    }

    private void reverseSort() {
        //sort array on date/time in reverse order
        Collections.sort(moodList, new Comparator<MoodEventModel>() {
            @Override
            public int compare(MoodEventModel mood1, MoodEventModel mood2) {
                return mood2.getDatetime().compareTo(mood1.getDatetime());      //reversed
            }
        });
    }

    private void updateListView() {
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(MoodHistory.this);
        moodListAdapter = new MoodListAdapter(moodList, moodEvents, MoodHistory.this);
        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);
    }

    @Override
    public void onListClick(int position) {
        openEditDeleteDialog();
    }

    public void openEditDeleteDialog() {

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(getSupportFragmentManager(), "Options");
    }
}
