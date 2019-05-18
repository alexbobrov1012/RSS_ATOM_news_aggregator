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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChannelAddDialog extends DialogFragment {

    EditText linkText;

    EditText nameText;

    SharedPreferences sharedPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.d("DIALOG","onCreateDiaog");
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
        linkText = this.getDialog().findViewById(R.id.channel_link_text);
        nameText = this.getDialog().findViewById(R.id.channel_name_text);
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
        saveState();
    }

    private void saveState() {
        sharedPref = NewsApplication.appInstance.getSharedPreferences("dialog_add",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("channel_name", nameText.getText().toString());
        editor.putString("channel_link", linkText.getText().toString());
        editor.apply();
    }

    private void loadState() {
        sharedPref = NewsApplication.appInstance.getSharedPreferences("dialog_add",
                Context.MODE_PRIVATE);
        String name = sharedPref.getString("channel_name","");
        String link = sharedPref.getString("channel_link","");
        nameText.setText(name);
        linkText.setText(link);
    }
}
