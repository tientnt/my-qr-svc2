package com.am.qr.v3.services;

import com.am.qr.v3.repositories.CodeRepository;

import javax.inject.Inject;
import java.util.List;

public class CodeServiceImpl implements CodeService {

    @Inject
    CodeRepository codeRepository;

    @Override
    public boolean importHashes(String svc, List<String> codes) {
        return codeRepository.importHashes(svc, codes);
    }

    @Override
    public String findServiceByCode(String code) {
        return codeRepository.findServiceByCode(code);
    }
}
