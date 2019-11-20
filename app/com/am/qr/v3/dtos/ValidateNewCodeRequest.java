package com.am.qr.v3.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import play.data.validation.Constraints;

public class ValidateNewCodeRequest {
    public static final String[] ALLOWED_FIELDS = {"code", "svc"};

    @Constraints.Required(message = "code is required.")
    private String code;

    @Constraints.Required(message = "svc is required.")
    private String svc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSvc() {
        return svc;
    }

    public void setSvc(String svc) {
        this.svc = svc;
    }
}
