package ca.ualberta.moodroid;

import android.app.Application;
import android.content.Context;

public class ContextGrabber extends Application {

    private static ContextGrabber instance;

    public static ContextGrabber get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
