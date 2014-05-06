package com.roombooking.dao.user;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserJPADao extends AbstractDao<User> implements UserDao {

    protected UserJPADao() {
        super(User.class);
    }

    @Transactional(readOnly = true)
    public User findByLoginPassword(String login, String password) {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

        Root<User> root = criteriaQuery.from(this.entityClass);
        Path<String> namePath = root.get("login");
        criteriaQuery.where(builder.equal(namePath, login));
        namePath = root.get("password");
        criteriaQuery.where(builder.equal(namePath, password));

        TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        List<User> users = typedQuery.getResultList();
        if (users.isEmpty()) {
            return null;
        }

        return users.iterator().next();
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> users = findAll();
        if (!users.isEmpty()) {
            for (User user : users) {
                user.setBookings(null);
                user.getRole().setUsers(null);
                user.getPosition().setRights(null);
                user.getPosition().setUsers(null);
                user.setPassword(null);
            }
        }
        return users;
    }

    @Transactional(readOnly = true)
    public User getUserById(int id) {
        User user = findById(id);
        if (user != null) {
            user.setBookings(null);
            user.getRole().setUsers(null);
            user.getPosition().setRights(null);
            user.getPosition().setUsers(null);
        }
        return user;
    }

}
