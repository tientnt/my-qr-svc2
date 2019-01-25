package com.am.qr.v3.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.Logger;

import java.io.IOException;

public class Response {

    private static Logger.ALogger logger = Logger.of(Response.class);

    private int code;
    private String message;
    private JsonNode data;
    private JsonNode errors;

    public Response(int code, String message, Object data, Object errors) {
        this.code = code;
        this.message = message;
        if(data instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.data = mapper.readTree(data.toString());
            } catch (IOException ex) {
                logger.error("invalid data json format {}", ex.getMessage());
                this.data = null;
            }
        } else {
            this.data = (JsonNode) data;
        }
        if(errors instanceof String) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                this.errors = mapper.readTree(errors.toString());
            } catch (IOException ex) {
                logger.error("invalid errors json format {}", ex.getMessage());
                this.errors = null;
            }
        } else {
            this.errors = (JsonNode) errors;
        }
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
}
