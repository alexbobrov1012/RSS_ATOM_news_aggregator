package com.example.rss_atom_news_aggregator;

import android.content.Context;

import com.example.rss_atom_news_aggregator.room.News;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class NewsViewModel extends ViewModel {
    private LiveData<List<News>> allNews;

    private String currentChannel;

    public NewsViewModel() {
        allNews = NewsApplication.appInstance.getRepository().getAllNews();
    }

    public LiveData<List<News>> getAllNews() {
        return allNews;
    }

    public void fetchNews(Context context, String link) {
        NewsApplication.appInstance.getRepository().fetchNews(context, link);
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

}
