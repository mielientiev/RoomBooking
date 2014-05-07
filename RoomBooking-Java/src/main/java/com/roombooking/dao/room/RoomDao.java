package com.roombooking.dao.room;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Room;

import java.util.List;

public interface RoomDao extends Dao<Room> {

    Room getRoomById(int id);

    List<Room> getAllRooms();

}
