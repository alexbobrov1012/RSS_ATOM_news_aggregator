package com.example.rss_atom_news_aggregator.presentation.news;


import android.view.View;
import android.widget.TextView;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.News;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView titleView;

    private TextView dateView;

    private TextView contentsView;

    private OnItemListClickListener listener;

    private String link;

    public NewsListHolder(@NonNull View itemView, OnItemListClickListener listener) {
        super(itemView);
        this.listener = listener;
        this.titleView = itemView.findViewById(R.id.title);
        this.dateView = itemView.findViewById(R.id.date);
        this.contentsView = itemView.findViewById(R.id.contents);
        itemView.setOnClickListener(this);
    }

    public void bind(News model) {
        titleView.setText(model.getTitle());
        dateView.setText(model.getDate());
        contentsView.setText(model.getContent());
        link = model.getLink();
    }

    @Override
    public void onClick(View v) {
        listener.onItemListClick(getAdapterPosition(), link);
    }
}
