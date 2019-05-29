package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.StateKeeper;
import com.example.rss_atom_news_aggregator.Utils;
import com.example.rss_atom_news_aggregator.room.Channel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;
import java.util.Map;

public class ChannelAddDialog extends DialogFragment implements View.OnClickListener {

    Button applyButton;

    Button cancelButton;

    private EditText linkText;

    private EditText nameText;

    private Map<String, String> settingsMap;

    private ChannelViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsMap = new HashMap<>();
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        Log.d("DIALOG","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("DIALOG","onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_channel,null);
        applyButton = view.findViewById(R.id.button_ok);
        applyButton.setOnClickListener(this);
        cancelButton = view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        setArguments(null);
        loadState();
        if (bundle != null) {
            String linkData = (String) bundle.get("link");
            linkText.setText(linkData);
        }
        Log.d("DIALOG","onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        saveState(nameText.getText().toString(), linkText.getText().toString());
        Log.d("DIALOG","onPause");
    }

    private void saveState(String name, String link) {
        settingsMap.clear();
        settingsMap.put(StateKeeper.KEY_CHANNEL_NAME, name);
        settingsMap.put(StateKeeper.KEY_CHANNEL_LINK, link);
        StateKeeper.saveState(StateKeeper.OPTION.ADD_DIALOG, settingsMap);
    }

    private void loadState() {
        linkText = getDialog().findViewById(R.id.channel_link_text);
        nameText = getDialog().findViewById(R.id.channel_name_text);
        settingsMap = StateKeeper.loadState(StateKeeper.OPTION.ADD_DIALOG);
        nameText.setText(settingsMap.get(StateKeeper.KEY_CHANNEL_NAME));
        linkText.setText(settingsMap.get(StateKeeper.KEY_CHANNEL_LINK));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_ok) {
            // create new channel
            if (getDialog() == null) {
                Log.v("DIALOG", "bad");
                return;
            }

            Channel channel;
            EditText nameText = getDialog().findViewById(R.id.channel_name_text);
            EditText linkText = getDialog().findViewById(R.id.channel_link_text);
            String name = nameText.getText().toString();
            String link = linkText.getText().toString();

            if (Utils.validateInputChannel(name, link, getContext())) {
                channel = new Channel(nameText.getText().toString(), linkText.getText().toString());
                viewModel.addChannel(channel);
                Log.v("DIALOG", "keke");
                nameText.setText("");
                linkText.setText("");
                dismiss();
            }
        } else if (v.getId() == R.id.button_cancel) {
            dismiss();
        }
    }
}
