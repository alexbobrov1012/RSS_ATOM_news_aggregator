package com.example.rss_atom_news_aggregator.presentation.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.room.News;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListHolder> {

    private List<News> news;

    @NonNull
    @Override
    public NewsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new NewsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsListHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        if (news == null)
            return 0;
        return news.size();
    }

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }
}
