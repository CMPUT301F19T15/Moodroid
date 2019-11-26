package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

/**
 * This class creates the Notification activity, one of the 4 main activities. There will be
 * data displayed on this screen via a custom array view if the user has pending requests to
 * accept or deny.
 */
public class Notifications extends BaseUIActivity implements FollowListAdapter.OnListListener {

    /**
     * The Users.
     */
    @Inject
    UserService users;


    private static final int ACTIVITY_NUM = 0;

    private RecyclerView moodListRecyclerView;
    private RecyclerView.Adapter moodListAdapter;
    private RecyclerView.LayoutManager moodListLayoutManager; //aligns items in list
    /**
     * The Request list.
     */
    ArrayList<FollowRequestModel> requestList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ContextGrabber.get().di().inject(Notifications.this);

        ButterKnife.bind(this);
        this.setTitle("Notifications");
        bottomNavigationView(ACTIVITY_NUM);

        requestList = new ArrayList<>();

        users.getAllFollowRequests().addOnSuccessListener(new OnSuccessListener<List<FollowRequestModel>>() {
            @Override
            public void onSuccess(List<FollowRequestModel> followRequestModels) {
                Log.d("NOTIFICATIONS/GET", "Got follow requests: " + followRequestModels.size());
                if (followRequestModels.size() > 0) {
                    requestList.addAll(followRequestModels);
                    updateListView();
                }
            }
        });
        users.getAllFollowingRequests().addOnSuccessListener(new OnSuccessListener<List<FollowRequestModel>>() {
            @Override
            public void onSuccess(List<FollowRequestModel> followRequestModels) {
                Log.d("NOTIFICATIONS/GET", "Got following requests: " + followRequestModels.size());
                if (followRequestModels.size() > 0) {
                    requestList.addAll(followRequestModels);
                    updateListView();
                }
            }
        });
    }


    private void updateListView() {

        Collections.sort(requestList, new Comparator<FollowRequestModel>() {
            @Override
            public int compare(FollowRequestModel t1, FollowRequestModel t2) {
                return t2.dateObject().compareTo(t1.dateObject());
            }
        });

        moodListRecyclerView = findViewById(R.id.notification_list_view);
        moodListRecyclerView.setHasFixedSize(true);
        moodListLayoutManager = new LinearLayoutManager(Notifications.this);
        moodListAdapter = new FollowListAdapter(requestList, Notifications.this);
        moodListRecyclerView.setLayoutManager(moodListLayoutManager);
        moodListRecyclerView.setAdapter(moodListAdapter);
    }

    @Override
    public void onListClick(int position) {
        return;
    }


    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView(ACTIVITY_NUM);

    }


}
