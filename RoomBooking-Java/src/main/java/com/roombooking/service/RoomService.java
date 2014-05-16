package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.dao.roomtype.RoomTypeDao;
import com.roombooking.entity.Rights;
import com.roombooking.entity.Room;
import com.roombooking.entity.RoomType;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RoomService {

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomTypeDao roomTypeDao;

    public Room getRoomByIdWithUserRights(int roomId, User user) {
        Room room = roomDao.getRoomByIdWithUserRights(roomId, user);
        if (room != null) {
            cleanUnnecessaryFields(room);
        }
        return room;
    }

    private void cleanUnnecessaryFields(Room room) {         //todo change this on JsonView
        for (Rights rights : room.getRoomType().getRights()) {
            rights.setPosition(null);
            rights.setRoomType(null);
        }
    }

    public List<Room> getAllRoomsWithUserRights(User user) {
        List<Room> rooms = roomDao.getAllRoomsWithUserRights(user);
        if (!rooms.isEmpty()) {
            for (Room room : rooms) {
                cleanUnnecessaryFields(room);
            }
        }
        return rooms;
    }

    public List<RoomType> getAllRoomTypes() {
        return roomTypeDao.findAll();
    }

    public List<Room> filterRooms(User user, int roomType, int places, int computers,
                                  boolean board, boolean projector, String roomName) {
        List<Room> rooms = roomDao.getFilteredRooms(roomType, places, computers, board, projector, roomName);
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            room = roomDao.getRoomByIdWithUserRights(room.getId(), user);
            cleanUnnecessaryFields(room);
            result.add(room);
        }
        return result;
    }
}
