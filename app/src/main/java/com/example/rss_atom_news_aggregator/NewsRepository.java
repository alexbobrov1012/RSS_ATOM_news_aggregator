package com.example.rss_atom_news_aggregator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.example.rss_atom_news_aggregator.network.NewsParser;
import com.example.rss_atom_news_aggregator.network.NewsParserATOM;
import com.example.rss_atom_news_aggregator.network.NewsParserRSS;
import com.example.rss_atom_news_aggregator.network.NewsWorker;
import com.example.rss_atom_news_aggregator.room.Channel;
import com.example.rss_atom_news_aggregator.room.ChannelDao;
import com.example.rss_atom_news_aggregator.room.News;
import com.example.rss_atom_news_aggregator.room.NewsDao;
import com.example.rss_atom_news_aggregator.room.NewsRoomDatabase;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NewsRepository {
    private NewsDao newsDao;

    private ChannelDao channelDao;

    private LiveData<List<News>> AllNews;

    private LiveData<List<Channel>> AllChannels;

    private String currentChannel;

    private NewsParser parser;

    private Executor executor;

    private PeriodicWorkRequest NewsWorkRequestPeriodic;

    private final String TAG_SERVICE = "Worker123";

    public NewsRepository() {
        NewsRoomDatabase db = NewsApplication.appInstance.getDBInstance();
        newsDao = db.newsDao();
        channelDao = db.channelDao();
        AllNews = newsDao.getAllNews();
        AllChannels = channelDao.getAllChannels();
        currentChannel = "";
        executor =  Executors.newSingleThreadExecutor();
    }

    public LiveData<List<News>> getAllNews() {
        return AllNews;
    }

    public LiveData<List<Channel>> getAllChannels() {
        return AllChannels;
    }

    public void serviceRoutine() {
        List<News> listNews;
        InputStream in;

        // get and parse news
        Log.d(TAG_SERVICE, "in run..");
        try {
            HttpURLConnection connection;
            URL url = new URL(currentChannel);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            in = connection.getInputStream();
            if (in == null) {
                Log.d(TAG_SERVICE, "instream is null");
                return;
            }
            listNews = parser.parse(in);
            in.close();
            connection.disconnect();
            if(listNews == null) {
                Log.d(TAG_SERVICE, "parsing failed");
                return;
            } else {
                deleteAll();
                insert(listNews);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG_SERVICE, "out run..");
    }

    public void fetchNews(String channelLink) {
        if (TextUtils.equals(channelLink,currentChannel) || !isOnline()) {
            // fetch from db
            AllNews = newsDao.getAllNews();
        } else {
            currentChannel = channelLink;
            // fetch from network
            if (currentChannel.toLowerCase().contains("rss")) {
                // rss parser
                parser = new NewsParserRSS();

            } else if (currentChannel.toLowerCase().contains("atom")) {
                // atom parser
                parser = new NewsParserATOM();

            }
            Constraints constraints = new Constraints.Builder().setRequiredNetworkType(
                    NetworkType.CONNECTED).build();
            if (NewsWorkRequestPeriodic == null) {
                NewsWorkRequestPeriodic = new PeriodicWorkRequest.Builder(
                        NewsWorker.class, StateKeeper.getRepeatInterval(), TimeUnit.MINUTES)
                        .setConstraints(constraints)
                          .build();
                WorkManager.getInstance().enqueueUniquePeriodicWork("fetch_news_worker",
                        ExistingPeriodicWorkPolicy.REPLACE, NewsWorkRequestPeriodic);
            } else {
                OneTimeWorkRequest newsWorkRequest = new OneTimeWorkRequest.Builder(NewsWorker.class).
                        setConstraints(constraints).build();
                WorkManager.getInstance().enqueue(newsWorkRequest);
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) NewsApplication.
                        appInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    
    private void insert(List<News> newsList) {
        for (News tok : newsList) {
            newsDao.insert(tok);
        }
    }

    public void insert(final Channel channel) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channelDao.insert(channel);
            }
        });
    }

    public void delete(final int id) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channelDao.delete(id);
            }
        });

    }

    private void deleteAll() {
        newsDao.deleteAll();
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }
}
