package com.roombooking.dao;

import com.roombooking.entity.User;

public interface UserDao extends Dao<User> {

    public User findByLoginPassword(String login, String password);

}
