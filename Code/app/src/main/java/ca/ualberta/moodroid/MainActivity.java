package ca.ualberta.moodroid;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.ualberta.moodroid.callback.Callback;
import ca.ualberta.moodroid.model.BaseModel;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.MoodInterface;

@Singleton
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MoodRepository mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        db = FirebaseFirestore.getInstance();
//        collection = db.collection("test");


        mood = new MoodRepository();


//        MoodModel model = new MoodModel();
//        model.setColor("red");
//        model.setEmoji(":angry:");
//        model.setName("Angry");
////
////
//        System.out.println(model);
//
//        mood.create(model);


        mood.get()
                // Here I am showing this as a single change to the data
                .addOnCompleteListener(new OnCompleteListener<List<ModelInterface>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<ModelInterface>> task) {
                        for (ModelInterface m : task.getResult()) {
                            MoodModel s = (MoodModel) m;
                            Log.d("RESULT", "" + s.getInternalId() + "|" + s.getName());
                            s.setName(new Random().nextInt() + "Angry");
                            mood.update(s);
                        }
                    }
                })
                // Here I am showing multiple changes to the data
                .addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
                    @Override
                    public void onSuccess(List<ModelInterface> modelInterfaces) {
                        for (ModelInterface m : modelInterfaces) {
                            MoodModel s = (MoodModel) m;
                            Log.d("RESULT", "" + s.getInternalId() + "|" + s.getName());
                        }
                    }
                });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
