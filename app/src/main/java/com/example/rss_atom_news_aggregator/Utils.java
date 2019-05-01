package com.example.rss_atom_news_aggregator;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import java.util.Locale;

public class Utils {
    public static boolean validateInputChannel(String name, String link, Context context) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(link)) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_empty,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        } else if (!Patterns.WEB_URL.matcher(link).matches()) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_link_valid,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        } else if (!link.toLowerCase().contains("rss") && !link.toLowerCase().contains("atom")) {
            Toast hint = Toast.makeText(context.getApplicationContext(), R.string.error_dialog_link_unsup,
                    Toast.LENGTH_SHORT);
            hint.show();
            return false;
        }
        return true;
    }

    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}
