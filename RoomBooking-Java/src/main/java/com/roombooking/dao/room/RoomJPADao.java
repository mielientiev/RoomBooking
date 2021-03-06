package com.roombooking.dao.room;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Room;
import com.roombooking.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;

public class RoomJPADao extends AbstractDao<Room> implements RoomDao {

    protected RoomJPADao() {
        super(Room.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomByIdWithUserRights(int id, User user) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.findRoomByIdWithUserPositionRights", entityClass);
        query.setParameter("usid", user.getId());
        query.setParameter("roomId", id);
        List<Room> rooms = query.getResultList();
        return rooms.isEmpty() ? null : rooms.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllRoomsWithUserRights(User user) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.findAllRoomsWithUserPositionRights", entityClass);
        query.setParameter("usid", user.getId());
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getFilteredRooms(int roomType, int places, int computers, boolean board, boolean projector, String roomName, int userId) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.filterRooms", entityClass);
        query.setParameter("usid", userId);
        query.setParameter("typeid", roomType);
        query.setParameter("places", places);
        query.setParameter("comp", computers);
        query.setParameter("board", board);
        query.setParameter("proj", projector);
        query.setParameter("roomName", "%" + roomName + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllFreeRoomsByDateAndTimetableId(int userId, Date date, int timetableId) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.findAllFreeRoomsByDateAndTimetable", entityClass);
        query.setParameter("usid", userId);
        query.setParameter("timetableId", timetableId);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllFreeRoomsByDate(int userId, Date date) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.findAllFreeRoomsByDate", entityClass);
        query.setParameter("usid", userId);
        query.setParameter("date", date);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Room findByRoomName(String roomName) {
        TypedQuery<Room> query = getEntityManager().createNamedQuery("Room.findRoomByName", entityClass);
        query.setParameter("roomName", roomName);
        List<Room> rooms = query.getResultList();
        return rooms.isEmpty() ? null : rooms.get(0);
    }

}