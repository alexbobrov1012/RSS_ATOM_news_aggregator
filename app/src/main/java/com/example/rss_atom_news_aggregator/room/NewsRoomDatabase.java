package com.example.rss_atom_news_aggregator.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {News.class, Channel.class}, version = 1)
public abstract class NewsRoomDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
    public abstract ChannelDao channelDao();
}

