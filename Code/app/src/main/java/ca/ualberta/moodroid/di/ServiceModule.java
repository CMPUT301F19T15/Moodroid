package ca.ualberta.moodroid.di;


import ca.ualberta.moodroid.service.MoodEventService;
import dagger.Module;
import dagger.Provides;

/**
 * The type Service module.
 */
@Module
public class ServiceModule {

    /**
     * Mood event service mood event service.
     *
     * @param service the service
     * @return the mood event service
     */
    @Provides
    MoodEventService moodEventService(MoodEventService service) {
        return service;
    }
}
