package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.entity.Rights;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoomService {

    @Autowired
    private RoomDao roomDao;

    public Room getRoomByIdWithUserRights(int roomId, User user) {
        Room room = roomDao.getRoomByIdWithUserRights(roomId, user);
        if (room != null) {
            cleanUnnecessaryFields(room);
        }
        return room;
    }

    private void cleanUnnecessaryFields(Room room) {
        room.setBookings(null);
        room.getRoomType().setRooms(null);
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
}
