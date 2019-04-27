package com.example.rss_atom_news_aggregator.presentation.news;


import android.view.View;
import android.widget.TextView;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.room.News;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListHolder extends RecyclerView.ViewHolder {
    private TextView titleView;

    private TextView dateView;

    private TextView contentsView;

    public NewsListHolder(@NonNull View itemView) {
        super(itemView);
        this.titleView = itemView.findViewById(R.id.title);
        this.dateView = itemView.findViewById(R.id.date);
        this.contentsView = itemView.findViewById(R.id.contents);
    }

    public void bind(News model) {
        titleView.setText(model.getTitle());
        dateView.setText(model.getDate());
        contentsView.setText(model.getContent());
    }

}
