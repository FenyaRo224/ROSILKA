package com.example.a1

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddCategoryActivity : Activity() {
    private var editTextCategoryName: EditText? = null
    private var btnSaveCategory: Button? = null
    private var btnBack: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        editTextCategoryName = findViewById(R.id.editTextCategoryName)
        btnSaveCategory = findViewById(R.id.btnSaveCategory)
        btnBack = findViewById(R.id.btnBack)

        btnSaveCategory.setOnClickListener(View.OnClickListener { v: View? ->
            val categoryName = editTextCategoryName.getText().toString().trim { it <= ' ' }
            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Введите название категории", Toast.LENGTH_SHORT).show()
            } else {
                // Здесь будет логика сохранения (например, в базу данных или отправка на сервер)
                Toast.makeText(this, "Категория '$categoryName' добавлена", Toast.LENGTH_SHORT)
                    .show()
                finish() // Закрываем активность
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })
    }
}
