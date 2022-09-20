package com.example.naver_map_test;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    public static void setCarrierPreferences(Context context, String carrierKey, String carrierValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(carrierKey, carrierValue);
        editor.apply();
    }

    public static void setRatePreferences(Context context, String rateKey, String rateValue) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(rateKey, rateValue);
        editor.apply();
    }

    /**
     *
     * @param context 해당 액티비티의 Context
     * @param carrierKey key값 -> carrier
     * @return  Preference에 저장되어 있는 값
     */
    public static String getCarrierPreferences(Context context, String carrierKey) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
        return preferences.getString(carrierKey, null);
    }


    /**
     *
     * @param context 해당 액티비티의 Context
     * @param rateKey key값 -> rate
     * @return  Preference에 저장되어 있는 값
     */
    public static String getRatePreferences(Context context, String rateKey) {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
//        preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);

        return preferences.getString(rateKey, null);
    }
}
