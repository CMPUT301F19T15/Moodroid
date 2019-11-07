package ca.ualberta.moodroid.service;


import android.location.Location;

/**
 * The type Geolocation service.
 */
public class GeolocationService implements GeolocationInterface {


    public Location getCurrentLocation() {
        return new Location("example");
    }

}
