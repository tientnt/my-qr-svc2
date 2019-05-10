package com.am.common.repositories;

import java.util.List;

public interface BaseRepository<K, O> {
    O find(K id);

    void insert(O o);

    O save(O o);

    void remove(O o);

    void removeById(K id);

    //for generic Entity type
    <T> T findEntityById(Class<T> clazz, K id);

    <T> T findEntityByColumn(Class<T> clazz, String attributeName, String attributeValue);

    <T> List<T> findListEntityByColumn(Class<T> clazz, String attributeName, String attributeValue);

    void insertEntity(Object obj);

    <T> T saveEntity(T entity);

    void removeEntity(Object obj);

    <T> void removeEntityById(Class<T> clazz, K id);

    <T> List<T> fetchAll(Class<T> clazz);

    int updateByQuery(String sql);
}
