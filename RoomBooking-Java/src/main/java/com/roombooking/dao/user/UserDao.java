package com.roombooking.dao.user;

import com.roombooking.dao.Dao;
import com.roombooking.entity.User;

import java.util.List;

public interface UserDao extends Dao<User> {

    public User findByLoginPassword(String login, String password);

    public List<User> getAllUsers();

    public User getUserById(int id);

}
