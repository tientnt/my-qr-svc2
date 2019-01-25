package com.am.qr.v3.repositories;

import com.am.qr.v3.models.Client;
import com.am.qr.v3.models.Device;
import com.google.inject.ImplementedBy;

@ImplementedBy(AppDeviceRepositoryJPA.class)
public interface AppDeviceRepository extends BaseRepository<Long, Device> {

    Client findClientByKey(String key);
}
