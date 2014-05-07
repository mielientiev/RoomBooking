package com.roombooking.dao.room;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Room;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class RoomJPADao extends AbstractDao<Room> implements RoomDao {

    protected RoomJPADao() {
        super(Room.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomById(int id) {
        Room room = findById(id);
        if (room != null) {
            cleanUnnecessaryFields(room);
        }

        return room;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        List<Room> rooms = findAll();
        if (!rooms.isEmpty()) {
            for (Room room : rooms) {
                cleanUnnecessaryFields(room);
            }
        }
        return rooms;
    }


    private void cleanUnnecessaryFields(Room room) {
        room.setBookings(null);
        room.getRoomType().getRights();
        room.getRoomType().setRooms(null);
        room.getRoomType().getRights();
    }

}
