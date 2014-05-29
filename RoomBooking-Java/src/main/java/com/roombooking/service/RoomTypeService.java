package com.roombooking.service;

import com.roombooking.dao.roomtype.RoomTypeDao;
import com.roombooking.entity.RoomType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class RoomTypeService {

    @Autowired
    protected RoomTypeDao roomTypeDao;

    public List<RoomType> getAllRoomTypes() {
        return roomTypeDao.findAll();
    }

    public RoomType getRoomTypeById(int id) {
        return roomTypeDao.findById(id);
    }

    public RoomType addRoomType(RoomType roomType) {
        RoomType searchedRoomType = roomTypeDao.findByRoomTypeName(roomType.getRoomType());
        if (searchedRoomType != null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        RoomType newRoomType = new RoomType();
        newRoomType.setFields(roomType);
        roomTypeDao.save(newRoomType);
        return newRoomType;
    }

    public void deleteRoomTypeById(int id) {
        RoomType searchedRoomType = roomTypeDao.findById(id);
        if (searchedRoomType == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        roomTypeDao.deleteById(id);
    }

    public RoomType editRoomType(int id, RoomType roomType) {
        RoomType searchedRoomType = roomTypeDao.findById(id);
        if (searchedRoomType == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if (!searchedRoomType.getRoomType().equals(roomType.getRoomType())
                && roomTypeDao.findByRoomTypeName(roomType.getRoomType()) != null) {

            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        searchedRoomType.setFields(roomType);
        return roomTypeDao.update(searchedRoomType);
    }
}
