package com.roombooking.dao.user;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

public class UserJPADao extends AbstractDao<User> implements UserDao {

    protected UserJPADao() {
        super(User.class);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLoginPassword(String login, String password) {
        TypedQuery<User> typedQuery = this.getEntityManager().createNamedQuery("User.findUserByLoginPassword", entityClass);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("password", password);
        List<User> users = typedQuery.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

}
