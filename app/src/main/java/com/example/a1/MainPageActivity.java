package com.example.a1;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity {

    Button btnSendNewsletter, btnViewNews, btnAddNews, btnUserList, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        btnSendNewsletter = findViewById(R.id.btnSendNewsletter);
        btnViewNews = findViewById(R.id.btnViewNews);
        btnAddNews = findViewById(R.id.btnAddNews);
        btnUserList = findViewById(R.id.btnUserList);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnSendNewsletter.setOnClickListener(v ->
                startActivity(new Intent(MainPageActivity.this, SendNewsletterActivity.class))
        );

        btnViewNews.setOnClickListener(v ->
                startActivity(new Intent(MainPageActivity.this, NewsActivity.class))
        );

        btnAddNews.setOnClickListener(v ->
                startActivity(new Intent(MainPageActivity.this, AddNewsActivity.class))
        );

        btnUserList.setOnClickListener(v ->
                startActivity(new Intent(MainPageActivity.this, UserListActivity.class))
        );

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}