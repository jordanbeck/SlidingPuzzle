package com.twentyfivesquares.slidingpuzzle.util;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static String formatTime(long millis) {
        final HashMap<TimeUnit, Integer> timeUnits = getTimeUnits(millis);
        final int minutes = timeUnits.get(TimeUnit.MINUTES);
        final int seconds = timeUnits.get(TimeUnit.SECONDS);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private static HashMap<TimeUnit, Integer> getTimeUnits(long milliseconds) {
        HashMap<TimeUnit, Integer> values = new HashMap<>(2);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes);
        values.put(TimeUnit.MINUTES, (int) minutes);
        values.put(TimeUnit.SECONDS, (int) seconds);
        return values;
    }
}
