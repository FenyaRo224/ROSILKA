package com.example.a1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddAgencyActivity : AppCompatActivity() {
    var etNewAgency: EditText? = null
    var btnSaveAgency: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_agency)

        etNewAgency = findViewById(R.id.etNewAgency)
        btnSaveAgency = findViewById(R.id.btnSaveAgency)

        btnSaveAgency.setOnClickListener(View.OnClickListener { v: View? ->
            // Здесь можно добавить в базу данных
            Toast.makeText(this, "Агентство добавлено", Toast.LENGTH_SHORT).show()
            finish()
        })
    }
}
