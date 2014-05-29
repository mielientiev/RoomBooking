package com.roombooking.restapi;

import com.roombooking.entity.Rights;
import com.roombooking.service.RightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.roombooking.auth.Roles.ADMIN;
import static com.roombooking.auth.Roles.USER;

@Component
@Path("/rights")
public class RightsResource {

    @Autowired
    private RightsService rightsService;

    @GET
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rights> getRights() {
        List<Rights> rights = rightsService.getAllRights();
        if (rights.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return rights;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Rights getRightsById(@PathParam("id") int id) {
        Rights right = rightsService.getRightsById(id);
        if (right == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return right;
    }

    @PUT
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rights addNewRights(Rights right) {
        if (!isValidRights(right)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return rightsService.addRight(right);
    }

    private boolean isValidRights(Rights right) {
        return !(right == null || right.getPosition() == null || right.getRoomType() == null);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRight(@PathParam("id") int id) {
        rightsService.deleteRight(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Rights editRight(@PathParam("id") int id, Rights rights) {
        if (!isValidRights(rights)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return rightsService.editRights(id, rights);
    }

}

