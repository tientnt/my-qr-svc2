package com.am.qr.v3.repositories;

import com.am.qr.v3.models.Client;
import com.am.qr.v3.models.Device;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.List;

public class AppDeviceRepositoryJPA extends BaseRepositoryJPA<Long, Device> implements AppDeviceRepository {

    @Inject
    public AppDeviceRepositoryJPA(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, Device.class);
    }

    @Override
    public Client findClientByKey(String key) {
        return jpaApi.withTransaction(em -> {
            List<Client> results = em.createQuery("from Client a where a.key = :key", Client.class)
                                     .setParameter("key", key)
                                     .getResultList();
            if (results.size() > 0) {
                return results.get(0);
            }
            return null;
        });
    }
}
