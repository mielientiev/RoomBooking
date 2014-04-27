package com.roombooking.util;

import com.roombooking.dao.UserDaoInMemory;
import com.roombooking.entity.User;

public class UserDatabase {
    private static UserDaoInMemory userDaoInMemory = new UserDaoInMemory();

    public static User getUserById(int id) {
        return userDaoInMemory.getUserById(id);
    }

    public static User authenticate(String login, String password) {
        System.out.println("LOGIN = " + login + " PASSW " + password);
        User user = userDaoInMemory.auth(login, password);
        System.out.println(user);
        if (user == null) {
            return null;
        }
        return user;
    }
}
