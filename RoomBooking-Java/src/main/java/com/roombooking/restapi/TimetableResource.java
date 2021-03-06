package com.roombooking.restapi;

import com.roombooking.entity.Timetable;
import com.roombooking.service.TimetableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Path("/timetable")
public class TimetableResource {

    private static final Logger logger = LoggerFactory.getLogger(TimetableResource.class);

    @Autowired
    private TimetableService timetableService;

    @GET
    @Path("/{id}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public Timetable getTimetableById(@PathParam("id") int id) {
        logger.debug("Get timetable #{}", id);
        Timetable timetable = timetableService.getTimetable(id);
        if (timetable == null) {
            logger.debug("Timetable not found", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Timetable #{} found", id);
        return timetable;
    }

    @GET
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Timetable> getTimetable() {
        logger.debug("Get timetable");
        List<Timetable> timetable = timetableService.getTimetable();
        if (timetable.isEmpty()) {
            logger.debug("Timetable not found");
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Timetable found");
        return timetable;
    }

    @GET
    @Path("/room-{id}/{date}")
    @RolesAllowed({ADMIN, USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Timetable> getTimetableByRoomAndDate(@PathParam("id") int id, @PathParam("date") String date) {
        logger.debug("Get timetable by room #{} on {}", id, date);
        List<Timetable> timetable = timetableService.getTimetableByRoomIdAndDate(id, date);
        if (timetable.isEmpty()) {
            logger.debug("Timetable by room #{} on {}", id, date);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        logger.debug("Timetable by room #{} on {} Found", id, date);
        return timetable;
    }

    @PUT
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTimetable(Timetable timetable) {
        if (!isValidTimetable(timetable)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Timetable addedTimetable = timetableService.addNewTimetable(timetable);
        return Response.ok().entity(addedTimetable).build();
    }

    private boolean isValidTimetable(Timetable timetable) {
        return !(timetable == null || timetable.getStart() == null || timetable.getEnd() == null ||
                timetable.getStart().after(timetable.getEnd()));
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    public Response deleteTimetable(@PathParam("id") int id) {
        timetableService.deleteTimetableById(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    public Timetable editTimetable(@PathParam("id") int id, Timetable timetable) {
        if (!isValidTimetable(timetable)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return timetableService.editTimetable(id, timetable);
    }

}
