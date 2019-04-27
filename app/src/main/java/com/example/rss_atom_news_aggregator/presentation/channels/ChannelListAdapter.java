package com.example.rss_atom_news_aggregator.presentation.channels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.Channel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListHolder> {
    private List<Channel> channels;

    private OnItemListClickListener listener;

    public ChannelListAdapter(OnItemListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChannelListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_item, parent, false);
        return new ChannelListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelListHolder holder, int position) {
        holder.bind(channels.get(position));
    }

    @Override
    public int getItemCount() {
        if (channels == null)
            return 0;
        return channels.size();
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    public int getChannelId(int position) {
        return channels.get(position).getId();
    }
}
