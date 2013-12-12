package org.abstractj.fixture;

public class FakeService {

    //Fake service to validate the user against the database
    public static boolean userExists(String email){
        return (email != null || !email.isEmpty());
    }

}
