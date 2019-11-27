package ca.ualberta.moodroid.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

/**
 * This is an initial screen a user will encounter when they are creating a username/account.
 */
public class SignUp extends AppCompatActivity {


    /**
     * The repository that holds user data
     */
    @Inject
    UserRepository userRepo;

    @Inject
    AuthenticationService auth;

    @Inject
    UserService users;

    /**
     * the user object as firebase knows it.
     */
    FirebaseUser user;

    /**
     * A textview that displays the username.
     */
    @BindView(R.id.signup_username)
    EditText usernameField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        ContextGrabber.get().di().inject(SignUp.this);
        user = FirebaseAuth.getInstance().getCurrentUser();


    }

    /**
     * Register username.
     *
     * @param v the v
     */
    @OnClick(R.id.register_btn)
    public void registerUsername(View v) {
        final String username = usernameField.getText().toString();
        users.getUserByUsername(username).addOnSuccessListener(new OnSuccessListener<UserModel>() {
            @Override
            public void onSuccess(UserModel userModel) {
                //username already exists
                if(userModel != null){
                    usernameField.setError("That username is already taken.");
                } else {
                    //create new user
                    UserModel m = new UserModel();
                    m.setUsername(username);
                    users.createNewUser(m, user.getUid()).addOnSuccessListener(new OnSuccessListener<UserModel>() {
                        @Override
                        public void onSuccess(UserModel userModel) {
                            auth.setUsername(username);
                            startActivity(new Intent(SignUp.this, MoodHistory.class));
                        }
                    });
                }
            }
        });
    }
}
