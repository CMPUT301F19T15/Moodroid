package ca.ualberta.moodroid.service;

import javax.inject.Inject;

/**
 * We should be utilizing a library to validate data and use this as an interface to the library to decouple our application from the library
 */
public class ValidationService implements ValidationInterface {


    private UserService user;

    @Inject
    public ValidationService(UserService user) {
        this.user = user;
    }

    public boolean isMoodReasonValid(String reason) {
        return true;
    }

    public boolean isUsernameAvailable(String username) {
        return true;
    }

    public boolean isUsernameValid(String username) {
        return true;
    }

}
