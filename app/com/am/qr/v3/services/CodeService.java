package com.am.qr.v3.services;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(CodeServiceImpl.class)
public interface CodeService {
    boolean importHashes(String svc, List<String> codes);
}
