package com.roombooking.service;

import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(int id) {
        User user = userDao.findById(id);
        if (user != null) {
            cleanUnnecessaryFields(user);
        }
        return user;
    }

    private void cleanUnnecessaryFields(User user) {
        user.setBookings(null);
        user.getRole().setUsers(null);
        user.getPosition().setRights(null);
        user.getPosition().setUsers(null);
        user.setPassword(null);  //todo for admin
    }

    public List<User> getAllUsers() {
        List<User> users = userDao.findAll();
        if (!users.isEmpty()) {
            for (User user : users) {
                cleanUnnecessaryFields(user);
            }
        }
        return users;
    }

}
