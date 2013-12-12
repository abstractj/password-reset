package org.abstractj.rest;

import org.abstractj.api.service.TokenService;
import org.abstractj.model.Credential;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Path("/")
public class PasswordResetEndpoint {

    @Inject
    private TokenService tokenService;

    private static final Logger LOGGER = Logger.getLogger(PasswordResetEndpoint.class.getSimpleName());

    @GET
    @Path("/forgot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgot(@QueryParam("email") String email) {
        tokenService.send(email);
        return Response.status(NO_CONTENT).build();
    }

    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reset(Credential credential) {
        if (tokenService.isValid(credential.getToken())) {

            LOGGER.info("Email: " + credential.getEmail());

            return Response.status(NO_CONTENT)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Yay!").build();
        } else {
            return Response.status(NOT_FOUND).build();
        }
    }
}
