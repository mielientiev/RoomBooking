package com.roombooking.restapi;

import com.roombooking.entity.User;
import com.roombooking.util.UserDatabase;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user-service")
public class UserService {

    @GET
    // @RolesAllowed("ADMIN")
    @PermitAll
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserById(@PathParam("id") int id) {
        return UserDatabase.getUserById(id);
    }

}
