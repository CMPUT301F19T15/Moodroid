package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import butterknife.OnClick;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

public class Notifications extends AppCompatActivity {

    FollowRequestRepository requests;
    String me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        requests = new FollowRequestRepository();
        me = AuthenticationService.getInstance().getUsername();
        Log.d("NOTIFICATIONS/ME", me);

        // This will refresh the listing whenever a new item is added in real time
        requests.where("requesteeUsername", me).get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {
                Log.d("NOTIFICATIONS/SUCCESS", "Size: " + modelInterfaces.size());
                for (ModelInterface z : modelInterfaces) {
                    FollowRequestModel request = (FollowRequestModel) z;
                    Log.d("NOTIFICATIONS/REQUEST", request.getRequesteeUsername() + "|" + request.getRequesterUsername() + "|" + request.getState());

                    // TODO create the list view here with the buttons that determine the actions
                }
            }
        });
    }


    public void denyRequest() {
        // On a button click
        // TODO: deny the request, and also set the list view state to not show buttons and change color
    }

    public void acceptRequest() {
        // On a button click
        // TODO: accept the request, and also set the list view state to not show buttons and change color

    }

}
