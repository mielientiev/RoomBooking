package com.roombooking.service;

import com.roombooking.dao.position.PositionDao;
import com.roombooking.entity.Position;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class PositionService {

    @Autowired
    protected PositionDao positionDao;

    public List<Position> getAllPositions() {
        return positionDao.findAll();
    }

    public Position getPositionById(int id) {
        return positionDao.findById(id);
    }

    public Position addPosition(Position position) {
        Position searchedRoomType = positionDao.findByPositionTitle(position.getTitle());
        if (searchedRoomType != null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Position newPosition = new Position();
        newPosition.setFields(position);
        positionDao.save(newPosition);
        return newPosition;
    }

    public void deletePosition(int id) {
        Position searchedPosition = positionDao.findById(id);
        if (searchedPosition == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        positionDao.deleteById(id);
    }

    public Position editPosition(int id, Position position) {
        Position searchedPosition = positionDao.findById(id);
        if (searchedPosition == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if (!position.getTitle().equals(searchedPosition.getTitle())
                && positionDao.findByPositionTitle(position.getTitle()) != null) {

            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        searchedPosition.setFields(position);
        return positionDao.update(searchedPosition);
    }
}
