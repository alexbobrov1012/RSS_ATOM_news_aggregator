package com.example.rss_atom_news_aggregator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.print.PrinterId;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StateKeeper {
    private static final String FILE_NAME = "prefs_news_app";

    private static SharedPreferences sharedPref = NewsApplication.appInstance.getSharedPreferences(
            FILE_NAME, Context.MODE_PRIVATE);

    public enum OPTION {
        ADD_DIALOG,
        SETTINGS_DIALOG
    }

    public static final String KEY_CHANNEL_NAME = "channel_name";
    public static final String KEY_CHANNEL_LINK = "channel_link";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_PERIOD = "period";

    public static void updateLocale(Context context) {
        int language = Integer.parseInt(sharedPref.getString(KEY_LANGUAGE,"0"));
        if (language == 0) {
            updateResources(context, "en");
        }
        else if (language == 1) {
            updateResources(context, "ru");
        }
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static void saveState(OPTION option, Map<String, String> settingsMap) {
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (option) {
            case ADD_DIALOG:
                editor.putString(KEY_CHANNEL_NAME, settingsMap.get(KEY_CHANNEL_NAME));
                editor.putString(KEY_CHANNEL_LINK, settingsMap.get(KEY_CHANNEL_LINK));
                editor.apply();
                break;
            case SETTINGS_DIALOG:
                editor.putString(KEY_LANGUAGE, settingsMap.get(KEY_LANGUAGE));
                editor.putString(KEY_PERIOD, settingsMap.get(KEY_PERIOD));
                editor.apply();
                break;
        }
    }

    public static Map<String, String> loadState(OPTION option) {
        Map <String, String> stateMap = new HashMap<>();
        switch (option) {
            case ADD_DIALOG:
                String name = sharedPref.getString(KEY_CHANNEL_NAME, "");
                String link = sharedPref.getString(KEY_CHANNEL_LINK, "");
                stateMap.put(KEY_CHANNEL_NAME, name);
                stateMap.put(KEY_CHANNEL_LINK, link);
                break;
            case SETTINGS_DIALOG:
                String language = sharedPref.getString(KEY_LANGUAGE,"0");
                String period = sharedPref.getString(KEY_PERIOD, "15");
                stateMap.put(KEY_LANGUAGE, language);
                stateMap.put(KEY_PERIOD, period);
                break;
        }
        return stateMap;
    }

    public static int getRepeatInterval() {
        return Integer.parseInt(sharedPref.getString(KEY_PERIOD,"0"));

    }
}
