package com.example.rss_atom_news_aggregator;

import android.content.Context;
import android.content.Intent;

import com.example.rss_atom_news_aggregator.Network.NewsService;
import com.example.rss_atom_news_aggregator.Room.News;
import com.example.rss_atom_news_aggregator.Room.NewsDao;
import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;
import java.util.List;
import androidx.lifecycle.LiveData;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<List<News>> mAllNews;

    NewsRepository() {
        NewsRoomDatabase db = NewsRoomDatabase.getInstance();
        mNewsDao = db.newsDao();
        mAllNews = mNewsDao.getAllNews();
    }

    LiveData<List<News>> getAllNews() {
        return mAllNews;
    }

    public void fetchNews(Context context) {
        Intent intent = new Intent(context, NewsService.class);
        context.startService(intent);
    }

}
