package com.example.rss_atom_news_aggregator.presentation.channels;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
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

import com.example.rss_atom_news_aggregator.R;
import com.example.rss_atom_news_aggregator.StateKeeper;

import java.util.HashMap;

import java.util.Map;
import java.util.Objects;

public class SettingsDialog extends DialogFragment {
    
    private SeekBar periodSeekBar;

    private Spinner langSpinner;

    private TextView seekBarView;

    private Map<String, String> settingsMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_settings, null));
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        StateKeeper.updateLocale(Objects.requireNonNull(getDialog()).getContext());
        Log.v("LOCALE", "Settings");
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
        settingsMap = new HashMap<>();
        String[] data = getResources().getStringArray(R.array.language_array);
        langSpinner = this.getDialog().findViewById(R.id.lang_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),
                R.layout.spinner_layout, data);
        langSpinner.setAdapter(adapter);
        Button applyButton = this.getDialog().findViewById(R.id.button_apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsMap.put(StateKeeper.KEY_PERIOD, String.valueOf(periodSeekBar.getProgress()));
                settingsMap.put(StateKeeper.KEY_LANGUAGE, String.valueOf(langSpinner.getSelectedItemId()));
                StateKeeper.saveState(StateKeeper.OPTION.SETTINGS_DIALOG, settingsMap);
                StateKeeper.updateLocale(getDialog().getContext().getApplicationContext());
                Log.v("LOCALE", "Settings2");
                Objects.requireNonNull(getActivity()).recreate();
                getDialog().dismiss();
            }
        });
        Button cancelButton = this.getDialog().findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        settingsMap = StateKeeper.loadState(StateKeeper.OPTION.SETTINGS_DIALOG);
        langSpinner.setSelection(Integer.parseInt(settingsMap.get(StateKeeper.KEY_LANGUAGE)));
        periodSeekBar.setProgress(Integer.parseInt(settingsMap.get(StateKeeper.KEY_PERIOD)));
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
