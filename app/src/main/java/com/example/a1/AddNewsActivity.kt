package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

// Убедись, что имя пакета правильное

class AddNewsActivity : AppCompatActivity() {
    var etNewsTitle: EditText? = null
    var spinnerAgency: Spinner? = null
    var spinnerCategory: Spinner? = null
    var btnAddAgency: Button? = null
    var btnAddCategory: Button? = null
    var btnBack: Button? = null
    var btnContinue: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_news)

        etNewsTitle = findViewById(R.id.etNewsTitle)
        spinnerAgency = findViewById(R.id.spinnerAgency)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddAgency = findViewById(R.id.btnAddAgency)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnBack = findViewById(R.id.btnBack)
        btnContinue = findViewById(R.id.btnContinue)

        // Заполнение спиннеров фиктивными данными (можно заменить на реальные из базы)
        spinnerAgency.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Выберите агентство")
            )
        )
        spinnerCategory.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf("Выберите категорию")
            )
        )

        btnAddAgency.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    AddAgencyActivity::class.java
                )
            )
        })
        btnAddCategory.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    AddCategoryActivity::class.java
                )
            )
        })
        btnBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        btnContinue.setOnClickListener(View.OnClickListener { v: View? ->
            // Передача данных на экран редактирования
            val intent = Intent(
                this,
                EditNewsActivity::class.java
            )
            intent.putExtra("title", etNewsTitle.getText().toString())
            intent.putExtra("agency", spinnerAgency.getSelectedItem().toString())
            intent.putExtra("category", spinnerCategory.getSelectedItem().toString())
            startActivity(intent)
        })
    }
}
