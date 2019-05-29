package com.example.rss_atom_news_aggregator.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ChannelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Channel channel);

    @Query("delete from channel_table")
    void deleteAll();

    @Query ("select * from channel_table")
    LiveData<List<Channel>> getAllChannels();

    @Query("delete from channel_table where link = :link")
    void delete(String link);

}
