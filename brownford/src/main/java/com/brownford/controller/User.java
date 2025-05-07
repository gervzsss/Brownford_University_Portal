package com.brownford.controller;

public class User {
    private String password;
    private String role;

    public User(String password, String role) {
        this.password = password;
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
