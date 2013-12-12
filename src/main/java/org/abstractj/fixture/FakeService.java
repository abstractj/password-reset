package org.abstractj.fixture;

import org.abstractj.model.Credential;

public class FakeService {

    //Fake service to validate the user against the database
    public static boolean userExists(String email) {
        return isNotEmpty(email);
    }

    public static boolean update(Credential credential) {
        return matches(credential.getPassword(), credential.getConfirmation());
    }

    private static boolean isNotEmpty(String email) {
        return (email != null || !email.isEmpty());
    }

    private static boolean matches(String password, String confirmation) {
        if(isNotEmpty(password) && isNotEmpty(confirmation)) {
            return password.equals(confirmation);
        }
        return false;
    }

}
