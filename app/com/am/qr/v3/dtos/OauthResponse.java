package com.am.qr.v3.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class OauthResponse {
    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private JsonNode data;

    @JsonProperty("errors")
    private JsonNode errors;

    public int getCode() {
        return code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public JsonNode getData(){
        return this.data;
    }

    public void setData(JsonNode data){
        this.data = data;
    }

    public JsonNode getErrors(){
        return this.errors;
    }

    public void setErrors(JsonNode errors){
        this.errors = errors;
    }
}
