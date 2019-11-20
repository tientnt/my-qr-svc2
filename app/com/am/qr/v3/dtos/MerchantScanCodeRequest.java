package com.am.qr.v3.dtos;

import play.data.validation.Constraints;

public class MerchantScanCodeRequest {
    private String code;

    private String type;

    private String svc;

    private MerchantScanCodeDetails data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSvc() {
        return svc;
    }

    public void setSvc(String svc) {
        this.svc = svc;
    }

    public MerchantScanCodeDetails getData() {
        return data;
    }

    public void setData(MerchantScanCodeDetails data) {
        this.data = data;
    }
}
