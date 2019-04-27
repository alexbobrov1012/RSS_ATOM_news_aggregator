package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.example.rss_atom_news_aggregator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ChannelAddDialog extends DialogFragment {

    EditText linkText;
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
        if (bundle != null) {
            String linkData = (String) bundle.get("link");
            linkText = this.getDialog().findViewById(R.id.channel_link_text);
            linkText.setText(linkData.toString());
        }
        Log.d("DIALOG","onStart");
    }
}
