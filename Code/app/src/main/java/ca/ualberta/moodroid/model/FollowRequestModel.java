package ca.ualberta.moodroid.model;

public class FollowRequestModel {


    enum RequestState {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    private String requesterUsername;
    private String requesteeUsername;
    private RequestState state;
}
