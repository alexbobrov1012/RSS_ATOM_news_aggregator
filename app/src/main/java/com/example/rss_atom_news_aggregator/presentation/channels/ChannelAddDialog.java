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
import android.widget.EditText;

import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.StateKeeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Map;

public class ChannelAddDialog extends DialogFragment {

    private EditText linkText;

    private EditText nameText;

    private Map<String, String> settingsMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsMap = new HashMap<>();
        Log.d("DIALOG","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("DIALOG","onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add_channel,null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        this.setArguments(null);
        linkText = getDialog().findViewById(R.id.channel_link_text);
        nameText = getDialog().findViewById(R.id.channel_name_text);
        settingsMap = StateKeeper.loadState(StateKeeper.OPTION.ADD_DIALOG);
        nameText.setText(settingsMap.get(StateKeeper.KEY_CHANNEL_NAME));
        linkText.setText(settingsMap.get(StateKeeper.KEY_CHANNEL_LINK));
        if (bundle != null) {
            String linkData = (String) bundle.get("link");
            linkText.setText(linkData);
        }
        Log.d("DIALOG","onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        settingsMap.put(StateKeeper.KEY_CHANNEL_NAME, nameText.getText().toString());
        settingsMap.put(StateKeeper.KEY_CHANNEL_LINK, linkText.getText().toString());
        StateKeeper.saveState(StateKeeper.OPTION.ADD_DIALOG, settingsMap);
        Log.d("DIALOG","onPause");
    }


}
