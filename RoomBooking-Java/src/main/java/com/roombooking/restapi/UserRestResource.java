package com.roombooking.restapi;

import com.fasterxml.jackson.annotation.JsonView;
import com.roombooking.dao.UserDao;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Service
@Path("/user-service")
public class UserRestResource {

    @Autowired
    private UserDao dao;

    @GET
    @RolesAllowed({"ADMIN", "User"})
    @Path("/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(User.class)
    public Response getUserById(@PathParam("id") int id) {
        User user = dao.findById(id);
        if (user != null) {
            System.out.println(user.getRole().getTitle());
            System.out.println(user.getRole().getId());
            return Response.ok().entity(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "User"})
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(User.class)
    public List<User> getAllUsers() {
        return new ArrayList<>(dao.findAll());
    }

}
