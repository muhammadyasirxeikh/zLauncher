package com.zvision.zlaunchertwo.shared_storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceStorage {

    public static final String PREF_NAME = "com.zvision.zlaunchertwo";
    public static final int MODE = Context.MODE_PRIVATE;


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void setAppInstallTime(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();

    }

    public static long getAppInstallTime(Context context, String key) {
        return getPreferences(context).getLong(key, 0);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        return getPreferences(context).getBoolean(key, defaultValue);
    }

    public static void setBooleanValue(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();

    }

    public static void setStringValue(Context context, String key, String value) {
        getEditor(context).putString(key, value).apply();
    }

    public static String getStringValue(Context context, String key, String default_value) {
        return getPreferences(context).getString(key, default_value);
    }

    public static int getIntegerValue(Context context, String key, int defaultValue) {
        return getPreferences(context).getInt(key, defaultValue);
    }

    public static void setIntegerValue(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }
}
