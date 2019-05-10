package com.am.common.repositories;

import com.am.common.models.Dummy;
import com.google.inject.ImplementedBy;

@ImplementedBy(DummyRepositoryJPA.class)
public interface DummyRepository extends BaseRepository<Long, Dummy> {

}
