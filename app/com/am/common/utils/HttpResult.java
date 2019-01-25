package com.am.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpStatus;
import play.mvc.Result;
import play.mvc.Results;

import java.util.Map;

public class HttpResult<T> {

    private int code;

    private String message;

    private T data;

    private Map<String, String> errors;

    private HttpResult(int code, String message, Object data, Object errors) {
        this.code = code;
        this.message = message;
    }

    public HttpResult(SvcResult<T> svcResult) {
        int code = HttpStatus.SC_OK;
        if (svcResult.getStatus() != SvcResult.Status.SUCCESS) {
            code = HttpStatus.SC_BAD_REQUEST; //default error code
            if (svcResult.getErrors() != null) {
                if (svcResult.getErrors().containsKey(SvcResult.ERROR_CODE)) {
                    code = Integer.parseInt(svcResult.getErrors().get(SvcResult.ERROR_CODE));
                    svcResult.getErrors().remove(SvcResult.ERROR_CODE);
                }
            }
        }
        this.code = code;
        this.message = svcResult.getMessage();
        this.data = svcResult.getData();
        this.errors = svcResult.getErrors();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public Result toResult() {
        JsonNode node = AMObjectMapper.toJsonNode(this, false);
        Result result;
        switch (this.code) {
            case HttpStatus.SC_OK:
                result = Results.ok(node);
                break;
            case HttpStatus.SC_UNAUTHORIZED:
                result = Results.unauthorized(node);
                break;
            case HttpStatus.SC_NOT_FOUND:
                result = Results.notFound(node);
                break;
            case HttpStatus.SC_FORBIDDEN:
                result = Results.forbidden(node);
                break;
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                result = Results.internalServerError(node);
                break;
            case HttpStatus.SC_NOT_ACCEPTABLE:
                result = Results.notAcceptable(node);
                break;
            default:
                result = Results.badRequest(node);
                break;
        }
        return result;
    }

    public static Result toResult(int code, String message, Object data, Object errors) {
        HttpResult<Object> httpResult = new HttpResult<>(code, message, data, errors);
        return httpResult.toResult();
    }
}
