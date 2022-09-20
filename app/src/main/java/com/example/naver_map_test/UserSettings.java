package com.example.naver_map_test;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UserSettings extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        Toolbar settingsToolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(settingsToolbar);
        ActionBar actionBar = getSupportActionBar();


        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_settings_back);
            actionBar.setTitle("환경설정");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.settings, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        Context mContext;
        Activity mActivity;
        SharedPreferences prefs;

        @Override
        public void onAttach(@NonNull Context context) {
            mContext = context;

            if(context instanceof Activity) {
                mActivity = (Activity) context;
            }
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            mActivity = null;
            mContext = null;
            super.onDetach();
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            SharedPreferences preferences = requireActivity().getSharedPreferences("preferences", 0);

            System.out.println("preferences : " + preferences.getAll());

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
            System.out.println("prefs : " + prefs.getAll());
            prefs.registerOnSharedPreferenceChangeListener(prefListener);

            SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(requireActivity());
            Map<String, ?> entrySet = sharedPreferences.getAll();
            System.out.println("entrySet : " + entrySet.get("carrierPreference"));

            // KT VIP
            String carrier = PreferenceUtil.getCarrierPreferences(requireActivity().getApplicationContext(), "carrier");
            String rate = PreferenceUtil.getRatePreferences(requireActivity().getApplicationContext(), "rate");

            ListPreference carrierPreference = findPreference("carrierPreference");
            ListPreference ratePreference = findPreference("ratePreference");

            if(carrier.equals("SKT")) {

                carrierPreference.setValueIndex(0);
                ratePreference.setEntries(R.array.SKT);
                ratePreference.setEntryValues(R.array.SKT);

                CharSequence[] rateList = ratePreference.getEntryValues();
                initRateInfo(ratePreference, rateList, rate);

            }
            if(carrier.equals("KT")) {

                carrierPreference.setValueIndex(1);
                ratePreference.setEntries(R.array.KT);
                ratePreference.setEntryValues(R.array.KT);

                CharSequence[] rateList = ratePreference.getEntryValues();
                initRateInfo(ratePreference, rateList, rate);

            }
            if(carrier.equals("LG")) {

                carrierPreference.setValueIndex(2);
                ratePreference.setEntries(R.array.LG);
                ratePreference.setEntryValues(R.array.LG);

                CharSequence[] rateList = ratePreference.getEntryValues();
                initRateInfo(ratePreference, rateList, rate);
            }
        }

        public void initRateInfo(ListPreference ratePreference, CharSequence[] rateList, String rate) {
            if(Arrays.asList(rateList).contains(rate)) {
                int index = Arrays.asList(rateList).indexOf(rate);

                if(index == -1 || index > Arrays.asList(rateList).size()) {
                    ratePreference.setValueIndex(0);
                } else {
                    ratePreference.setValueIndex(index);
                }
            }
        }


        SharedPreferences.OnSharedPreferenceChangeListener prefListener = (sharedPreferences, key) -> {

//            Map<String, ?> entrySet = sharedPreferences.getAll();
//            if(key.equals("carrierPreference")) {
//
//                ListPreference carrierPreference = findPreference("carrierPreference");
//                ListPreference ratePreference = findPreference("ratePreference");
//
//                System.out.println("carrierPreference : " + entrySet.get("carrierPreference"));
//                System.out.println("ratePreference : " + entrySet.get("ratePreference"));
//
//                if(entrySet.get("carrierPreference") == "SKT") {
//                    System.out.println("carrierPreference - entrySet : " + entrySet.get("carrierPreference"));
//                    assert ratePreference != null;
//                    ratePreference.setEntries(R.array.SKT);
//                    ratePreference.setEntryValues(R.array.SKT);
//                    ratePreference.setValueIndex(0);
//
////                    PreferenceUtil.setCarrierPreferences(mContext, "carrier", "SKT");
//
//                }
//                if(entrySet.get("carrierPreference") == "KT") {
//
//                    assert ratePreference != null;
//                    ratePreference.setEntries(R.array.KT);
//                    ratePreference.setEntryValues(R.array.KT);
//                    ratePreference.setValueIndex(0);
//
//
////                    PreferenceUtil.setCarrierPreferences(mContext, "carrier", "KT");
//
//                }
//                if(entrySet.get("carrierPreference") == "LG") {
//                    assert ratePreference != null;
//                    ratePreference.setEntries(R.array.LG);
//                    ratePreference.setEntryValues(R.array.LG);
//                    ratePreference.setValueIndex(0);
//
////                    PreferenceUtil.setCarrierPreferences(mContext, "carrier", "LG");
//                }
//
//            }
//
//            if(key.equals("ratePreference")) {
//                Toast.makeText(mContext, "ratePreference 클릭", Toast.LENGTH_SHORT).show();
////                PreferenceUtil.setRatePreferences(mContext, "rate", (String) entrySet.get("ratePreference"));
//


            Map<String, ?> entrySet = sharedPreferences.getAll();
            SharedPreferences prefs1 = requireActivity().getSharedPreferences("preferences", 0);
            SharedPreferences.Editor editor = prefs1.edit();
//            System.out.println("prefs1 - carrier: " + prefs1.getString("carrier", ""));
//            System.out.println("prefs1 - rate: " + prefs1.getString("rate", ""));
            System.out.println("entrySet : " + entrySet);

            if(key.equals("carrierPreference")) {

                ListPreference carrierPreference = findPreference("carrierPreference");
                ListPreference ratePreference = findPreference("ratePreference");

                System.out.println("carrierPreference : " + entrySet.get("carrierPreference"));
                System.out.println("ratePreference : " + entrySet.get("ratePreference"));

                if(Objects.requireNonNull(entrySet.get("carrierPreference")).equals("SKT")) {
                    System.out.println("carrierPreference - entrySet : " + entrySet.get("carrierPreference"));
                    ratePreference.setEntries(R.array.SKT);
                    ratePreference.setEntryValues(R.array.SKT);
                    ratePreference.setValueIndex(0);

                    editor.putString("carrier", "SKT");
                    editor.apply();

//                    PreferenceUtil.setRatePreferences(requireActivity().getApplicationContext(), "carrier", "SKT");

                }
                if(Objects.requireNonNull(entrySet.get("carrierPreference")).equals("KT")) {
                    ratePreference.setEntries(R.array.KT);
                    ratePreference.setEntryValues(R.array.KT);
                    ratePreference.setValueIndex(0);

                    editor.putString("carrier", "KT");
                    editor.apply();
//                    PreferenceUtil.setRatePreferences(requireActivity().getApplicationContext(), "carrier", "KT");
                }
                if(Objects.requireNonNull(entrySet.get("carrierPreference")).equals("LG")) {
                    ratePreference.setEntries(R.array.LG);
                    ratePreference.setEntryValues(R.array.LG);
                    ratePreference.setValueIndex(0);

                    editor.putString("carrier", "LG");
                    editor.apply();
//                    PreferenceUtil.setRatePreferences(requireActivity().getApplicationContext(), "carrier", "LG");
                }

            }

            if(key.equals("ratePreference")) {
//                Toast.makeText(requireContext(), "ratePreference 클릭", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}