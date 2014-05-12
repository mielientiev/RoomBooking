package com.roombooking.service;

import com.roombooking.dao.user.UserDao;
import com.roombooking.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

}
