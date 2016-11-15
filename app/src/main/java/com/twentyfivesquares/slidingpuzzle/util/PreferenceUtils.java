package com.twentyfivesquares.slidingpuzzle.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a utility class that I developed a while ago for fetching and saving shared preferences.
 */
public final class PreferenceUtils {

    private PreferenceUtils() {
        throw new AssertionError("No instances");
    }

    public static void savePreferenceString(Context context, String preferencesName, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String fetchPreferenceString(Context context, String preferencesName, String key) {
        return fetchPreferenceString(context, preferencesName, key, null);
    }

    public static String fetchPreferenceString(
            Context context, String preferencesName, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static void savePreferenceBoolean(Context context, String preferencesName, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean fetchPreferenceBoolean(
            Context context, String preferencesName, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static void savePreferenceInt(Context context, String preferencesName, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int fetchPreferenceInt(Context context, String preferencesName, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static void savePreferenceLong(Context context, String preferencesName, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long fetchPreferenceLong(Context context, String preferencesName, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static void savePreferenceStringSet(
            Context context, String preferencesName, String key, Set<String> values) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(key, values);
        editor.apply();
    }

    public static Set<String> fetchPreferenceStringSet(Context context, String preferencesName, String key) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        return settings.getStringSet(key, new HashSet<String>());
    }

    public static void removePreference(Context context, String preferencesName, String key) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        settings.edit().remove(key).apply();
    }

    /**
     * Clear the entire SharedPreferences, except the preferences that we want to keep, which can
     * be passed through a HashMap.
     *
     * @param context         Context object.
     * @param preferencesName Preference name.
     * @param prefsToKeep     A HashMap containing the type of the preference and its key, so that
     *                        we can retrieve the values and store them into the Editor again to
     *                        retain them.
     */
    public static void clearPreferences(Context context, String preferencesName, List<Pair<Class, String>> prefsToKeep) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Keep the preferences we want to keep by setting their values again in the current Editor.
        if (prefsToKeep != null) {
            for (Pair<Class, String> pair : prefsToKeep) {
                if (pair.first.equals(Boolean.class)) {
                    editor.putBoolean(pair.second, settings.getBoolean(pair.second, false));
                } else if (pair.first.equals(String.class)) {
                    editor.putString(pair.second, settings.getString(pair.second, null));
                } else if (pair.first.equals(Integer.class)) {
                    editor.putInt(pair.second, settings.getInt(pair.second, 0));
                } else if (pair.first.equals(Float.class)) {
                    editor.putFloat(pair.second, settings.getFloat(pair.second, 0));
                } else if (pair.first.equals(Long.class)) {
                    editor.putLong(pair.second, settings.getLong(pair.second, 0));
                } else if (pair.first.equals(Set.class)) {
                    editor.putStringSet(pair.second, settings.getStringSet(pair.second, null));
                }
            }
        }

        // And then clear the rest of it. The editor will keep whatever in it and clear the others.
        editor.clear().apply();
    }
}
