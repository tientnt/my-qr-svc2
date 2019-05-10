package com.am.common.repositories;

import play.Logger;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.Function;

public class BaseRepositoryJPA<K, O> implements BaseRepository<K, O> {

    protected final JPAApi jpaApi;

    protected final DatabaseExecutionContext executionContext;

    protected Logger.ALogger logger = Logger.of(BaseRepositoryJPA.class);

    protected Class<O> objectType;

    public BaseRepositoryJPA(JPAApi jpaApi, DatabaseExecutionContext executionContext, Class<O> objectType) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
        this.objectType = objectType;
    }

    @Override
    public O find(K id) {
        return wrap(em -> em.find(objectType, id));
    }

    @Override
    public void insert(O o) {
        wrap(em -> {
            em.persist(o);
            return o;
        });
    }

    @Override
    public O save(O o) {
        return wrap(em -> em.merge(o));
    }

    @Override
    public void remove(O o) {
        wrap(em -> {
            em.remove(o);
            return o;
        });
    }

    @Override
    public void removeById(K id) {
        O o = find(id);
        if (o != null) {
            wrap(em -> {
                em.remove(o);
                return o;
            });
        }
    }

    @Override
    public <T> T findEntityById(Class<T> clazz, K id) {
        return jpaApi.withTransaction(em -> em.find(clazz, id));
    }

    @Override
    public <T> T findEntityByColumn(Class<T> clazz, String attributeName, String attributeValue) {
        String entityName = clazz.getSimpleName();
        String sql = String.format("from %s where %s = :attributeValue", entityName, attributeName);
        return jpaApi.withTransaction(em -> {
            List<T> results = em.createQuery(sql, clazz)
                                .setParameter("attributeValue", attributeValue)
                                .getResultList();
            if (results.size() > 0) {
                //get latest row
                return results.get(results.size() - 1);
            }
            return null;
        });
    }

    @Override
    public <T> List<T> findListEntityByColumn(Class<T> clazz, String attributeName, String attributeValue) {
        String entityName = clazz.getSimpleName();
        String sql = String.format("from %s where %s = :attributeValue", entityName, attributeName);
        return jpaApi.withTransaction(em -> {
            List<T> results = em.createQuery(sql, clazz)
                                .setParameter("attributeValue", attributeValue)
                                .getResultList();
            return results;
        });
    }

    @Override
    public void insertEntity(Object obj) {
        jpaApi.withTransaction(em -> {
            em.persist(obj);
            return obj;
        });
    }

    @Override
    public <T> T saveEntity(T entity) {
        return jpaApi.withTransaction(em -> em.merge(entity));
    }

    @Override
    public void removeEntity(Object obj) {
        jpaApi.withTransaction(em -> {
            em.remove(obj);
            return obj;
        });
    }

    @Override
    public <T> void removeEntityById(Class<T> clazz, K id) {
        //        T entity = findEntityById(clazz, id);
        //        removeEntity(entity);
        jpaApi.withTransaction(em -> {
            T entity = em.find(clazz, id);
            em.remove(entity);
            return entity;
        });
    }

    @Override
    public <T> List<T> fetchAll(Class<T> clazz) {
        return jpaApi.withTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);
            Root<T> root = cq.from(clazz);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        });
    }

    @Override
    public int updateByQuery(String sql) {
        return jpaApi.withTransaction(em -> em.createNativeQuery(sql).executeUpdate());
    }

    protected O wrap(Function<EntityManager, O> function) {
        return jpaApi.withTransaction(function);
    }
}
