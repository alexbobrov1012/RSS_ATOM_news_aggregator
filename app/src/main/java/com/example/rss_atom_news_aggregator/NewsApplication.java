package com.example.rss_atom_news_aggregator;

import android.app.Application;

import com.example.rss_atom_news_aggregator.Room.NewsRoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class NewsApplication extends Application {
    private static volatile NewsRoomDatabase ourInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = Room.databaseBuilder(this,
                NewsRoomDatabase.class, "news_database")
                  .addCallback(new RoomDatabase.Callback() {
                      @Override
                      public void onOpen(@NonNull SupportSQLiteDatabase db) {
                          super.onOpen(db);
                          Thread thread = new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  ourInstance.newsDao().deleteAll();
                              }
                          });
                          thread.start();
                      }
                  })
                  .build();
    }
    public static NewsRoomDatabase getOurInstance() {
        return ourInstance;
    }
}
