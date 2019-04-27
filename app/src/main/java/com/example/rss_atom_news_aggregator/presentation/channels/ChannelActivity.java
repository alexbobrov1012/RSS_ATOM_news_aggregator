package com.example.rss_atom_news_aggregator.presentation.channels;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;

import com.example.rss_atom_news_aggregator.ChannelViewModel;
import com.example.rss_atom_news_aggregator.presentation.news.NewsActivity;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.presentation.OnItemListClickListener;
import com.example.rss_atom_news_aggregator.room.Channel;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class ChannelActivity extends AppCompatActivity implements OnItemListClickListener {
    ChannelAddDialog dialog;

    ChannelViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ChannelAddDialog();
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            Bundle arg = new Bundle();
            arg.putCharSequence("link", data.toString());
            dialog.setArguments(arg);
            dialog.show(getSupportFragmentManager(), "add_channel");
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getSupportFragmentManager(), "add_channel");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_channel);
        final ChannelListAdapter adapter = new ChannelListAdapter(this);
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
    }

    public void dialogClick(View view) {
        if (view.getId() == R.id.button_ok) {
            // create new channel
            if (dialog.getDialog() == null) {
                Log.v("DIALOG", "bad");
                return;
            }

            Channel channel = null;
            EditText nameText = dialog.getDialog().findViewById(R.id.channel_name_text);
            EditText linkText = dialog.getDialog().findViewById(R.id.channel_link_text);
            String name = nameText.getText().toString();
            String link = linkText.getText().toString();

            if (name.equals("") || link.equals("")) {
                Toast hint = Toast.makeText(getApplicationContext(), R.string.error_dialog_empty,
                        Toast.LENGTH_SHORT);
                hint.show();
            } else if (!Patterns.WEB_URL.matcher(link).matches()) {
                Toast hint = Toast.makeText(getApplicationContext(), R.string.error_dialog_link_valid,
                            Toast.LENGTH_SHORT);
                hint.show();

            } else if (!link.toLowerCase().contains("rss") && !link.toLowerCase().contains("atom")) {
                    Toast hint = Toast.makeText(getApplicationContext(), R.string.error_dialog_link_unsup,
                            Toast.LENGTH_SHORT);
                    hint.show();
            } else {
                channel = new Channel(nameText.getText().toString(), linkText.getText().toString());
                viewModel.addChannel(channel);
                dialog.dismiss();
            }

        } else if (view.getId() == R.id.button_cancel) {
            dialog.dismiss();
        }
    }

    @Override
    public void onItemListClick(int position, String link) {
        Intent intent = new Intent(this, NewsActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);

    }
}
