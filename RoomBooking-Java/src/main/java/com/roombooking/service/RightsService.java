package com.roombooking.service;

import com.roombooking.dao.position.PositionDao;
import com.roombooking.dao.rights.RightsDao;
import com.roombooking.dao.roomtype.RoomTypeDao;
import com.roombooking.entity.Position;
import com.roombooking.entity.Rights;
import com.roombooking.entity.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class RightsService {

    @Autowired
    protected RightsDao rightsDao;

    @Autowired
    protected PositionDao positionDao;

    @Autowired
    protected RoomTypeDao roomTypeDao;

    public List<Rights> getAllRights() {
        return rightsDao.findAll();
    }

    public Rights getRightsById(int id) {
        return rightsDao.findById(id);
    }

    @Transactional
    public Rights addRight(Rights right) {
        Rights searchedRight =
                rightsDao.getByPositionIdAndRoomTypeId(right.getPosition().getId(), right.getRoomType().getId());

        if (searchedRight != null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Rights newRight = new Rights();
        fillRightsInfo(newRight, right);
        rightsDao.save(newRight);
        return newRight;
    }

    private void fillRightsInfo(Rights filledRights, Rights rights) {
        RoomType roomType = roomTypeDao.findById(rights.getRoomType().getId());
        if (roomType == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Position position = positionDao.findById(rights.getPosition().getId());
        if (position == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        filledRights.setRoomType(roomType);
        filledRights.setPosition(position);
    }

    public void deleteRight(int id) {
        Rights searchedRight = rightsDao.findById(id);
        if (searchedRight == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        rightsDao.deleteById(id);
    }

    @Transactional
    public Rights editRights(int id, Rights rights) {
        Rights editedRights = rightsDao.findById(id);
        if (editedRights == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        fillRightsInfo(editedRights, rights);
        return rightsDao.update(editedRights);
    }
}
