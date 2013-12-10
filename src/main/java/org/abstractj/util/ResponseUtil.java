package org.abstractj.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ResponseUtil {

    private static Response.Status status;

    private class Message {

        private String message;

        private Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public Response ok(String message) {
        return Response.status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(new Message("Reset instructions sent!")).build();
    }
}
