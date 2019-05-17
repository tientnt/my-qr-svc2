package com.am.qr.v3.repositories;

import com.am.qr.v3.models.Route;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(CodeRepositoryJPA.class)
public interface CodeRepository {

    String findServiceByCode(String code);

    boolean importHashes(String svc, List<String> hashes);

    Route findByCode(String code);
}
