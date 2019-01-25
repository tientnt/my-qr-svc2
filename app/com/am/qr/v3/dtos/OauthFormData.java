package com.am.qr.v3.dtos;

public class OauthFormData {

    public static final String[] ALLOWED_FIELDS = {"username", "password", "refreshToken", "email", "clientId", "regId"};

    private String username;

    private String password;

    private String refreshToken;

    private String email;

    private String clientId;

    private String firstName;

    private String lastName;

    private String regId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public OauthFormData() {
    }

    public OauthFormData(String username, String password, String email, String clientId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.clientId = clientId;
    }

}
