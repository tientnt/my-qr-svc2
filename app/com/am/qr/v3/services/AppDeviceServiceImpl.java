package com.am.qr.v3.services;

import com.am.qr.v3.models.Client;
import com.am.qr.v3.repositories.AppDeviceRepository;

import javax.inject.Inject;

public class AppDeviceServiceImpl implements AppDeviceService {

    @Inject
    AppDeviceRepository appDeviceRepository;

    @Override
    public Client findClientByKey(String key) {
        return appDeviceRepository.findClientByKey(key);
    }
}
