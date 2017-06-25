package com.example.pappu_pc.testchat.model;

import java.io.Serializable;

/**
 * Created by pappu-pc on 6/11/2017.
 */

public class User implements Serializable {
    public String getName() {
        return name;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(String name, String email) {

        this.name = name;
        this.email = email;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String name,email,token;
}
