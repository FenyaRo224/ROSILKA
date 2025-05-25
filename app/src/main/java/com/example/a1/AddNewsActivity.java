package com.example.a1; // Убедись, что имя пакета правильное

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewsActivity extends AppCompatActivity {

    EditText etNewsTitle;
    Spinner spinnerAgency, spinnerCategory;
    Button btnAddAgency, btnAddCategory, btnBack, btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        etNewsTitle = findViewById(R.id.etNewsTitle);
        spinnerAgency = findViewById(R.id.spinnerAgency);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnAddAgency = findViewById(R.id.btnAddAgency);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        btnBack = findViewById(R.id.btnBack);
        btnContinue = findViewById(R.id.btnContinue);

        // Заполнение спиннеров фиктивными данными (можно заменить на реальные из базы)
        spinnerAgency.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Выберите агентство"}));
        spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Выберите категорию"}));

        btnAddAgency.setOnClickListener(v -> startActivity(new Intent(this, AddAgencyActivity.class)));
        btnAddCategory.setOnClickListener(v -> startActivity(new Intent(this, AddCategoryActivity.class)));
        btnBack.setOnClickListener(v -> finish());
        btnContinue.setOnClickListener(v -> {
            // Передача данных на экран редактирования
            Intent intent = new Intent(this, EditNewsActivity.class);
            intent.putExtra("title", etNewsTitle.getText().toString());
            intent.putExtra("agency", spinnerAgency.getSelectedItem().toString());
            intent.putExtra("category", spinnerCategory.getSelectedItem().toString());
            startActivity(intent);
        });
    }
}
