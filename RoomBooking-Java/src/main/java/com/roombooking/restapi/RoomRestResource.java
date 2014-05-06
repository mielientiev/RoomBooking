package com.roombooking.restapi;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.entity.Room;
import com.roombooking.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Service
@Path("/room-service")
public class RoomRestResource {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.getClassName());
    @Autowired
    private RoomDao dao;

    @GET
    @Path("/room/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Room getRoomById(@PathParam("{id}") String id){
        return null;
    }

}
