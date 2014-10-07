package activities.safepassbeta;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import utilities.safepassbeta.Utility;


public class UserSettingsActivity extends Activity {

    static int defaultVal = 0;
    private boolean onRestart = false;

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            bindPreferenceSummaryToValue(findPreference("tries_list"));
            bindPreferenceSummaryToValue(findPreference("autolock_list"));
            findPreference("password_length").setSummary("Length: " + defaultVal);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + Utility.BACKUP_FOLDER
                        + File.separator + Utility.BACKUP_FILE);
                if (file.exists()) {
                    findPreference("backup_location").setSummary(file.getAbsolutePath());
                } else {
                    findPreference("backup_location").setSummary("No backup created");
                }
            } else {
                findPreference("backup_location").setSummary("No SDCard found");
            }

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            if(s.equals("password_length")) {
                int value = sharedPreferences.getInt("password_length", 0);
                findPreference("password_length").setSummary("Length: " + value);
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }
    }

    // On back button pressed, show lock screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            Utility.fromAdd = true;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // On return from background
    @Override
    protected void onRestart() {
        super.onRestart();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        onRestart = true;
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!onRestart) {
            // Toast.makeText(getApplicationContext(), "on Pause", Toast.LENGTH_SHORT).show();
            Utility.currentTime = System.currentTimeMillis() / 1000;
        }
    }

    // call super.onResume
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("SafePass Settings");
        actionBar.setDisplayHomeAsUpEnabled(false);
        // Display the fragment as the main content.
        SharedPreferences sharedPreferences = getSharedPreferences(Utility.PREFS_FILE, MODE_PRIVATE);
        defaultVal = sharedPreferences.getInt("password_length", 10);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
}
