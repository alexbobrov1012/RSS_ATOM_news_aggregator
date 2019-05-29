package com.example.rss_atom_news_aggregator.presentation.channels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    ChannelListAdapter adapter;

    ChannelViewModel viewModel;

    public SwipeToDeleteCallback(ChannelListAdapter adapter, ChannelViewModel viewModel) {
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT );
        this.adapter = adapter;
        this.viewModel = viewModel;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        String link = adapter.getChannelLink(position);
        viewModel.delete(link);
        Log.d("DIALOG","ID = " + link);
    }
}
