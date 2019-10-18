package com.am.common.utils;

/**
 * Created by: billnguyen
 * at: 2019-03-10 11:40
 * <p>
 * AppliedMesh Pte. Ltd.
 */
public enum Service {
    DOOR_ACCESS("door-access"),
    EVOUCHER("evoucher"),
    WWPM("wwpm"),
    HPB("hpb"),
    UNKNOWN("unknown");

    private String serviceName;

    Service(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static Service fromServiceName(String serviceName) {
        for (Service svc : Service.values()) {
            if (svc.getServiceName().equals(serviceName)) {
                return svc;
            }
        }
        return UNKNOWN;
    }

}
