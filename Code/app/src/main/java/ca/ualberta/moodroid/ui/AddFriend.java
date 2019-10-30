package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.UserService;

public class AddFriend extends AppCompatActivity {

//    UserService users;
//
//    public AddFriend(UserService userService) {
//        this.users = userService;
//    }

    UserRepository users;
    FollowRequestRepository requests;
    String me;

    @BindView(R.id.username)
    EditText usernameField;

    @BindView(R.id.status_text)
    TextView statusField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        this.users = new UserRepository();
        this.requests = new FollowRequestRepository();
        this.me = "someusername123";

        ButterKnife.bind(this);
    }

    @OnClick(R.id.request_follow_btn)
    public void attemptUserFollow(View view) {
        statusField.setText("");
        final String name = this.usernameField.getText().toString();
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
                                request.setState("undecided");
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
