package com.example.a1

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.SupabaseClient.NewsArticle
import com.example.a1.SupabaseClient.publishNewsArticleSync
import jp.wasabeef.richeditor.RichEditor

class EditNewsActivity : AppCompatActivity() {
    private var editor: RichEditor? = null
    private var btnBold: Button? = null
    private var btnItalic: Button? = null
    private var btnFontSize: Button? = null
    private var btnColor: Button? = null
    private var btnInsertImage: Button? = null
    private var btnBack: Button? = null
    private var btnPublish: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_news)

        editor = findViewById(R.id.editor)
        btnBold = findViewById(R.id.btnBold)
        btnItalic = findViewById(R.id.btnItalic)
        btnFontSize = findViewById(R.id.btnFontSize)
        btnColor = findViewById(R.id.btnColor)
        btnInsertImage = findViewById(R.id.btnInsertImage)
        btnBack = findViewById(R.id.btnBack)
        btnPublish = findViewById(R.id.btnPublish)

        editor.setEditorHeight(200)
        editor.setEditorFontSize(16)
        editor.setPlaceholder("Введите контент новости...")

        btnBold.setOnClickListener(View.OnClickListener { v: View? -> editor.setBold() })
        btnItalic.setOnClickListener(View.OnClickListener { v: View? -> editor.setItalic() })
        btnFontSize.setOnClickListener(View.OnClickListener { v: View? -> editor.setFontSize(24) }) // изменить размер
        btnColor.setOnClickListener(View.OnClickListener { v: View? -> editor.setTextColor(Color.BLUE) }) // изменить цвет

        btnInsertImage.setOnClickListener(View.OnClickListener { v: View? ->
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        })

        btnBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })

        btnPublish.setOnClickListener(View.OnClickListener { v: View? ->
            val htmlContent = editor.getHtml()
            // Получение данных из предыдущей активности
            val title = intent.getStringExtra("title")
            val agencyid =
                if (intent.hasExtra("agencyid")) intent.getLongExtra("agencyid", 0) else null
            val categoryid =
                if (intent.hasExtra("categoryid")) intent.getLongExtra("categoryid", 0) else null
            val userid = if (intent.hasExtra("userid")) intent.getLongExtra("userid", 0) else null

            if (title == null || title.isEmpty() || htmlContent == null || htmlContent.isEmpty()) {
                Toast.makeText(this, "Введите заголовок и контент", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Асинхронная публикация новости
            PublishNewsTask(title, htmlContent, userid, agencyid, categoryid).execute()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                editor!!.insertImage(imageUri.toString(), "вставленное изображение")
            }
        }
    }

    // AsyncTask для публикации новости
    private inner class PublishNewsTask(
        private val title: String,
        private val content: String,
        private val userid: Long?,
        private val agencyid: Long?,
        private val categoryid: Long?
    ) :
        AsyncTask<Void?, Void?, Boolean>() {
        private var errorMsg: String? = ""

        override fun doInBackground(vararg voids: Void): Boolean {
            try {
                // Создаём объект новости (NewsArticle) и отправляем через Kotlin SupabaseClient
                val article = NewsArticle(
                    0L,  // articleid (автоинкремент)
                    title,
                    content,
                    userid,
                    agencyid,
                    categoryid,
                    null // createdate
                )
                publishNewsArticleSync(article)
                return true
            } catch (e: Exception) {
                errorMsg = e.message
                return false
            }
        }

        override fun onPostExecute(success: Boolean) {
            if (success) {
                Toast.makeText(this@EditNewsActivity, "Новость опубликована", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(
                    this@EditNewsActivity,
                    NewsActivity::class.java
                )
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@EditNewsActivity,
                    "Ошибка публикации: $errorMsg", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}