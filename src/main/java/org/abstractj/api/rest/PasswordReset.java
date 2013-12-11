package org.abstractj.api.rest;

import org.abstractj.model.Credential;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface PasswordReset {

    @GET
    @Path("/forgot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgot(@QueryParam("email") String email);

    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reset(Credential credential);
}
