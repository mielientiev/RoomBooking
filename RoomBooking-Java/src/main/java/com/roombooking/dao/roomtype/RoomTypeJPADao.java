package com.roombooking.dao.roomtype;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.RoomType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

public class RoomTypeJPADao extends AbstractDao<RoomType> implements RoomTypeDao {

    protected RoomTypeJPADao() {
        super(RoomType.class);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomType findByRoomTypeName(String roomTypeName) {
        TypedQuery<RoomType> query = getEntityManager().createNamedQuery("RoomType.findRoomTypeByRoomTypeName", entityClass);
        query.setParameter("roomName", roomTypeName);
        List<RoomType> roomTypes = query.getResultList();
        return roomTypes.isEmpty() ? null : roomTypes.get(0);
    }

}
