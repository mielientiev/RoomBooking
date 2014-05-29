package com.roombooking.restapi;

import com.roombooking.entity.Position;
import com.roombooking.service.PositionService;
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
@Path("/positions")
public class PositionResource {

    @Autowired
    private PositionService positionService;

    @GET
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Position> getPositions() {
        List<Position> positions = positionService.getAllPositions();
        if (positions.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return positions;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Position getPositionById(@PathParam("id") int id) {
        Position position = positionService.getPositionById(id);
        if (position == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return position;
    }

    @PUT
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Position addNewPosition(Position position) {
        if (!isValidPosition(position)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return positionService.addPosition(position);
    }

    private boolean isValidPosition(Position position) {
        return !(position == null || position.getTitle() == null);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePosition(@PathParam("id") int id) {
        positionService.deletePosition(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Position editPosition(@PathParam("id") int id, Position position) {
        if (!isValidPosition(position)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return positionService.editPosition(id, position);
    }

}
