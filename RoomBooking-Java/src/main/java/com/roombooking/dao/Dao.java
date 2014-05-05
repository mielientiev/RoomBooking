package com.roombooking.dao;

import java.util.List;

public interface Dao<T> {

    List<T> findAll();

    T findById(int id);

    T update(T entity);

    void save(T entity);

    void deleteById(int id);

}
