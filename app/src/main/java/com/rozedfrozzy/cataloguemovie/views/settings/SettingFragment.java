package com.rozedfrozzy.cataloguemovie.views.settings;


import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import com.rozedfrozzy.cataloguemovie.R;
import com.rozedfrozzy.cataloguemovie.scheduler.DailyReminderReceiver;
import com.rozedfrozzy.cataloguemovie.scheduler.ReleaseReminderReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    private DailyReminderReceiver dailyReminderReceiver = new DailyReminderReceiver();
    private ReleaseReminderReceiver releaseReminderReceiver = new ReleaseReminderReceiver();

    String setting_locale, daily_reminder, release_reminder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        daily_reminder = getString(R.string.key_reminder_daily);
        release_reminder = getString(R.string.key_reminder_release);
        setting_locale = getString(R.string.key_setting_locale);

        findPreference(daily_reminder).setOnPreferenceChangeListener(this);
        findPreference(release_reminder).setOnPreferenceChangeListener(this);
        findPreference(setting_locale).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            }
        });

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        boolean isOn = (boolean) newValue;

        if (key.equals(daily_reminder)){
            if (isOn){
                dailyReminderReceiver.setRepeatingAlarm(getActivity(), dailyReminderReceiver.TYPE_REPEATING, "07:00", getString(R.string.label_alarm_daily_reminder));
            } else {
                dailyReminderReceiver.cancelAlarm(getActivity(), dailyReminderReceiver.TYPE_REPEATING);
            }
            return true;
        }

        if (key.equals(release_reminder)){
            if (isOn){
                releaseReminderReceiver.setRepeatingAlarm(getActivity(), releaseReminderReceiver.TYPE_REPEATING, "08:00");
            } else {
                releaseReminderReceiver.cancelAlarm(getActivity(), releaseReminderReceiver.TYPE_REPEATING);
            }
            return true;
        }

        return false;
    }
}
