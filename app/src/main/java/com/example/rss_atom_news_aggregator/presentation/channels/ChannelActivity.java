package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.StateKeeper;
import com.example.rss_atom_news_aggregator.presentation.news.NewsActivity;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.Channel;
import com.example.rss_atom_news_aggregator.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class ChannelActivity extends AppCompatActivity implements OnItemListClickListener {

    ChannelViewModel viewModel;

    ChannelListAdapter adapter;

    Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        StateKeeper.updateLocale(this);
        Log.v("LOCALE", "ChannelAc");
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ChannelAddDialog dialogAdd = new ChannelAddDialog();
        final SettingsDialog dialogSettings = new SettingsDialog();
        Intent intent = getIntent();
        String action = intent.getAction();
        String data = null;
        if (Intent.ACTION_SEND.equals(action)) {
            data = intent.getStringExtra(Intent.EXTRA_TEXT);
        } else if (Intent.ACTION_VIEW.equals(action)) {
            data = Objects.requireNonNull(intent.getData()).toString();
        }
        if (data != null) {
            Bundle arg = new Bundle();
            arg.putCharSequence("link", data);
            dialogAdd.setArguments(arg);
            dialogAdd.show(getSupportFragmentManager(), "add_channel");
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.show(getSupportFragmentManager(), "add_channel");
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview_channel);
        adapter = new ChannelListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getAllChannels().observe(this, new Observer<List<Channel>>() {
            @Override
            public void onChanged(List<Channel> channels) {
                adapter.setChannels(channels);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, viewModel));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSettings.show(getSupportFragmentManager(), "settings");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemListClick(int position, String link) {
        Intent intent = new Intent(this, NewsActivity.class);
        String name = adapter.getChannelName(position);
        intent.putExtra("link", link);
        intent.putExtra("name", name);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

}
