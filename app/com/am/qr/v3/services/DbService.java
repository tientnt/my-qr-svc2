package com.am.qr.v3.services;

import com.am.qr.v3.repositories.DummyRepository;

import javax.inject.Inject;
import java.util.List;

public class DbService {

    private DummyRepository dummyRepository;

    @Inject
    public DbService(DummyRepository dummyRepository) {
        this.dummyRepository = dummyRepository;
    }

    public <T> T findById(Class<T> clazz, Long id) {
        return dummyRepository.findEntityById(clazz, id);
    }

    public void insertEntity(Object obj) {
        dummyRepository.insertEntity(obj);
    }

    public <T> T saveEntity(T entity) {
        return dummyRepository.saveEntity(entity);
    }

    public void removeEntity(Object obj) {
        dummyRepository.removeEntity(obj);
    }

    public <T> void removeEntityById(Class<T> clazz, Long id) {
        dummyRepository.removeEntityById(clazz, id);
    }

    public <T> List<T> fetchAll(Class<T> clazz) {
        return dummyRepository.fetchAll(clazz);
    }
}
