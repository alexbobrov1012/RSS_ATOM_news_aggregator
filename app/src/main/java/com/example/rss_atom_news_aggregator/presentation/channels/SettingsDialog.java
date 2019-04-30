package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.rss_atom_news_aggregator.NewsApplication;
import com.example.rss_atom_news_aggregator.R;

public class SettingsDialog extends DialogFragment {
    SharedPreferences sharedPref;
    
    SeekBar periodSeekBar;

    Spinner langSpinner;

    TextView seekBarView;

    Button applyButton;

    Button cancelButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_settings,null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        periodSeekBar = this.getDialog().findViewById(R.id.period_seekBar);
        seekBarView = this.getDialog().findViewById(R.id.seekBar_textView);
        seekBarView.setText(String.valueOf(periodSeekBar.getProgress()));
        periodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarView.setText(String.valueOf(periodSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        String[] data = getResources().getStringArray(R.array.language_array);
        langSpinner = this.getDialog().findViewById(R.id.lang_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_layout, data);
        langSpinner.setAdapter(adapter);
        applyButton = this.getDialog().findViewById(R.id.button_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveState();
                getDialog().dismiss();
            }
        });
        cancelButton = this.getDialog().findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        loadState();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void saveState() {
        sharedPref = NewsApplication.appInstance.getSharedPreferences("settings_dialog",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("language", langSpinner.getSelectedItemId());
        editor.putInt("period", periodSeekBar.getProgress());
        editor.apply();
    }

    private void loadState() {
        sharedPref = NewsApplication.appInstance.getSharedPreferences("settings_dialog",
                Context.MODE_PRIVATE);
        long language = sharedPref.getLong("language",0);
        int period = sharedPref.getInt("period", 15);
        langSpinner.setSelection((int)language);
        periodSeekBar.setProgress(period);
    }
}
