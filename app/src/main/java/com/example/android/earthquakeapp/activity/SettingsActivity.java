package com.example.android.earthquakeapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.earthquakeapp.Configurations;
import com.example.android.earthquakeapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            final Preference minMagnitude = findPreference(getString(R.string.settings_min_magnitude_key));
            bindPreferenceSummaryToValue(minMagnitude);

            final Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);

            final Preference limitRow = findPreference(getString(R.string.settings_limit_row_key));
            bindPreferenceSummaryToValue(limitRow);

            final Preference startTimeLimit = findPreference(getString(R.string.settings_start_time_limit_key));
            bindPreferenceSummaryToValue(startTimeLimit);

            final Preference webOpen = findPreference(getString(R.string.settings_web_open_key));
            bindPreferenceSummaryToValue(webOpen);

            final Preference mapOpne = findPreference(getString(R.string.settings_map_open_key));
            bindPreferenceSummaryToValue(mapOpne);
        }

        /**
         * callback to notify change to settings
         *
         * @param preference preference to update
         * @param newValue   new value
         * @return to prevent a proposed preference change by returning false
         */
        @Override
        public boolean onPreferenceChange(@NonNull final Preference preference, @NonNull final Object newValue) {
            final String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                final ListPreference listPreference = (ListPreference) preference;
                final int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    final CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            Configurations.SETTINGS_CHANGED = true;
            return true;
        }

        /**
         * method to attach listener and show value saved on SharedPreferences
         *
         * @param preference
         */
        private void bindPreferenceSummaryToValue(@NonNull final Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            final String preferenceString = preferences.getString(preference.getKey(), "");
            if (preferenceString != null) {
                onPreferenceChange(preference, preferenceString);
            }
        }
    }
}
