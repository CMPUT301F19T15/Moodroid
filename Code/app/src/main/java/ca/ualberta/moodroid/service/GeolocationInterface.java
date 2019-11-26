package ca.ualberta.moodroid.service;

import android.location.Location;

/**
 * The interface Geolocation interface.
 * implemented in GeolocationService
 */
public interface GeolocationInterface {

    /**
     * Gets current location.
     *
     * @return the current location
     */
    public Location getCurrentLocation();

}
