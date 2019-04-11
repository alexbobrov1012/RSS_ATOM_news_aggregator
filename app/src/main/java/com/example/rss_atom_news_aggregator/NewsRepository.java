package com.example.rss_atom_news_aggregator;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.rss_atom_news_aggregator.Room.News;
import com.example.rss_atom_news_aggregator.Room.NewsDao;
import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;

import java.util.List;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<List<News>> mAllNews;

    NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getInstance(application);
        mNewsDao = db.newsDao();
        mAllNews = mNewsDao.getAllNews();
    }

    LiveData<List<News>> getAllNews() {
        return mAllNews;
    }


    public void insert (News word) {
        new insertAsyncTask(mNewsDao).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<News, Void, Void> {

        private NewsDao mAsyncTaskDao;

        insertAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final News... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
