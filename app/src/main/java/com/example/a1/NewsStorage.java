package com.example.a1;

import java.util.ArrayList;

public class NewsStorage {
    private static ArrayList<NewsItem> newsList = new ArrayList<>();

    public static void addNews(NewsItem item) {
        newsList.add(item);
    }

    public static ArrayList<NewsItem> getNewsList() {
        return newsList;
    }
}
