package com.example.a1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategoryActivity extends Activity {

    private EditText editTextCategoryName;
    private Button btnSaveCategory, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        btnSaveCategory = findViewById(R.id.btnSaveCategory);
        btnBack = findViewById(R.id.btnBack);

        btnSaveCategory.setOnClickListener(v -> {
            String categoryName = editTextCategoryName.getText().toString().trim();
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Введите название категории", Toast.LENGTH_SHORT).show();
            } else {
                // Здесь будет логика сохранения (например, в базу данных или отправка на сервер)
                Toast.makeText(this, "Категория '" + categoryName + "' добавлена", Toast.LENGTH_SHORT).show();
                finish(); // Закрываем активность
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
