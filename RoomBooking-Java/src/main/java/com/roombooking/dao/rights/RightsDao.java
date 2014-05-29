package com.roombooking.dao.rights;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Rights;

public interface RightsDao extends Dao<Rights> {

    Rights getByPositionIdAndRoomTypeId(int posId, int roomTypeId);

}
