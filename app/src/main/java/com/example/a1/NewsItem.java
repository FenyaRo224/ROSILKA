package com.example.a1;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsItem implements Serializable {
    private String title;
    private String content;
    private String agency;
    private String category;
    private String date;

    // Конструктор
    public NewsItem(String title, String content, String agency, String category) {
        this.title = title;
        this.content = content;
        this.agency = agency;
        this.category = category;
        this.date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
    }

    // Геттеры
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAgency() {
        return agency;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}
