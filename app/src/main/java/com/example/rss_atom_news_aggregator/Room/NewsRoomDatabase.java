package com.example.rss_atom_news_aggregator.Room;

import android.content.Context;
import android.os.AsyncTask;

import com.example.rss_atom_news_aggregator.NewsApplication;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {News.class}, version = 1)
public abstract class NewsRoomDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public static NewsRoomDatabase getInstance() {
        return NewsApplication.getOurInstance();
    }
}

