package com.am.qr.v3.repositories;

import com.am.qr.v3.models.Dummy;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class DummyRepositoryJPA extends BaseRepositoryJPA<Long, Dummy> implements DummyRepository {

    @Inject
    public DummyRepositoryJPA(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, Dummy.class);
    }
}
