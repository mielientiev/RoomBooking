package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.dao.roomtype.RoomTypeDao;
import com.roombooking.entity.Room;
import com.roombooking.entity.RoomType;
import com.roombooking.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;

public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    protected RoomDao roomDao;
    @Autowired
    protected RoomTypeDao roomTypeDao;

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

    public List<Room> getAllFreeRoomsByDate(int userId, String date) {
        Date filterDate;
        try {
            filterDate = Date.valueOf(date);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        return roomDao.getAllFreeRoomsByDate(userId, filterDate);
    }

    public Room addNewRoom(Room room) {
        Room searchedRoom = roomDao.findByRoomName(room.getRoomName());
        if (searchedRoom != null) {
            logger.debug("Room with this name: {} exists", room.getRoomName());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Room newRoom = new Room();
        fillRoomInfo(newRoom, room);
        roomDao.save(newRoom);
        return newRoom;
    }

    private void fillRoomInfo(Room filledRoom, Room room) {
        RoomType roomType = roomTypeDao.findById(room.getRoomType().getId());
        if (roomType == null) {
            logger.debug("RoomType with this id: {} doesnt exist", room.getRoomType().getId());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        filledRoom.setFields(room);
        filledRoom.setRoomType(roomType);
    }

    public void deleteRoomById(int id) {
        Room room = roomDao.findById(id);
        if (room == null) {
            logger.debug("Room with id#{} doesn't exist", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        roomDao.deleteById(id);
    }

    public Room editRoom(int id, Room room) {
        Room searchedRoom = roomDao.findById(id);
        if (searchedRoom == null) {
            logger.debug("Room with id#{} doesn't exist", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        fillRoomInfo(searchedRoom, room);
        return roomDao.update(searchedRoom);
    }

}
