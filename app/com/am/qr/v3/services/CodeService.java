package com.am.qr.v3.services;

import com.am.qr.v3.models.Route;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(CodeServiceImpl.class)
public interface CodeService {
    boolean importHashes(String svc, List<String> codes, String status, List<String> groups);

    String findServiceByCode(String code);

    Route findByCode(String code);

    List<Route> findListByCode(String code);

    Route findByCodeAndSvc(String code, String svc);

    Route findByCodeSvcGroup(String code, String svc, String group);
}
