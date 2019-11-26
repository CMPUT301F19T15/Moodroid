package ca.ualberta.moodroid;

import android.app.Application;
import android.content.Context;

import ca.ualberta.moodroid.di.DaggerServiceComponent;
import ca.ualberta.moodroid.di.ServiceComponent;
import ca.ualberta.moodroid.di.ServiceModule;
import dagger.Component;

public class ContextGrabber extends Application {

    private static ContextGrabber instance;

    ServiceComponent component;


    public static ContextGrabber get() {
        return instance;
    }

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
