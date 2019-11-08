package ca.ualberta.moodroid.service;


import android.location.Location;

/**
 * The type Geolocation service.
 */
public class GeolocationService implements GeolocationInterface {


    /**
     * Returns this instance of a current location
     *
     *
     * @return
     */


    public Location getCurrentLocation() {
        return new Location("example");
    }

}
