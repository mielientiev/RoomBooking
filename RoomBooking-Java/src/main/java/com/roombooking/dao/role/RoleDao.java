package com.roombooking.dao.role;

import com.roombooking.dao.Dao;
import com.roombooking.entity.Role;

public interface RoleDao extends Dao<Role> {

    Role findRoleByTitle(String title);

}
