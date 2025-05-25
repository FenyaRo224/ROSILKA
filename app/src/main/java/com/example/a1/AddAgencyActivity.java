package com.example.a1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class AddAgencyActivity extends AppCompatActivity {

    EditText etNewAgency;
    Button btnSaveAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agency);

        etNewAgency = findViewById(R.id.etNewAgency);
        btnSaveAgency = findViewById(R.id.btnSaveAgency);

        btnSaveAgency.setOnClickListener(v -> {
            // Здесь можно добавить в базу данных
            Toast.makeText(this, "Агентство добавлено", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
