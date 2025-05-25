package com.example.a1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private ListView listViewUsers;
    private Button btnBack;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUsers = findViewById(R.id.listViewUsers);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Загрузка пользователей из Supabase (через статический метод для Java)
        new Thread(() -> {
            userInfoList = SupabaseClient.getUserInfoList();
            runOnUiThread(() -> {
                if (userInfoList.isEmpty()) {
                    Toast.makeText(this, "Нет пользователей или ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userInfoList);
                listViewUsers.setAdapter(adapter);
            });
        }).start();
    }
}