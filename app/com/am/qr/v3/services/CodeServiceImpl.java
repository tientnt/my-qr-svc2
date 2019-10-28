package com.am.qr.v3.services;

import com.am.qr.v3.models.Route;
import com.am.qr.v3.repositories.CodeRepository;

import javax.inject.Inject;
import java.util.List;

public class CodeServiceImpl implements CodeService {

    @Inject
    CodeRepository codeRepository;

    @Override
    public boolean importHashes(String svc, List<String> codes, String status) {
        return codeRepository.importHashes(svc, codes, status);
    }

    @Override
    public String findServiceByCode(String code) {
        return codeRepository.findServiceByCode(code);
    }

    @Override
    public Route findByCode(String code) {
        return codeRepository.findByCode(code);
    }

    @Override
    public List<Route> findListByCode(String code) {
        return codeRepository.findListByCode(code);
    }

    @Override
    public Route findByCodeAndSvc(String code, String svc) {
        return codeRepository.findByCodeAndSvc(code, svc);
    }
}
