package com.roombooking.service;

import com.roombooking.dao.room.RoomDao;
import com.roombooking.entity.Rights;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import com.roombooking.util.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class RoomService {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.getClassName());
    @Autowired
    private RoomDao roomDao;

    @Transactional(readOnly = true)
    public Room getRoomByIdWithUserRights(int roomId, User user) {
        Room room = roomDao.getRoomById(roomId);
        if (room != null) {
            setUserRights(user, room);
        }
        return room;
    }

    /*
        Link Problem
        room1   -->   getRoomType()[1]
        room2   -->   getRoomType()[2]
        room3  -->    getRoomType()[3]
        room4  -->    room1.getRoomType()[1]
    */
    //todo [Problem] clear 'smellcode' or rewrite on sql query
    private void setUserRights(User user, Room room) {
        logger.info("User with id " + user.getId() + " Room id " + room.getId());
        Rights rights = new Rights();
        for (Rights x : room.getRoomType().getRights()) {
            if (x.getPosition() == null) {
                return;
            }
            System.out.println("x.getPosition().getId()" + x.getPosition().getId());
            System.out.println("user.getPosition().getId()" + user.getPosition().getId());
            if (x.getPosition().getId() == user.getPosition().getId()) {
                logger.info("Rights for this room & user found: right id " + x.getId() + "; " +
                        "canBook " + x.getCanBookRoom());
                rights.setId(x.getId());
                rights.setCanBookRoom(x.getCanBookRoom());
                room.getRoomType().setRights(Arrays.asList(rights));
                break;
            }
            logger.info("Rights NOT found: right id " + x.getId() + "; " + "User position id " + user.getPosition().getId());
        }

    }

    @Transactional(readOnly = true)
    public List<Room> getAllRoomsWithUserRights(User user) {
        List<Room> rooms = roomDao.getAllRooms();

        if (!rooms.isEmpty()) {
            for (Room room : rooms) {
                setUserRights(user, room);
            }
        }
        return rooms;
    }

}
