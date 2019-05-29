package com.example.rss_atom_news_aggregator.presentation.channels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.room.Channel;

import java.util.List;

public class ChannelViewModel extends ViewModel {
    private LiveData<List<Channel>> allChannels;

    public ChannelViewModel() {
        allChannels = NewsApplication.appInstance.getRepository().getAllChannels();
    }

    LiveData<List<Channel>> getAllChannels() {
        return allChannels;
    }

    void addChannel(Channel channel) {
        NewsApplication.appInstance.getRepository().insert(channel);
    }

    void delete(String link) {
        NewsApplication.appInstance.getRepository().delete(link);
    }

}
