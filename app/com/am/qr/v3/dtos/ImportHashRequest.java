package com.am.qr.v3.dtos;

import play.data.validation.Constraints;

import java.util.List;

public class ImportHashRequest {

    public static final String[] ALLOWED_FIELDS = {"hashes", "svc", "status"};

    @Constraints.Required(message = "hashes is required.")
    private List<String> hashes;

    @Constraints.Required(message = "svc is required.")
    private String svc;

    private String status;

    public List<String> getHashes() {
        return hashes;
    }

    public void setHashes(List<String> hashes) {
        this.hashes = hashes;
    }

    public String getSvc() {
        return svc;
    }

    public void setSvc(String svc) {
        this.svc = svc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
