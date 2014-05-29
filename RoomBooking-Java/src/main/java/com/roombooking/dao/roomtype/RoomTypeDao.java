package com.roombooking.dao.roomtype;

import com.roombooking.dao.Dao;
import com.roombooking.entity.RoomType;

public interface RoomTypeDao extends Dao<RoomType> {

    RoomType findByRoomTypeName(String roomType);

}
