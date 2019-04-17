package com.example.rss_atom_news_aggregator;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.example.rss_atom_news_aggregator.Room.News;
import com.example.rss_atom_news_aggregator.Room.NewsDao;
import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
            List listNews = new ArrayList<News>();
            InputStream in = null;
            NewsParser parser = new NewsParser();
            @Override
            public void run() {
                // get and parse news
                Log.d(TAG, "in run..");
                try {
                    in = fetchNewsUrl("http://ports.com/feed/");
                    if (in == null)
                        Log.d(TAG, "instream is null");
                    listNews = parser.parse(in);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "out run..");
                insert(listNews);
            }
        });
        thread.start();
    }

    public void insert(List<News> news) {
        for (News tok : news) {
            mNewsDao.insert(tok);
            Log.d(TAG, "insert");
        }
    }

    private InputStream fetchNewsUrl(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        return connection.getInputStream();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
