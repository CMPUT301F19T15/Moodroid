package ca.ualberta.moodroid.service;

public interface AuthenticationInterface {


    public void login(String username, String password);

    public void register(String username, String password);

    public String getUid();

}
