package com.roombooking.dao.test;

import com.roombooking.dao.UserDao;
import com.roombooking.entity.Role;
import com.roombooking.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserStubDao implements UserDao {

    private List<User> users;
    private List<Role> roles;

    public UserStubDao() {
        users = new ArrayList<>();
        roles = new ArrayList<>();
        Role roleAdimn = new Role();
        roleAdimn.setId(1);
        roleAdimn.setTitle("Admin");
        Role roleUser = new Role();
        roleUser.setId(0);
        roleUser.setTitle("User");

        User user = new User(1, "login", "passw", "Igor", "Melentev");
        user.setRole(roleAdimn);
        users.add(user);
        user = new User(2, "login2", "passw2", "Sergey", "March");
        user.setRole(roleUser);
        users.add(user);
        user = new User(3, "Login3", "passw4", "Andrey", "March");
        user.setRole(roleAdimn);
        users.add(user);

        Role role = new Role();
        role.setId(55);
        role.setTitle("Admins");
        role.setUsers(Arrays.asList(new User(8, "asd", "asdas", "asdasd", "asd"), new User(9, "w", "w",
                "w", "w")));
        roles.add(role);
    }

    public Role getRole(int id) {
        return roles.get(id);
    }

    @Override
    public User findByLoginPassword(String login, String password) {
        return users.stream().filter(user -> login.equals(user.getLogin()) && password.equals(user.getPassword()))
                .findFirst().orElse(null);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void save(User entity) {

    }

    @Override
    public void deleteById(int id) {

    }
}
