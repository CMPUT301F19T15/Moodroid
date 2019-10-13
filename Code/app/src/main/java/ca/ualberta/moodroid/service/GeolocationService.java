package ca.ualberta.moodroid.service;


import android.location.Location;

public class GeolocationService implements GeolocationInterface {


    public Location getCurrentLocation() {
        return new Location("example");
    }

}
