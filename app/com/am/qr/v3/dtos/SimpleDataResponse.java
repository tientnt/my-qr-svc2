package com.am.qr.v3.dtos;

import com.am.common.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleDataResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;

    public SimpleDataResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
