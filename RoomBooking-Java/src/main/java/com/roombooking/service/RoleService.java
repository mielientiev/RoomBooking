package com.roombooking.service;

import com.roombooking.dao.role.RoleDao;
import com.roombooking.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class RoleService {

    @Autowired
    protected RoleDao roleDao;

    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    public Role getRoleById(int id) {
        return roleDao.findById(id);
    }

    public Role addRole(Role role) {
        Role searchedRole = roleDao.findRoleByTitle(role.getTitle());
        if (searchedRole != null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        Role newRole = new Role();
        newRole.setFields(role);
        roleDao.save(newRole);
        return newRole;
    }

    public void deleteRole(int id) {
        Role searchedRole = roleDao.findById(id);
        if (searchedRole == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        roleDao.deleteById(id);
    }

    public Role editRole(int id, Role role) {
        Role searchedRole = roleDao.findById(id);
        if (searchedRole == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        if (!role.getTitle().equals(searchedRole.getTitle())
                && roleDao.findRoleByTitle(role.getTitle()) != null) {

            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        searchedRole.setFields(role);
        return roleDao.update(searchedRole);
    }
}
