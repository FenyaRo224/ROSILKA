package com.example.a1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import jp.wasabeef.richeditor.RichEditor;

public class EditNewsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1001;

    private RichEditor editor;
    private Button btnBold, btnItalic, btnFontSize, btnColor, btnInsertImage, btnBack, btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news);

        editor = findViewById(R.id.editor);
        btnBold = findViewById(R.id.btnBold);
        btnItalic = findViewById(R.id.btnItalic);
        btnFontSize = findViewById(R.id.btnFontSize);
        btnColor = findViewById(R.id.btnColor);
        btnInsertImage = findViewById(R.id.btnInsertImage);
        btnBack = findViewById(R.id.btnBack);
        btnPublish = findViewById(R.id.btnPublish);

        editor.setEditorHeight(200);
        editor.setEditorFontSize(16);
        editor.setPlaceholder("Введите контент новости...");

        btnBold.setOnClickListener(v -> editor.setBold());
        btnItalic.setOnClickListener(v -> editor.setItalic());
        btnFontSize.setOnClickListener(v -> editor.setFontSize(24)); // изменить размер
        btnColor.setOnClickListener(v -> editor.setTextColor(Color.BLUE)); // изменить цвет

        btnInsertImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        btnBack.setOnClickListener(v -> finish());

        btnPublish.setOnClickListener(v -> {
            String htmlContent = editor.getHtml();

            // Получение данных из предыдущей активности
            String title = getIntent().getStringExtra("title");
            String agency = getIntent().getStringExtra("agency");
            String category = getIntent().getStringExtra("category");

            if (title == null || title.isEmpty() || htmlContent == null || htmlContent.isEmpty()) {
                Toast.makeText(this, "Введите заголовок и контент", Toast.LENGTH_SHORT).show();
                return;
            }

            // Сохранение новости
            NewsItem newItem = new NewsItem(title, htmlContent, agency, category);
            NewsStorage.addNews(newItem);

            Toast.makeText(this, "Новость опубликована", Toast.LENGTH_SHORT).show();

            // Переход на список новостей
            Intent intent = new Intent(this, NewsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                editor.insertImage(imageUri.toString(), "вставленное изображение");
            }
        }
    }
}
