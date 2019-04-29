package com.example.rss_atom_news_aggregator;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.rss_atom_news_aggregator.network.NewsParser;
import com.example.rss_atom_news_aggregator.network.NewsParserATOM;
import com.example.rss_atom_news_aggregator.network.NewsParserRSS;
import com.example.rss_atom_news_aggregator.network.NewsService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

public class NewsRepository {
    private NewsDao newsDao;

    private ChannelDao channelDao;

    private LiveData<List<News>> AllNews;

    private LiveData<List<Channel>> AllChannels;

    private String currentChannel;

    private NewsParser parser;

    private HttpURLConnection connection;

    Executor executor;

    private final String TAG_SERVICE = "REpo";

    public NewsRepository() {
        NewsRoomDatabase db = NewsApplication.appInstance.getDBInstance();
        newsDao = db.newsDao();
        channelDao = db.channelDao();
        AllNews = newsDao.getAllNews();
        AllChannels = channelDao.getAllChannels();
        currentChannel = "";
    }

    LiveData<List<News>> getAllNews() {
        return AllNews;
    }

    LiveData<List<Channel>> getAllChannels() {
        return AllChannels;
    }

    public void serviceRoutine() {
        List listNews = new ArrayList<News>();
        InputStream in = null;
        // get and parse news
        Log.d(TAG_SERVICE, "in run..");
        try {
            in = getInputStream(currentChannel);
            if (in == null)
                Log.d(TAG_SERVICE, "instream is null");
            listNews = parser.parse(in);
            in.close();
            deleteAll();
            insert(listNews);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.disconnect();
        Log.d(TAG_SERVICE, "out run..");
    }

    public void fetchNews(Context context, String channelLink) {
        if (TextUtils.equals(channelLink,currentChannel)) {
            // fetch from db
            Log.d("KEKE", "DB");
            AllNews = newsDao.getAllNews();
            return;

        } else if (isOnline()){
            Log.d("KEKE", "SERV");
            currentChannel = channelLink;
            // fetch from network
            if (currentChannel.toLowerCase().contains("rss")) {
                // rss parser
                parser = new NewsParserRSS();

            } else if (currentChannel.toLowerCase().contains("atom")) {
                // atom parser
                parser = new NewsParserATOM();

            }
            Intent intent = new Intent(context, NewsService.class);
            context.startService(intent);
        } else {
            Log.d("KEKE", "else");
        }
    }

    private InputStream getInputStream(String link) throws IOException {
        URL url = new URL(link);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        return connection.getInputStream();
    }
    
    private void insert(List<News> newsList) {
        for (News tok : newsList) {
            newsDao.insert(tok);
        }
    }

    public void insert(final Channel channel) {
        executor =  Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                channelDao.insert(channel);
            }
        });
    }

    public void delete(final int id) {
        executor =  Executors.newSingleThreadExecutor();
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

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateChannel(Channel channel) {
        channelDao.update(channel);
    }

    public int getChannelId(String name, String link) {
        return channelDao.getId(name, link);
    }
}
