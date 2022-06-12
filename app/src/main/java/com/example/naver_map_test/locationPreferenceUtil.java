package com.example.naver_map_test;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class locationPreferenceUtil {


    public static void setLatitudePreference(@NonNull Context context, String latKey, Double layValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(latKey, Double.doubleToRawLongBits(layValue));
        editor.apply();
    }

    public static void setLongitudePreference(@NonNull Context context, String longKey, Double longValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(longKey, Double.doubleToRawLongBits(longValue));
        editor.apply();
    }


    public static double getLatitudePreferences(Context context, String latKey, double defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        if(!preferences.contains(latKey)) {
            return defaultValue;
        }
        return Double.longBitsToDouble(preferences.getLong(latKey, 0));
    }


    public static double getLongitudePreferences(Context context, String longKey, double defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);

        if(!preferences.contains(longKey)) {
            return defaultValue;
        }

        return Double.longBitsToDouble(preferences.getLong(longKey, 0));
    }



}
