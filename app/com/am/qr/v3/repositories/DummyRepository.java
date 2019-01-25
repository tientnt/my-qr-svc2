package com.am.qr.v3.repositories;

import com.am.qr.v3.models.Dummy;
import com.google.inject.ImplementedBy;

@ImplementedBy(DummyRepositoryJPA.class)
public interface DummyRepository extends BaseRepository<Long, Dummy> {

}
