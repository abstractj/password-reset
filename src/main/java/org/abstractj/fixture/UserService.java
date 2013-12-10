package org.abstractj.fixture;

import java.util.logging.Logger;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getSimpleName());

    //Fake service to validate the user against the database
    public boolean userExists(String email){
        return (email != null || !email.isEmpty());
    }

    public void send(String uri) {
        LOGGER.info("Sending password reset instructions");
        LOGGER.info("===================================");
        LOGGER.info(uri);
        LOGGER.info("===================================");

    }
}
