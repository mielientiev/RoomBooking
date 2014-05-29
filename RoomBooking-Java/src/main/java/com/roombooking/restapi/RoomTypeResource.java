package com.roombooking.restapi;

import com.roombooking.entity.RoomType;
import com.roombooking.service.RoomTypeService;
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
@Path("/roomtypes")
public class RoomTypeResource {

    @Autowired
    private RoomTypeService roomTypeService;

    @GET
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoomType> getRoomTypes() {
        List<RoomType> roomTypes = roomTypeService.getAllRoomTypes();
        if (roomTypes.isEmpty()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return roomTypes;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public RoomType getRoomTypes(@PathParam("id") int id) {
        RoomType roomType = roomTypeService.getRoomTypeById(id);
        if (roomType == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return roomType;
    }

    @PUT
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RoomType addNewRoomType(RoomType roomType) {
        if (!isValidRoomType(roomType)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roomTypeService.addRoomType(roomType);
    }

    private boolean isValidRoomType(RoomType roomType) {
        return !(roomType == null || roomType.getRoomType() == null);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    public Response deleteRoomType(@PathParam("id") int id) {
        roomTypeService.deleteRoomTypeById(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RoomType editRoomType(@PathParam("id") int id, RoomType roomType) {
        if (!isValidRoomType(roomType)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roomTypeService.editRoomType(id, roomType);
    }
}
