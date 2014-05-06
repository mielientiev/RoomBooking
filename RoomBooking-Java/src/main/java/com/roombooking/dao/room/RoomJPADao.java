package com.roombooking.dao.room;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Room;

public class RoomJPADao extends AbstractDao<Room> implements RoomDao{

    protected RoomJPADao() {
        super(Room.class);
    }


}
