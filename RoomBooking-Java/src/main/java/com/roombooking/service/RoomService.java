package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.dao.roomtype.RoomTypeDao;
import com.roombooking.entity.Room;
import com.roombooking.entity.RoomType;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

public class RoomService {

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomTypeDao roomTypeDao;

    public Room getRoomByIdWithUserRights(int roomId, User user) {
        return roomDao.getRoomByIdWithUserRights(roomId, user);
    }

    public List<Room> getAllRoomsWithUserRights(User user) {
        return roomDao.getAllRoomsWithUserRights(user);
    }

    public List<RoomType> getAllRoomTypes() {
        return roomTypeDao.findAll();
    }

    public List<Room> filterRooms(User user, int roomType, int places, int computers,
                                  boolean board, boolean projector, String roomName) {
        return roomDao.getFilteredRooms(roomType, places, computers, board, projector, roomName, user.getId());
    }

    public List<Room> getAllFreeRoomsByDateAndTimetable(int userId, String date, int timetableId) {
        Date filterDate;
        try {
            filterDate = Date.valueOf(date);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roomDao.getAllFreeRoomsByDateAndTimetableId(userId, filterDate, timetableId);
    }
}
