package com.example.a1;

import java.util.ArrayList;

public class UserStorage {
    private static ArrayList<String> emails = new ArrayList<>();

    public static void addEmail(String email) {
        emails.add(email);
    }

    public static ArrayList<String> getAllEmails() {
        return emails;
    }
}
