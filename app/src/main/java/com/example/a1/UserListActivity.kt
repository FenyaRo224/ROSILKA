package com.example.a1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.SupabaseClient.getUserInfoList

class UserListActivity : AppCompatActivity() {
    private var listViewUsers: ListView? = null
    private var btnBack: Button? = null
    private var adapter: ArrayAdapter<String>? = null
    private var userInfoList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        listViewUsers = findViewById(R.id.listViewUsers)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })

        // Загрузка пользователей из Supabase (через статический метод для Java)
        Thread {
            userInfoList = getUserInfoList()
            runOnUiThread {
                if (userInfoList!!.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Нет пользователей или ошибка загрузки",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        userInfoList!!
                    )
                listViewUsers.setAdapter(adapter)
            }
        }.start()
    }
}