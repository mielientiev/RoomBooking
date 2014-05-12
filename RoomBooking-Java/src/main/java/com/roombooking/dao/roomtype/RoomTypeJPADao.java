package com.roombooking.dao.roomtype;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.RoomType;

public class RoomTypeJPADao extends AbstractDao<RoomType> implements RoomTypeDao {

    protected RoomTypeJPADao() {
        super(RoomType.class);
    }

}
