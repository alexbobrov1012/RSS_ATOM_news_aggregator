package com.example.rss_atom_news_aggregator.Room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {News.class}, version = 1)
public abstract class NewsRoomDatabase extends RoomDatabase {
    private static volatile NewsRoomDatabase ourInstance;

    public abstract NewsDao newsDao();

    public static NewsRoomDatabase getInstance(final Context context) {
        if (ourInstance == null) {
            synchronized (NewsRoomDatabase.class) {
                if (ourInstance == null)
                    ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                            NewsRoomDatabase.class, "news_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
            }
        }
        return ourInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(ourInstance).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final NewsDao mDao;
        PopulateDbAsync(NewsRoomDatabase db) {
            mDao = db.newsDao();
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            News news = new News("11.04.19", "Самый лучший заголовок", "сегодня" +
                    " я начал делать проект", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                    "_aggregator");
            return null;
        }
    }
}

