package com.reshmi.james.popularmovies.ui.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import com.reshmi.james.popularmovies.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);
    }
}
