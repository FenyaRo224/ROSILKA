package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainPageActivity : AppCompatActivity() {
    var btnSendNewsletter: Button? = null
    var btnViewNews: Button? = null
    var btnAddNews: Button? = null
    var btnUserList: Button? = null
    var btnBackToLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        btnSendNewsletter = findViewById(R.id.btnSendNewsletter)
        btnViewNews = findViewById(R.id.btnViewNews)
        btnAddNews = findViewById(R.id.btnAddNews)
        btnUserList = findViewById(R.id.btnUserList)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)

        btnSendNewsletter.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainPageActivity,
                    SendNewsletterActivity::class.java
                )
            )
        }
        )

        btnViewNews.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainPageActivity,
                    NewsActivity::class.java
                )
            )
        }
        )

        btnAddNews.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainPageActivity,
                    AddNewsActivity::class.java
                )
            )
        }
        )

        btnUserList.setOnClickListener(View.OnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainPageActivity,
                    UserListActivity::class.java
                )
            )
        }
        )

        btnBackToLogin.setOnClickListener(View.OnClickListener { v: View? -> finish() })
    }
}