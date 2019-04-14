package com.example.rss_atom_news_aggregator;

import android.app.Application;

import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;

import androidx.room.Room;

public class NewsApplication extends Application {
    private static volatile NewsRoomDatabase ourInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = Room.databaseBuilder(this,
                NewsRoomDatabase.class, "news_database")
                  .build();
    }
    public static NewsRoomDatabase getOurInstance() {
        return ourInstance;
    }
}
