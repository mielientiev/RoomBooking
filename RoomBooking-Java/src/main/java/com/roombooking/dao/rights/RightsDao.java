package com.roombooking.dao.rights;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Rights;

import java.util.List;


public interface RightsDao extends Dao<Rights> {

    List<Rights> getUserRights(int userId);

}
