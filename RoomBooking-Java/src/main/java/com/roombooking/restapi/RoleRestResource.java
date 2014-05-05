package com.roombooking.restapi;

import com.fasterxml.jackson.annotation.JsonView;
import com.roombooking.dao.UserDao;
import com.roombooking.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service
@Path("/role-service")
public class RoleRestResource {

    @Autowired
    private UserDao dao;


    @GET
    @RolesAllowed("User")
    @Path("/role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(Role.class)
    public Role getUserById(@PathParam("id") int id) {
        return null;
    }

}
