package com.example.a1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SendNewsletterActivity extends AppCompatActivity {

    Spinner spinnerNews;
    ListView listViewEmails;
    Button btnSend, btnBack;

    ArrayList<NewsItem> newsList;
    ArrayList<String> emailList;
    ArrayList<String> newsTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_newsletter);

        spinnerNews = findViewById(R.id.spinnerNews);
        listViewEmails = findViewById(R.id.listViewEmails);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        // 1. Получаем список новостей
        newsList = NewsStorage.getNewsList();
        newsTitles = new ArrayList<>();
        for (NewsItem item : newsList) {
            newsTitles.add(item.getTitle());
        }
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, newsTitles);
        newsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNews.setAdapter(newsAdapter);

        // 2. Получаем список email пользователей
        emailList = UserStorage.getAllEmails();  // Предположим, что вы реализовали этот метод
        ArrayAdapter<String> emailAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, emailList);
        listViewEmails.setAdapter(emailAdapter);

        // 3. Отправка email
        btnSend.setOnClickListener(v -> {
            int selectedNewsIndex = spinnerNews.getSelectedItemPosition();
            NewsItem selectedNews = newsList.get(selectedNewsIndex);

            ArrayList<String> selectedEmails = new ArrayList<>();
            for (int i = 0; i < emailList.size(); i++) {
                if (listViewEmails.isItemChecked(i)) {
                    selectedEmails.add(emailList.get(i));
                }
            }

            if (selectedEmails.isEmpty()) {
                Toast.makeText(this, "Выберите хотя бы один email!", Toast.LENGTH_SHORT).show();
                return;
            }

            sendEmail(selectedEmails, selectedNews.getTitle(), stripHtml(selectedNews.getContent())); // ✅ исправлено
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void sendEmail(ArrayList<String> recipients, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // только email apps
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients.toArray(new String[0]));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(emailIntent, "Выберите почтовое приложение"));
        } else {
            Toast.makeText(this, "Нет подходящего почтового приложения", Toast.LENGTH_SHORT).show();
        }
    }

    private String stripHtml(String html) {
        return android.text.Html.fromHtml(html, android.text.Html.FROM_HTML_MODE_LEGACY).toString();
    }
}
