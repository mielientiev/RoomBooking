package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.dao.roomtype.RoomTypeDao;
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
        List<Room> rooms = roomDao.getFilteredRooms(roomType, places, computers, board, projector, roomName);
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            room = roomDao.getRoomByIdWithUserRights(room.getId(), user);
            result.add(room);
        }
        return result;
    }
}
