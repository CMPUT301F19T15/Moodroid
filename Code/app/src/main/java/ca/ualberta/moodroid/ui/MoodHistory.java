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
import android.view.View;
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

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;

import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodService;


public class MoodHistory extends BaseUIActivity implements MoodListAdapter.OnListListener {

    MoodEventService moodEvents;
    MoodService moods;
    private int ACTIVITY_NUM = 1;
    private Intent intent;
    protected RecyclerView moodListRecyclerView;
    protected RecyclerView.Adapter moodListAdapter;
    protected RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    ArrayList<MoodEventModel> moodList;
    List<MoodModel> allMoods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);
        moodEvents = new MoodEventService();
        moods = new MoodService();
        ButterKnife.bind(this);

        //Bottom Navigation Bar Listener
        bottomNavigationView();
        setTitle("Mood History");


        toolBarButtonLeft.setImageResource(R.drawable.ic_addmood);
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(MoodHistory.this, AddMood.class);
                startActivity(intent);
            }
        });

        //Recycler List View with all mood events of the user
        moodList = new ArrayList<>();
        moodEvents.getMyEvents().addOnSuccessListener(new OnSuccessListener<List<MoodEventModel>>() {
            @Override
            public void onSuccess(List<MoodEventModel> moodEventModels) {

                Log.d("MOODHISTORY/GET", "Got mood Events: " + moodEventModels.size());

                moods.getAllMoods().addOnSuccessListener(new OnSuccessListener<List<MoodModel>>() {
                    @Override
                    public void onSuccess(List<MoodModel> moodModels) {
                        moodList.addAll(moodEventModels);
                        allMoods = moodModels;
                        reverseSort();
                        updateListView();
                    }
                });

            }
        });
    }

    protected void reverseSort() {
        Collections.sort(moodList, new Comparator<MoodEventModel>() {
            @Override
            public int compare(MoodEventModel mood1, MoodEventModel mood2) {
                try {
                    return mood2.dateObject().compareTo(mood1.dateObject());      //reversed
                } catch (Exception e) {
                    Log.e("MOODHISTORY/SORT", "Could not sort mood history: " + e.getMessage());
                }
                return 0;
            }
        });
    }

    protected void updateListView() {
        moodListRecyclerView = findViewById(R.id.mood_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(MoodHistory.this);
        moodListAdapter = new MoodListAdapter(moodList, allMoods, false, MoodHistory.this);
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
