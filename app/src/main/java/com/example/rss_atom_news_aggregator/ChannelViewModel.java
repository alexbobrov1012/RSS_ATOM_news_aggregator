package com.example.rss_atom_news_aggregator;

import android.content.Context;

import com.example.rss_atom_news_aggregator.room.Channel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ChannelViewModel extends ViewModel {
    private LiveData<List<Channel>> allChannels;

    public ChannelViewModel() {
        allChannels = NewsApplication.appInstance.getRepository().getAllChannels();
    }

    public LiveData<List<Channel>> getAllChannels() {
        return allChannels;
    }

    public void addChannel(Channel channel) {
         NewsApplication.appInstance.getRepository().insert(channel);
    }

    public void delete(int id) {
        NewsApplication.appInstance.getRepository().delete(id);
    }

    public void update(Channel channel) {
        NewsApplication.appInstance.getRepository().updateChannel(channel);
    }

    public int getChannelId(String name, String link) {
        return NewsApplication.appInstance.getRepository().getChannelId(name, link);
    }

}
