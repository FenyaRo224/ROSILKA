package com.example.a1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    private ListView listViewNews;
    private Button btnBack;

    private ArrayList<NewsItem> newsList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> newsTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listViewNews = findViewById(R.id.listViewNews);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Получаем список новостей из хранилища
        newsList = NewsStorage.getNewsList();
        newsTitles = new ArrayList<>();

        // Заполняем список заголовков для отображения
        for (NewsItem item : newsList) {
            newsTitles.add(item.getTitle());
        }

        // Устанавливаем адаптер для ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newsTitles);
        listViewNews.setAdapter(adapter);

        // Обработка нажатий по элементам списка
        listViewNews.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(NewsActivity.this, FullNewsActivity.class);
            intent.putExtra("news", newsList.get(position)); // newsList.get(position) должен быть Serializable
            startActivity(intent);
        });
    }
}
