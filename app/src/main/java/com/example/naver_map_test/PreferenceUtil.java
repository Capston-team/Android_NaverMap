package com.example.naver_map_test;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    public static void setCarrierPreferences(Context context, String carrierKey, String carrierValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(carrierKey, carrierValue);
        editor.apply();
    }

    public static void setRatePreferences(Context context, String rateKey, String rateValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(rateKey, rateValue);
        editor.apply();
    }

    public static String getCarrierPreferences(Context context, String carrierKey) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
        return preferences.getString(carrierKey, null);
    }

    public static String getRatePreferences(Context context, String rateKey) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
//        preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);

        return preferences.getString(rateKey, null);
    }
}
