package com.roombooking.dao;

import com.roombooking.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDaoInMemory {

    private List<User> users = new ArrayList<>();

    public UserDaoInMemory() {
        users.add(new User(1, "login", "passw", "Igor", "Melentev", 1));
        users.add(new User(2, "login2", "passw2", "Sergey", "March", 0));
        users.add(new User(3, "login3", "passw4", "asda", "asd", 1));
    }

    public User getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    public User auth(String login, String password) {
        return users.stream().filter(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()))
                .findFirst().get();
    }
}
