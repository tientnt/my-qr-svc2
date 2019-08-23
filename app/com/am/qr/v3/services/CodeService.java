package com.am.qr.v3.services;

import com.am.qr.v3.models.Route;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(CodeServiceImpl.class)
public interface CodeService {
    boolean importHashes(String svc, List<String> codes);

    String findServiceByCode(String code);

    Route findByCode(String code);

    Route findByCodeAndSvc(String code, String svc);
}
