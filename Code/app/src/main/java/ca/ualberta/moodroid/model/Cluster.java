package ca.ualberta.moodroid.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;

public interface Cluster<T extends ClusterItem> {

    LatLng getPosition();

    Collection<T> getItems();

    int getSize();
}
