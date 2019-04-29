package com.example.rss_atom_news_aggregator.presentation.news;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.rss_atom_news_aggregator.R;

public class WebActivity extends AppCompatActivity {
    private String newsLink;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebViewClient webViewClient = new WebViewClient() {

            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)@Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        newsLink = getIntent().getExtras().getString("link");
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(webViewClient);
        webView.getSettings().getJavaScriptEnabled();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_web);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(newsLink);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        webView.loadUrl(newsLink);
        swipeRefreshLayout.setRefreshing(false);
        //Toast toast = Toast.makeText(this, newsLink + "e", Toast.LENGTH_LONG);
        //toast.show();
    }

}
