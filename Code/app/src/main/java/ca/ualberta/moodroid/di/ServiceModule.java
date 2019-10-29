package ca.ualberta.moodroid.di;


import ca.ualberta.moodroid.service.MoodEventService;
import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    MoodEventService moodEventService(MoodEventService service) {
        return service;
    }
}
