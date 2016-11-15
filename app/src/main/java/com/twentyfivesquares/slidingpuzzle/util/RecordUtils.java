package com.twentyfivesquares.slidingpuzzle.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twentyfivesquares.slidingpuzzle.object.Record;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RecordUtils {

    private static final String PREFS_NAME = RecordUtils.class.getPackage() + ".PREFS";
    private static final String PREFS_RECORDS = "RECORDS";

    public static Record fetchRecord(Context context, Integer size) {
        Record record = fetchRecords(context).get(size);
        return record == null ? new Record() : record;
    }

    public static Map<Integer, Record> fetchRecords(Context context) {
        final String recordsJson = PreferenceUtils.fetchPreferenceString(context, PREFS_NAME, PREFS_RECORDS);

        if (TextUtils.isEmpty(recordsJson)) {
            return new HashMap<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Record>>(){}.getType();
        return gson.fromJson(recordsJson, type);
    }

    public static void updateRecord(Context context, Integer size, Record record) {
        Map<Integer, Record> records = fetchRecords(context);
        records.put(size, record);
        saveRecords(context, records);
    }

    public static void saveRecords(Context context, Map<Integer, Record> records) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<Integer, Record>>(){}.getType();
        String recordsJson = gson.toJson(records, type);
        PreferenceUtils.savePreferenceString(context, PREFS_NAME, PREFS_RECORDS, recordsJson);
    }

    public static void clearRecords(Context context) {
        PreferenceUtils.savePreferenceString(context, PREFS_NAME, PREFS_RECORDS, null);
    }
}
