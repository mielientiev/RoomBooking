package com.roombooking.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class AbstractDao<T> implements Dao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    protected Class<T> entityClass;

    protected AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    protected AbstractDao() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        final CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = builder.createQuery(this.entityClass);

        criteriaQuery.from(this.entityClass);

        TypedQuery<T> typedQuery = this.getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public T update(T entity) {
        return this.getEntityManager().merge(entity);
    }

    @Override
    @Transactional
    public void save(T entity) {
        this.getEntityManager().persist(entity);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        T entity = this.findById(id);
        if (entity == null) {
            return;
        }
        this.getEntityManager().remove(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T findById(int id) {
        return this.getEntityManager().find(this.entityClass, id);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
