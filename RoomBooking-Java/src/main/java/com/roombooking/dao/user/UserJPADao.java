package com.roombooking.dao.user;

import com.roombooking.dao.AbstractDao;
import com.roombooking.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserJPADao extends AbstractDao<User> implements UserDao {

    protected UserJPADao() {
        super(User.class);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLoginPassword(String login, String password) {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = builder.createQuery(this.entityClass);

        Root<User> root = criteriaQuery.from(this.entityClass);
        criteriaQuery.where(builder.equal(root.get("login"), login), builder.equal(root.get("password"), password));

        TypedQuery<User> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        List<User> users = typedQuery.getResultList();

        if (users.isEmpty()) {
            return null;
        }
        return users.iterator().next();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> users = findAll();
        if (!users.isEmpty()) {
            for (User user : users) {
                cleanUnnecessaryFields(user);
            }
        }
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        User user = findById(id);
        if (user != null) {
            cleanUnnecessaryFields(user);
            // user.setPassword("");  //todo for admin
        }
        return user;
    }

    private void cleanUnnecessaryFields(User user) {
        user.setBookings(null);
        user.getRole().setUsers(null);
        user.getPosition().setRights(null);
        user.getPosition().setUsers(null);
    }

}
