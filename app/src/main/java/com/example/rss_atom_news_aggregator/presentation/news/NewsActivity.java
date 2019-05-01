package com.example.rss_atom_news_aggregator.presentation.news;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
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

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

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
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        getWindow().setExitTransition(new Fade());
        getWindow().setEnterTransition(new Slide());
        setContentView(R.layout.activity_main);
        link = getIntent().getExtras().getString("link");
        String name = getIntent().getExtras().getString("name");
        setTitle(name);
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
                } else {
                    adapter.setNews(viewModel.getAllNews().getValue());
                }
            }
        });
        viewModel.fetchNews(NewsActivity.this, link);
        adapter.setNews(viewModel.getAllNews().getValue());

    }

    @Override
    public void onItemListClick(int position, String link) {
        Intent intent = new Intent(this, WebActivity.class);
        String title = adapter.getNewsTitle(position);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

}
