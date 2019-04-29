package com.example.rss_atom_news_aggregator.presentation.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rss_atom_news_aggregator.NewsViewModel;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.News;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class NewsActivity extends AppCompatActivity implements OnItemListClickListener {

    private NewsViewModel viewModel;

    private String link;

    NewsListAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        link = getIntent().getExtras().getString("link");
        //Toast.makeText(this, link, Toast.LENGTH_SHORT).show();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //viewModel.fetchNews(NewsActivity.this, link);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.fetchNews(NewsActivity.this, link);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        viewModel.setCurrentChannel(link);
        viewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (!TextUtils.equals(link, viewModel.getChannelRepo())) {
                    adapter.removeItems();
                    Log.d("KEKE", "act");
                } else {
                    adapter.setNews(viewModel.getAllNews().getValue());
                    Log.d("KEKE", "act1");
                }
            }
        });
        viewModel.fetchNews(NewsActivity.this, link);
        adapter.setNews(viewModel.getAllNews().getValue());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            startActivity(browserIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemListClick(int position, String link) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

}
