//package com.example.naver_map_test;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.widget.Toolbar;
//import androidx.preference.ListPreference;
//import androidx.preference.PreferenceFragmentCompat;
//import androidx.preference.PreferenceManager;
//
//import java.util.Map;
//
//public class UserSettingsFragment extends PreferenceFragmentCompat {
//
//    SharedPreferences prefs;
//
//    ListPreference carrierPreference;
//    ListPreference ratePreference;
//
//
//
//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//
//
//        addPreferencesFromResource(R.xml.root_preferences);
//
//        carrierPreference = (ListPreference) findPreference("carrierPreference");
//        ratePreference = findPreference("ratePreference");
//
//        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
//
//        if(!prefs.getString("carrierPreference", "").equals("")) {
//            carrierPreference.setSummary(prefs.getString("carrierPreference", "통신사 변경"));
//        }
//
//        if(!prefs.getString("ratePreference", "").equals("")) {
//            ratePreference.setSummary(prefs.getString("ratePreference", "등급 변경"));
//        }
//
//        prefs.registerOnSharedPreferenceChangeListener(prefListener);
//    }
//
//    SharedPreferences.OnSharedPreferenceChangeListener prefListener = (sharedPreferences, key) -> {
//
//        Map<String, ?> entrySet = sharedPreferences.getAll();
//
//        if(key.equals("carrierPreference")) {
//
//            if(entrySet.get("carrierPreference") == "SKT") {
//                System.out.println("ratePreference - entrySet : " + entrySet.get("ratePreference"));
//                ratePreference.setEntries(R.array.SKT);
//                ratePreference.setEntryValues(R.array.SKT);
//                ratePreference.setValueIndex(0);
//
//            } else if(entrySet.get("carrierPreference") == "KT") {
//                System.out.println("ratePreference - entrySet : " + entrySet.get("ratePreference"));
//                ratePreference.setEntries(R.array.KT);
//                ratePreference.setEntryValues(R.array.KT);
//                ratePreference.setValueIndex(0);
//
//            } else {
//                System.out.println("ratePreference - entrySet : " + entrySet.get("ratePreference"));
//                ratePreference.setEntries(R.array.LG);
//                ratePreference.setEntryValues(R.array.LG);
//                ratePreference.setValueIndex(0);
//            }
//        }
//
//        if(key.equals("ratePreference")) {
//            Toast.makeText(requireContext(), "ratePreference 클릭", Toast.LENGTH_SHORT).show();
//        }
//    };
//}
