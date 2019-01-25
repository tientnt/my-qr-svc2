package com.am.common.utils;

import java.util.Map;

public class SvcResult<T> {

    public enum Status {
        SUCCESS,
        ERROR
    }

    public static String ERROR_CODE = "error_code";

    private Status status;

    private String message;

    private T data;

    private Map<String, String> errors;

    public SvcResult() {
    }

    public SvcResult(T data) {
        this.data = data;
    }

    public SvcResult(T data, Map<String, String> errors) {
        this.data = data;
        this.errors = errors;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
