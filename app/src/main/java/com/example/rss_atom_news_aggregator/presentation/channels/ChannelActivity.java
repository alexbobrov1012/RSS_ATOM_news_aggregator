package com.example.rss_atom_news_aggregator.presentation.channels;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.rss_atom_news_aggregator.ChannelViewModel;
import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.presentation.news.NewsActivity;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.Channel;
import com.example.rss_atom_news_aggregator.utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class ChannelActivity extends AppCompatActivity implements OnItemListClickListener {
    ChannelAddDialog dialogAdd;

    SettingsDialog dialogSettings;

    ChannelViewModel viewModel;

    ChannelListAdapter adapter;

    Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialogAdd = new ChannelAddDialog();
        dialogSettings = new SettingsDialog();
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            Bundle arg = new Bundle();
            arg.putCharSequence("link", data.toString());
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void dialogClick(View view) {
        if (view.getId() == R.id.button_ok) {
            // create new channel
            if (dialogAdd.getDialog() == null) {
                Log.v("DIALOG", "bad");
                return;
            }

            Channel channel = null;
            EditText nameText = dialogAdd.getDialog().findViewById(R.id.channel_name_text);
            EditText linkText = dialogAdd.getDialog().findViewById(R.id.channel_link_text);
            String name = nameText.getText().toString();
            String link = linkText.getText().toString();


            if (utils.validateInputChannel(name, link, this)) {
                channel = new Channel(nameText.getText().toString(), linkText.getText().toString());
                viewModel.addChannel(channel);
                dialogAdd.dismiss();
            }
        } else if (view.getId() == R.id.button_cancel) {
            dialogAdd.dismiss();
        }
    }

    @Override
    public void onItemListClick(int position, String link) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("link", link);
        intent.setFlags(FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

    }
}
