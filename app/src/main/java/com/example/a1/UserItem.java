package com.example.a1;

public class UserItem {
    private String email;
    private String status;

    public UserItem(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
