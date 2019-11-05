package ca.ualberta.moodroid.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.utils.demo.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;

public class CustomMarkerCluster extends Map implements ClusterManager.OnClusterClickListener<MoodEventModel>, ClusterManager.OnClusterInfoWindowClickListener<MoodEventModel>, ClusterManager.OnClusterItemClickListener<MoodEventModel>, ClusterManager.OnClusterItemInfoWindowClickListener<MoodEventModel>{

    private ClusterManager<MoodEventModel> mClusterManager;
    private ArrayList<MoodEventModel> mUserList = new ArrayList<>();

    @Override
    protected void startMyMap(boolean isRestore){
        MoodEventRepository moodEvents = new MoodEventRepository();
        // get all model interfaces (moodeventModel) then change it to a moodeventModel and get the location
        moodEvents.where("username", "bryce").get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {
                for(ModelInterface m: modelInterfaces) {
                    MoodEventModel event = (MoodEventModel) m;
                    Log.d("MARKER", "NEW EVENT LOCATION: "+event.getLocation());
                }
            }
        });

        if (!isRestore) {                                           ///set to my location
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.631611, -113.323975), 9.5f));
        }

        mClusterManager = new ClusterManager<MoodEventModel>(this, getMap());
        mClusterManager.setRenderer(new MoodRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems();
        mClusterManager.cluster();

    }
    // add markers
    private void addItems() {

        mClusterManager.addItem(new MoodEventModel(getLocation(), "Bryce", R.drawable.));

    }

}
