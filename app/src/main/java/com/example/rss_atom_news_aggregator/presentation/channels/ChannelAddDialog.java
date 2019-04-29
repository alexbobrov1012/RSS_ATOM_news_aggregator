package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

    SharedPreferences sPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DIALOG","onCreate");
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
        sPref = NewsApplication.appInstance.getSharedPreferences("dialog_add",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("channel_name", nameText.getText().toString());
        editor.putString("channel_link", linkText.getText().toString());
        editor.apply();
    }

    private void loadState() {
        sPref = NewsApplication.appInstance.getSharedPreferences("dialog_add",
                Context.MODE_PRIVATE);
        String name = sPref.getString("channel_name","");
        String link = sPref.getString("channel_link","");
        nameText.setText(name);
        linkText.setText(link);
    }
}
