package org.abstractj.rest;

import org.abstractj.api.ExpirationTime;
import org.abstractj.fixture.UserService;
import org.abstractj.model.Token;
import org.abstractj.service.TokenService;
import org.abstractj.util.ResponseUtil;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

@Path("/")
public class PasswordResetEndpoint {

    @Inject
    private TokenService tokenService;
    @Inject
    private UserService userService;
    @Inject
    private ExpirationTime expirationTime;

    @GET
    @Path("/forgot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgot(@QueryParam("email") String email) {

        Token token;

        //Here of course we need to validate the e-mail against the database or PicketLink
        if (userService.userExists(email)) {
            token = tokenService.generate(new ExpirationTime());
            userService.send(uri(token.getId()));
        }
        //It' base64 encoded but also can be an Hex
        return new ResponseUtil().ok(String.format("Instructions sent to %s", email));
    }

    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reset(@QueryParam("id") String id) {

        Token token = tokenService.findTokenById(id);

        //First we check if the token is valid
        if (tokenService.isValid(token)) {
            //If yes, we need to redirect use to the login page
            //After user update the password, disable that token
            tokenService.disable(id);
            return Response.status(Response.Status.OK)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Yay!").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    private String uri(String id) {
        try {
            String url = loadProperties().getProperty("config.url");
            return String.format(url + "%s%s", "rest/reset?id=", URLEncoder.encode(id, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("META-INF/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }
}
