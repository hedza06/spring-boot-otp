package com.starter.springboot.security.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JWTToken {

    private String idToken;

    public JWTToken(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
