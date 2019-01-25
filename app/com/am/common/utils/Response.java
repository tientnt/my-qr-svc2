package com.am.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpStatus;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class Response {

    private int code;

    private String message;

    private JsonNode data;

    private JsonNode errors;

    public Response(int code, String message, Object data, Object errors) {
        this.code = code;
        this.message = message;
        this.data = AMObjectMapper.toJsonNode(data, false);
        this.errors = AMObjectMapper.toJsonNode(errors, false);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

    public JsonNode getErrors() {
        return errors;
    }

    public void setErrors(JsonNode errors) {
        this.errors = errors;
    }

    private static JsonNode badRequestValue(String message, Object errors) {
        return AMObjectMapper.toJsonNode(new Response(HttpStatus.SC_BAD_REQUEST, message, null, errors), false);
    }

    private static JsonNode unauthorizedValue(String message, Object errors) {
        return AMObjectMapper.toJsonNode(new Response(HttpStatus.SC_UNAUTHORIZED, message, null, errors), false);
    }

    private static JsonNode successValue(String message, Object data) {
        return AMObjectMapper.toJsonNode(new Response(HttpStatus.SC_OK, message, data, null), false);
    }

    private static JsonNode responseValue(int code, String message, Object data, Object errors) {
        return AMObjectMapper.toJsonNode(new Response(code, message, data, errors), false);
    }

    public static Result badRequest(String message, Object errors) {
        return Results.badRequest(badRequestValue(message, errors));
    }

    public static Result unauthorized(String message, Object errors) {
        return Results.unauthorized(unauthorizedValue(message, errors));
    }

    public static Result success(String message, Object data) {
        return Results.ok(successValue(message, data));
    }

    public static Result response(int code, String message, Object data, Object errors) {
        if (code == 200) {
            return Results.ok(responseValue(code, message, data, errors));
        }
        return Results.badRequest(responseValue(code, message, data, errors));
    }

    public static CompletionStage<Result> asyncBadRequest(String message, Object errors) {
        return CompletableFuture.completedFuture(Results.badRequest(badRequestValue(message, errors)));
    }

    public static CompletionStage<Result> asyncUnauthorized(String message, Object errors) {
        return CompletableFuture.completedFuture(Results.unauthorized(unauthorizedValue(message, errors)));
    }

}
