package com.roombooking.dao.role;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.Role;

public class RoleJPADao extends AbstractDao<Role> implements RoleDao {

    protected RoleJPADao() {
        super(Role.class);
    }
}
