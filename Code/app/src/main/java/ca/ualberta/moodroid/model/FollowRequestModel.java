package ca.ualberta.moodroid.model;

public class FollowRequestModel extends BaseModel {


    enum RequestState {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    private String requesterUsername;
    private String requesteeUsername;
    private RequestState state;

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public String getRequesteeUsername() {
        return requesteeUsername;
    }

    public void setRequesteeUsername(String requesteeUsername) {
        this.requesteeUsername = requesteeUsername;
    }
}
