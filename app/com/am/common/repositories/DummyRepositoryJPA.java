package com.am.common.repositories;

import com.am.common.models.Dummy;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

public class DummyRepositoryJPA extends BaseRepositoryJPA<Long, Dummy> implements DummyRepository {

    @Inject
    public DummyRepositoryJPA(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, Dummy.class);
    }
}
