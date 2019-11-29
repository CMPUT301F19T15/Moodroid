package ca.ualberta.moodroid;

import android.app.Application;

import ca.ualberta.moodroid.di.DaggerServiceComponent;
import ca.ualberta.moodroid.di.ServiceComponent;

/**
 * The type Context grabber.
 */
public class ContextGrabber extends Application {

    private static ContextGrabber instance;

    /**
     * The Component.
     */
    ServiceComponent component;


    /**
     * Get context grabber.
     *
     * @return the context grabber
     */
    public static ContextGrabber get() {
        return instance;
    }

    /**
     * Di service component.
     *
     * @return the service component
     */
    public ServiceComponent di() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        component = DaggerServiceComponent.builder().build();


        instance = this;
    }
}
