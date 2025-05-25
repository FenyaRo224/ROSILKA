package com.example.a1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendNewsletterActivity : AppCompatActivity() {

    private lateinit var spinnerNews: Spinner
    private lateinit var listViewEmails: ListView
    private lateinit var btnSend: Button
    private lateinit var btnBack: Button

    private var newsList: List<SupabaseClient.NewsArticle> = emptyList()
    private var emailList: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_newsletter)

        spinnerNews = findViewById(R.id.spinnerNews)
        listViewEmails = findViewById(R.id.listViewEmails)
        btnSend = findViewById(R.id.btnSend)
        btnBack = findViewById(R.id.btnBack)

        CoroutineScope(Dispatchers.Main).launch {
            // 1. Получаем список новостей из Supabase
            newsList = withContext(Dispatchers.IO) {
                SupabaseClient.fetchNewsArticles()
            }
            val newsTitles = newsList.map { it.title }
            val newsAdapter = ArrayAdapter(this@SendNewsletterActivity, android.R.layout.simple_spinner_item, newsTitles)
            newsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerNews.adapter = newsAdapter

            // 2. Получаем список email пользователей из Supabase
            emailList = withContext(Dispatchers.IO) {
                SupabaseClient.fetchAllUserEmails()
            }
            val emailAdapter = ArrayAdapter(this@SendNewsletterActivity, android.R.layout.simple_list_item_multiple_choice, emailList)
            listViewEmails.adapter = emailAdapter
            listViewEmails.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        }

        btnSend.setOnClickListener {
            val selectedNewsIndex = spinnerNews.selectedItemPosition
            if (selectedNewsIndex == AdapterView.INVALID_POSITION || newsList.isEmpty()) {
                Toast.makeText(this, "Выберите новость!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedNews = newsList[selectedNewsIndex]

            val selectedEmails = mutableListOf<String>()
            for (i in emailList.indices) {
                if (listViewEmails.isItemChecked(i)) {
                    selectedEmails.add(emailList[i])
                }
            }
            if (selectedEmails.isEmpty()) {
                Toast.makeText(this, "Выберите хотя бы один email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendEmail(selectedEmails, selectedNews.title, stripHtml(selectedNews.content))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun sendEmail(recipients: List<String>, subject: String, body: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Только email-приложения
            putExtra(Intent.EXTRA_EMAIL, recipients.toTypedArray())
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(emailIntent, "Выберите почтовое приложение"))
        } else {
            Toast.makeText(this, "Нет подходящего почтового приложения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stripHtml(html: String): String {
        return android.text.Html.fromHtml(html, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
    }
}