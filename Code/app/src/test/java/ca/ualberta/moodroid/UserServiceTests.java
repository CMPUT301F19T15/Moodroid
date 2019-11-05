package ca.ualberta.moodroid;

import com.google.android.gms.auth.api.Auth;

import org.junit.Test;

import java.util.List;

import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.UserModel;
import ca.ualberta.moodroid.repository.FollowRequestRepository;
import ca.ualberta.moodroid.repository.UserRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

public class UserServiceTests {

    //init for UsErSeRvIcE
    AuthenticationService auth = AuthenticationService.getInstance();
    UserRepository reeeee = new UserRepository();
    FollowRequestRepository requests = new FollowRequestRepository();
    //create service
    UserService userService = new UserService(auth, reeeee, requests);

    //test constructor

    //get all pending requests
    @Test
    public void TestPendingFollows1(){
       List<FollowRequestModel> myFollowers =  userService.getAllPendingFollowRequests();
       List<FollowRequestModel> actual = null;
       assert(myFollowers == actual);
    }

    @Test
    public void TestPendingFollows2(){
        List<FollowRequestModel> myFollowers =  userService.getAllPendingFollowRequests();
        List<FollowRequestModel> actual = null;
        assert(myFollowers == actual);
    }

    @Test
    public void TestPendingFollows3(){
        List<FollowRequestModel> myFollowers =  userService.getAllPendingFollowRequests();
        List<FollowRequestModel> actual = null;
        assert(myFollowers == actual);
    }

    //create follow request
    @Test
    public void createFollowRequest1(){
        //replace with a made usr
        UserModel usr = userService.getUserByUsername("User");
        FollowRequestModel request = userService.createFollowRequest(usr);
        //expected here compare to request
        FollowRequestModel actual = null;
        assert(request == actual);
    }
    @Test
    public void createFollowRequest2(){
        //replace with a made usr
        UserModel usr = userService.getUserByUsername("User");
        FollowRequestModel request = userService.createFollowRequest(usr);
        //expected here compare to request
        FollowRequestModel actual = null;
        assert(request == actual);
    }
    @Test
    public void createFollowRequest3(){
        //replace with a made usr
        UserModel usr = userService.getUserByUsername("User");
        FollowRequestModel request = userService.createFollowRequest(usr);
        //expected here compare to request
        FollowRequestModel actual = null;
        assert(request == actual);
    }

    //get user by user name
    @Test
    public void getUser1(){
        UserModel usr = userService.getUserByUsername("User");
        UserModel actual = null;
        assert(usr == actual);
    }
    @Test
    public void getUser2(){
        UserModel usr = userService.getUserByUsername("User");
        UserModel actual = null;
        assert(usr == actual);
    }
    @Test
    public void getUser3(){
        UserModel usr = userService.getUserByUsername("User");
        UserModel actual = null;
        assert(usr == actual);
    }
}
