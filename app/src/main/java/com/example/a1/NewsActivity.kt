package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.SupabaseClient.getNewsTitles

class NewsActivity : AppCompatActivity() {
    private var listViewNews: ListView? = null
    private var btnBack: Button? = null
    private var newsTitles: ArrayList<String>? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        listViewNews = findViewById(R.id.listViewNews)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })

        // Получение списка новостей из Supabase
        Thread {
            newsTitles = getNewsTitles()
            // Обновление UI — только в главном потоке!
            runOnUiThread {
                adapter =
                    ArrayAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        newsTitles!!
                    )
                listViewNews.setAdapter(adapter)
                listViewNews.setOnItemClickListener(OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    val intent = Intent(
                        this@NewsActivity,
                        FullNewsActivity::class.java
                    )
                    intent.putExtra("newsTitle", newsTitles!![position])
                    startActivity(intent)
                })
            }
        }.start()
    }
}