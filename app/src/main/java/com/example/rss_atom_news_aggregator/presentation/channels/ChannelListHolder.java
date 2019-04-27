package com.example.rss_atom_news_aggregator.presentation.channels;

import android.view.View;
import android.widget.TextView;

import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.Channel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView nameView;

    private String link;

    private OnItemListClickListener listener;

    public ChannelListHolder(@NonNull final View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.nameView = itemView.findViewById(R.id.channelName);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bind(Channel model) {
        nameView.setText(model.getName());
        link = model.getLink();
    }

    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition(), link);
    }
}
