package com.roombooking.dao.position;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Position;

public interface PositionDao extends Dao<Position> {

    Position findByPositionTitle(String title);

}
