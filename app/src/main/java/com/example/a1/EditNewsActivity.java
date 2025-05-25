package com.example.a1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
            Long agencyid = getIntent().hasExtra("agencyid") ? getIntent().getLongExtra("agencyid", 0) : null;
            Long categoryid = getIntent().hasExtra("categoryid") ? getIntent().getLongExtra("categoryid", 0) : null;
            Long userid = getIntent().hasExtra("userid") ? getIntent().getLongExtra("userid", 0) : null;

            if (title == null || title.isEmpty() || htmlContent == null || htmlContent.isEmpty()) {
                Toast.makeText(this, "Введите заголовок и контент", Toast.LENGTH_SHORT).show();
                return;
            }

            // Асинхронная публикация новости
            new PublishNewsTask(title, htmlContent, userid, agencyid, categoryid).execute();
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

    // AsyncTask для публикации новости
    private class PublishNewsTask extends AsyncTask<Void, Void, Boolean> {
        private final String title;
        private final String content;
        private final Long userid, agencyid, categoryid;
        private String errorMsg = "";

        PublishNewsTask(String title, String content, Long userid, Long agencyid, Long categoryid) {
            this.title = title;
            this.content = content;
            this.userid = userid;
            this.agencyid = agencyid;
            this.categoryid = categoryid;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Создаём объект новости (NewsArticle) и отправляем через Kotlin SupabaseClient
                SupabaseClient.NewsArticle article = new SupabaseClient.NewsArticle(
                        0L, // articleid (автоинкремент)
                        title,
                        content,
                        userid,
                        agencyid,
                        categoryid,
                        null // createdate
                );
                SupabaseClient.publishNewsArticleSync(article);
                return true;
            } catch (Exception e) {
                errorMsg = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(EditNewsActivity.this, "Новость опубликована", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditNewsActivity.this, NewsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(EditNewsActivity.this, "Ошибка публикации: " + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}