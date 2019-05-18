package com.example.rss_atom_news_aggregator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.example.rss_atom_news_aggregator.room.NewsRoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class NewsApplication extends Application {
    private NewsRoomDatabase dbInstance;

    private NewsRepository repoInstance;

    public static NewsApplication appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        dbInstance = Room.databaseBuilder(this,
                NewsRoomDatabase.class, "news_database1")
                  .addCallback(new RoomDatabase.Callback() {
                      @Override
                      public void onOpen(@NonNull SupportSQLiteDatabase db) {
                          super.onOpen(db);
                          Thread thread = new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  dbInstance.newsDao().deleteAll();
                              }
                          });
                          thread.start();
                      }
                  })
                  .build();
        repoInstance = new NewsRepository();
        NewsApplication.appInstance.updateLocale(this);
    }

    public NewsRoomDatabase getDBInstance() {
        return dbInstance;
    }

    public NewsRepository getRepository() {
        return repoInstance;
    }

    public void updateLocale(Context context) {
        SharedPreferences sharedPref = NewsApplication.appInstance.getSharedPreferences("settings_dialog",
                Context.MODE_PRIVATE);
        long language = sharedPref.getLong("language",0);
        if (language == 0) {
            Utils.updateResources(context, "en");
        }
        else if (language == 1) {
            Utils.updateResources(context, "ru");
        }
    }
}
