package com.roombooking.dao.user;

import com.roombooking.dao.Dao;
import com.roombooking.entity.User;

public interface UserDao extends Dao<User> {

    User findByLoginPassword(String login, String password);

}
