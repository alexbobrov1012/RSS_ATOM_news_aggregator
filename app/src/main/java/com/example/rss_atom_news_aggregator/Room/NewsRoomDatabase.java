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
        new PopulateDbAsync(NewsApplication.getOurInstance()).execute();
        return NewsApplication.getOurInstance();
    }

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
            mDao.insert(news);
            news = new News("14.04.19", "Порошенко приехал на стадион для дебатов с " +
                    "Зеленским", "Президент Украины Петр Порошенко прибыл на стадион " +
                    "«Олимпийский» для проведения дебатов с соперником по второму туру выборов " +
                    "главы государства Владимиром Зеленским. Об этом сообщает украинский телеканал" +
                    " «112».", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                    "_aggregator");
            mDao.insert(news);
            news = new News("14.04.19", "Американец за 50 лет создал «волшебный» сад " +
                    "(фото)", "Житель Филадельфии Исаия Загар с 1960-х непрерывно украшал " +
                    "улицу возле своего дома эксцентричной мозаикой.\n" +
                    "\n" +
                    "Он использовал осколки разбитых зеркал, бутылки, бусины, осколки плитки, " +
                    "камешки и даже велосипедные колеса. Это была попытка оживить убогий и лишенный" +
                    " красоты район города.", "https://github.com/bobrisha1012/RSS_ATOM_news" +
                    "_aggregator");
            mDao.insert(news);
            return null;
        }
    }
}

