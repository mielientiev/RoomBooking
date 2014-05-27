package com.roombooking.dao.position;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Position;

public class PositionJPADao extends AbstractDao<Position> implements PositionDao {

    protected PositionJPADao() {
        super(Position.class);
    }
}
