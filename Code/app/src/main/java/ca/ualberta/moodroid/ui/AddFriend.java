package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

public class AddFriend extends AppCompatActivity {

    Intent intent;
    UserRepository users;
    FollowRequestRepository requests;
    // TODO: Get my username and wait until it is grabbed
    String me;
    ImageButton toolBarButtonLeft;
    TextView toolBarTextView;
    String toolBarText;

    @BindView(R.id.username)
    EditText usernameField;

    @BindView(R.id.instruction)
    TextView statusField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        this.users = new UserRepository();
        this.requests = new FollowRequestRepository();
        this.me = AuthenticationService.getInstance().getUsername();

        toolBarButtonLeft = findViewById(R.id.toolbar_button_left);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarText = "Add Friend";

        toolBarTextView.setText(toolBarText);
        toolBarButtonLeft.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolBarButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to MoodMap Activity
                intent = new Intent(AddFriend.this, FriendsMoods.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.send)
    public void attemptUserFollow(View view) {
        statusField.setText("");
        Log.d("ADDUSER/OUT", "I am: " + me);
        final String name = this.usernameField.getText().toString();

        // TODO: refactor to use the userService
        users.where("username", name).one().addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
            @Override
            public void onComplete(@NonNull Task<ModelInterface> task) {
                if (task.isSuccessful()) {
                    UserModel user = (UserModel) task.getResult();
                    Log.d("ADDUSER/QUERY", "Found the user: " + user.getUsername());
                    requests.where("requesteeUsername", user.getUsername()).where("requesterUsername", me).one().addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
                        @Override
                        public void onComplete(@NonNull Task<ModelInterface> task) {
                            if (task.isSuccessful()) {
                                FollowRequestModel request = (FollowRequestModel) task.getResult();
                                // the request already exists - maybe notify the user that they already created one.
                                statusField.setText("Your request has already been sent to " + name + ". The state of your request is: " + request.getState());

                                Log.d("ADDUSER/REQUEST", "Request already exists, state: " + request.getState());
                            } else {
                                // create a new follow request
                                Log.d("ADDUSER/REQUEST", "Request non-existent");
                                FollowRequestModel request = new FollowRequestModel();
                                request.setRequesteeUsername(name);
                                request.setRequesterUsername(me);
                                request.setState(FollowRequestModel.REQUESTED_STATE);
                                request.setCreatedAt((String.valueOf((new Date()).getTime())));
                                requests.create(request).addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
                                    @Override
                                    public void onComplete(@NonNull Task<ModelInterface> task) {
                                        if (task.isSuccessful()) {
                                            statusField.setText("Your request has been sent to " + name + ". Please wait for their approval.");
                                            Log.d("ADDUSER/CREATEREQUEST", "Request Created!");
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    usernameField.setError("That username does not exist.");
                    Log.d("ADDUSER/FAILURE", "Couldn't find the user: " + name);
                }
            }
        });
    }
}
