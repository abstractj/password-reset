package org.abstractj.rest;

import org.abstractj.api.rest.PasswordReset;
import org.abstractj.model.Credential;
import org.abstractj.api.service.TokenService;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.*;

public class PasswordResetEndpoint implements PasswordReset {

    @Inject
    private TokenService tokenService;

    @Override
    public Response forgot(String email) {
        tokenService.send(email);
        return Response.status(NO_CONTENT).build();
    }

    @Override
    public Response reset(Credential credential) {
        if (tokenService.isValid(credential.getToken())) {
            return Response.status(NO_CONTENT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Yay!").build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }
}
