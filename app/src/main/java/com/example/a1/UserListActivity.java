package com.example.a1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ListView listViewUsers;
    Button btnBack;
    ArrayList<UserItem> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUsers = findViewById(R.id.listViewUsers);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Пример пользователей (позже можно заменить загрузкой из базы)
        userList = new ArrayList<>();
        userList.add(new UserItem("admin@example.com", "Администратор"));
        userList.add(new UserItem("user1@mail.com", "Пользователь"));
        userList.add(new UserItem("moderator@mail.com", "Модератор"));

        // Устанавливаем адаптер
        UserAdapter adapter = new UserAdapter(this, userList);
        listViewUsers.setAdapter(adapter);
    }
}
