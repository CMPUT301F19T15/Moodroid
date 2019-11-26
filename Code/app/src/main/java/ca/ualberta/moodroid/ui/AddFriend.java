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
import com.google.android.gms.tasks.OnSuccessListener;
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

/**
 * This class holds the logic that creates the ability for the user to interact with
 * the app and create friend requests.
 */
public class AddFriend extends AppCompatActivity {

    /**
     * This intent brings the user back to their friends list in the FriendsMoods activity
     */
    Intent intent;

    /**
     * The repository that stores all request related data.
     */
    FollowRequestRepository requests;

    /**
     * String data that stores the users username
     */
// TODO: Get my username and wait until it is grabbed
    String me;

    /**
     * Part of the UI, essentially acts as a back button to navigate back to FriendsMoods
     */
    ImageButton toolBarButtonLeft;

    /**
     * The text view that acts as a title for this activity.
     */
    TextView toolBarTextView;

    /**
     * The string data that fills the toolBarText text view.
     */
    String toolBarText;


    /**
     * This is the edit text field that the user interacts with, filling out the username of the
     * person they wish to add to their friends list.
     */
    @BindView(R.id.username)
    EditText usernameField;

    /**
     * The field of text that explains what the user can do in this activity.
     */
    @BindView(R.id.instruction)
    TextView statusField;

    /**
     * the code below builds the UI, and implements all of the logic that comes with it. As
     * stated above, this class is meant to give the user the option to add a friend by using that
     * friends username.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
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

    /**
     * When the send button is clicked, attempt to follow the user.
     * This creates a request from our app and when the "send" button is tapped it will send
     * a friend request.
     *
     * @param view the view
     */
    @OnClick(R.id.send)
    public void attemptUserFollow(View view) {
        statusField.setText("");
        Log.d("ADDUSER/OUT", "I am: " + me);
        final String name = this.usernameField.getText().toString();

        // TODO: refactor to use the userService
        new UserRepository().where("username", name).one().addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
            @Override
            public void onComplete(@NonNull Task<ModelInterface> task) {
                // We were able to find the user
                if (task.isSuccessful()) {
                    UserModel user = (UserModel) task.getResult();
                    Log.d("ADDUSER/QUERY", "Found the user: " + user.getUsername());
                    new FollowRequestRepository().where("requesteeUsername", user.getUsername()).where("requesterUsername", me).one().addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
                        @Override
                        public void onComplete(@NonNull Task<ModelInterface> task) {
                            if (task.isSuccessful()) {
                                FollowRequestModel request = (FollowRequestModel) task.getResult();
                                // If a follow request was previously declined, let the another one be sent (but only update the existing one)
                                if (request.getState().equals(FollowRequestModel.DENY_STATE)) {
                                    request.setState(FollowRequestModel.REQUESTED_STATE);
                                    // update the date
                                    request.setCreatedAt((String.valueOf((new Date()).getTime())));
                                    // Update the existing follow request, and notify the end user.
                                    requests.update(request).addOnSuccessListener(new OnSuccessListener<ModelInterface>() {
                                        @Override
                                        public void onSuccess(ModelInterface modelInterface) {
                                            statusField.setText("Your request was resent to " + name + ". It was previously declined.");

                                        }
                                    });
                                } else {
                                    // the request already exists - maybe notify the user that they already created one.
                                    statusField.setText("Your request has already been sent to " + name + ". The state of your request is: " + request.getState());
                                }

                                Log.d("ADDUSER/REQUEST", "Request already exists, state: " + request.getState());
                            } else {
                                //check if user is trying to follow themselves
                                if (!me.equals(name)) {
                                    // create a new follow request
                                    Log.d("ADDUSER/REQUEST", "Request non-existent");
                                    sendRequest(name);
                                }
                                //If you try to follow yourself
                                else {
                                    statusField.setText("You cannot follow yourself.");
                                }

                            }
                        }
                    });
                } else {
                    // the specified user doesn't exist
                    usernameField.setError("That username does not exist.");
                    Log.d("ADDUSER/FAILURE", "Couldn't find the user: " + name);
                }
            }
        });
    }

    /**
     * This sends the request and notifies the user that the request has been
     * sent. the request pends approval.
     * @param name
     */
    protected void sendRequest(String name) {

        FollowRequestModel request = new FollowRequestModel();
        request.setRequesteeUsername(name);
        request.setRequesterUsername(me);
        request.setState(FollowRequestModel.REQUESTED_STATE);
        request.setCreatedAt((String.valueOf((new

                Date()).

                getTime())));
        requests.create(request).

                addOnCompleteListener(new OnCompleteListener<ModelInterface>() {
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
