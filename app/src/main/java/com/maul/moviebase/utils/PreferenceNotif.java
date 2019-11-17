package com.maul.moviebase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceNotif {
    private final static String PREFERENCE_NOTIF = "preferenceNotif";
    private final static String KEY_DAILY = "dailyReminder";
    private final static String KEY_MESSAGE_RELEASE = "releaseMessege";
    private final static String KEY_MESSAGE_DAILY = "dailyMessege";
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public PreferenceNotif(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NOTIF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setTimeRelease(String time) {
        editor.putString(KEY_DAILY, time);
        editor.commit();
    }

    public void setReleaseMessage(String message) {
        editor.putString(KEY_MESSAGE_RELEASE, message);
    }

    public void setTimeDaily(String time) {
        editor.putString(KEY_DAILY, time);
        editor.commit();
    }

    public void setDailyMessage(String message) {
        editor.putString(KEY_MESSAGE_DAILY, message);
    }
}
