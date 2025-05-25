package com.example.a1

import android.os.Bundle
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullNewsActivity : AppCompatActivity() {

    private lateinit var webViewContent: WebView
    private lateinit var tvTitle: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvAgency: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_news)

        webViewContent = findViewById(R.id.webViewContent)
        tvTitle = findViewById(R.id.tvTitle)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvAgency = findViewById(R.id.tvAgency)
        tvCategory = findViewById(R.id.tvCategory)
        tvDate = findViewById(R.id.tvDate)

        val articleId = intent.getLongExtra("articleid", -1)
        if (articleId != -1L) {
            CoroutineScope(Dispatchers.Main).launch {
                val article = withContext(Dispatchers.IO) {
                    SupabaseClient.fetchNewsArticleById(articleId)
                }
                if (article != null) {
                    tvTitle.text = article.title
                    webViewContent.loadDataWithBaseURL(null, article.content, "text/html", "utf-8", null)
                    tvDate.text = article.createdate ?: ""

                    // Получаем имена пользователя, агентства, категории
                    val userName = withContext(Dispatchers.IO) {
                        article.userid?.let { SupabaseClient.fetchUserNameById(it) } ?: "Неизвестно"
                    }
                    val agencyName = withContext(Dispatchers.IO) {
                        article.agencyid?.let { SupabaseClient.fetchAgencyNameById(it) } ?: "Неизвестно"
                    }
                    val categoryName = withContext(Dispatchers.IO) {
                        article.categoryid?.let { SupabaseClient.fetchCategoryNameById(it) } ?: "Неизвестно"
                    }

                    tvAuthor.text = "Автор: $userName"
                    tvAgency.text = "Агентство: $agencyName"
                    tvCategory.text = "Категория: $categoryName"
                }
            }
        }
    }
}