package com.roombooking.dao.rights;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Rights;

import java.util.List;


public class RightsJPADao extends AbstractDao<Rights> implements RightsDao {

    protected RightsJPADao() {
        super(Rights.class);
    }

    //todo
    @Override
    public List<Rights> getUserRights(int userId) {
        return null;
    }

}
