package com.am.qr.v3.services;

import com.am.qr.v3.models.Client;
import com.google.inject.ImplementedBy;

@ImplementedBy(AppDeviceServiceImpl.class)
public interface AppDeviceService {
    Client findClientByKey(String key);
}
