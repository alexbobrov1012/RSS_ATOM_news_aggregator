package com.example.rss_atom_news_aggregator;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.rss_atom_news_aggregator.Room.News;
import com.example.rss_atom_news_aggregator.Room.NewsDao;
import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class NewsService extends Service {
    final String TAG = "Service";

    Thread thread;

    private NewsDao mNewsDao;

    @Override
    public void onCreate() {
        super.onCreate();
        NewsRoomDatabase db = NewsRoomDatabase.getInstance();
        mNewsDao = db.newsDao();
        Log.d(TAG, "Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        someWork();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroyed");
    }

    private void someWork() {
        thread = new Thread(new Runnable() {
            ArrayList listNews = new ArrayList<News>();
            @Override
            public void run() {
                // get and parse news
                News news = new News("11.04.19", "Самый лучший заголовок", "сегодня" +
                        " я начал делать проект", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                        "_aggregator");
                listNews.add(news);
                news = new News("14.04.19", "Порошенко приехал на стадион для дебатов с " +
                        "Зеленским", "Президент Украины Петр Порошенко прибыл на стадион " +
                        "«Олимпийский» для проведения дебатов с соперником по второму туру выборов " +
                        "главы государства Владимиром Зеленским. Об этом сообщает украинский телеканал" +
                        " «112».", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                        "_aggregator");
                listNews.add(news);
                news = new News("14.04.19", "Американец за 50 лет создал «волшебный» сад " +
                        "(фото)", "Житель Филадельфии Исаия Загар с 1960-х непрерывно украшал " +
                        "улицу возле своего дома эксцентричной мозаикой.\n" +
                        "\n" +
                        "Он использовал осколки разбитых зеркал, бутылки, бусины, осколки плитки, " +
                        "камешки и даже велосипедные колеса. Это была попытка оживить убогий и лишенный" +
                        " красоты район города.", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                        "_aggregator");
                listNews.add(news);
                insert(listNews);
            }
        });
        thread.start();
    }

    public void insert(ArrayList<News> news) {
        for (News tok : news)
            mNewsDao.insert(tok);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
