package com.example.rss_atom_news_aggregator.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (News news);

    @Query("delete from news_table")
    void deleteAll ();

    @Query ("select * from news_table")
    LiveData<List<News>> getAllNews ();
}
