package com.am.qr.v3.dtos;

import play.data.validation.Constraints;

import javax.validation.Constraint;

public class CodeRequest {
    public static final String[] ALLOWED_FIELDS = {"code", "type", "svc"};

    @Constraints.Required(message = "code is required.")
    private String code;

    @Constraints.Required(message = "type is required.")
    private String type;

    @Constraints.Required(message = "svc is required.")
    private String svc;

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
}
