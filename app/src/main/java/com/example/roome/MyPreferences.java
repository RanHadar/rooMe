package com.example.roome;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * this class is responsible for saving app state and date to preferences file.
 */
public class MyPreferences {
    static final String MY_PREFERENCES = "myPreferences";
    static final String IS_FIRST_LOGIN = "isFirst";
    static final String IS_ROMMATE_SEARCHER = "isRoommateSearcher";

    static boolean isFirst(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return reader.getBoolean(IS_FIRST_LOGIN, true);
    }

    static boolean isRoommateSearcher(Context context) {
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        return reader.getBoolean("isRoommateSearcher", true);
    }
}
