package com.roombooking.service;

import com.roombooking.dao.position.PositionDao;
import com.roombooking.dao.role.RoleDao;
import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.Position;
import com.roombooking.entity.Role;
import com.roombooking.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected RoleDao roleDao;

    @Autowired
    protected PositionDao positionDao;

    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public User addNewUser(User user) {
        User searchedUser = userDao.findByLogin(user.getLogin());

        if (searchedUser != null) {
            logger.debug("User with this login: {} exists", user.getLogin());
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        User newUser = new User();
        fillUserInfo(newUser, user);

        userDao.save(newUser);
        return newUser;
    }

    private void fillUserInfo(User filledUser, User user) {
        Role role = roleDao.findById(user.getRole().getId());
        if (role == null) {
            logger.debug("Role with this id: {} doesnt exist", user.getRole().getId());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Position position = positionDao.findById(user.getPosition().getId());
        if (position == null) {
            logger.debug("Position with this id: {} doesnt exist", user.getPosition().getId());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        filledUser.setFields(user);
        filledUser.setRole(role);
        filledUser.setPosition(position);
    }

    public void deleteUserById(int id) {
        User user = userDao.findById(id);
        if (user == null) {
            logger.debug("User with id#{} doesn't exist", id);
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        userDao.deleteById(id);
    }

    @Transactional
    public User editUser(int id, User user) {
        User editedUser = userDao.findById(id);
        if (editedUser == null) {
            logger.debug("User with this login doesn't {} exists", user.getLogin());
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        fillUserInfo(editedUser, user);
        return userDao.update(editedUser);
    }
}
