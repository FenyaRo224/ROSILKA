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
    private ArrayList<String> newsTitles;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listViewNews = findViewById(R.id.listViewNews);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Получение списка новостей из Supabase
        new Thread(() -> {
            newsTitles = SupabaseClient.getNewsTitles();

            // Обновление UI — только в главном потоке!
            runOnUiThread(() -> {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newsTitles);
                listViewNews.setAdapter(adapter);

                listViewNews.setOnItemClickListener((parent, view, position, id) -> {
                    Intent intent = new Intent(NewsActivity.this, FullNewsActivity.class);
                    intent.putExtra("newsTitle", newsTitles.get(position));
                    startActivity(intent);
                });
            });
        }).start();
    }
}