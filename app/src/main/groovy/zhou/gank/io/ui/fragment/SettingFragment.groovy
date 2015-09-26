package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R

@CompileStatic
public class SettingFragment extends PreferenceFragment {

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
    }

    @Override
    boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case getString(R.string.key_clear):
                def g = App.cacheFile().deleteDir()
                if (g) {
                    App.toast(R.string.success_clear)
                }
                return g
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }
}