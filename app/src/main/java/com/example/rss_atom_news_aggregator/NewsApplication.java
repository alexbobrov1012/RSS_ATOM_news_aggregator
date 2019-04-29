package com.example.rss_atom_news_aggregator;

import android.app.Application;
import android.content.Context;

import com.example.rss_atom_news_aggregator.room.NewsRoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class NewsApplication extends Application {
    private NewsRoomDatabase DBInstance;

    private NewsRepository RepoInstance;

    public static NewsApplication appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        DBInstance = Room.databaseBuilder(this,
                NewsRoomDatabase.class, "news_database1")
                  .addCallback(new RoomDatabase.Callback() {
                      @Override
                      public void onOpen(@NonNull SupportSQLiteDatabase db) {
                          super.onOpen(db);
                          Thread thread = new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  DBInstance.newsDao().deleteAll();
                              }
                          });
                          thread.start();
                      }
                  })
                  .build();
        RepoInstance = new NewsRepository();
    }

    public NewsRoomDatabase getDBInstance() {
        return DBInstance;
    }

    public NewsRepository getRepository() {
        return RepoInstance;
    }
}
