package com.example.a1;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FullNewsActivity extends AppCompatActivity {

    WebView webViewContent;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);

        webViewContent = findViewById(R.id.webViewContent);
        tvTitle = findViewById(R.id.tvTitle);

        NewsItem item = (NewsItem) getIntent().getSerializableExtra("news");
        if (item != null) {
            tvTitle.setText(item.getTitle());
            webViewContent.loadDataWithBaseURL(null, item.getContent(), "text/html", "utf-8", null);

        }
    }
}
