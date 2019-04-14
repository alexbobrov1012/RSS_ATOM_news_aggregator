package com.example.rss_atom_news_aggregator;

import com.example.rss_atom_news_aggregator.Room.News;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {
    private NewsRepository repository;

    private LiveData<List<News>> allNews;

    public NewsViewModel() {
        repository = new NewsRepository();
        allNews = repository.getAllNews();
    }

    public LiveData<List<News>> getAllNews() {
        return allNews;
    }

    public void insert(News news) {
        repository.insert(news);
    }
}
