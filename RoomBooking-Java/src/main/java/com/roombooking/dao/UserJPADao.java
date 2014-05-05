package com.roombooking.dao;

import com.roombooking.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserJPADao extends AbstractDao<User> implements UserDao {

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

}
