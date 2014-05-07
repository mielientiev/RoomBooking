package com.roombooking.dao.rights;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Rights;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class RightsJPADao extends AbstractDao<Rights> implements RightsDao {

    protected RightsJPADao() {
        super(Rights.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rights> getUserRights(int userId) {
        return null;
    }
}
