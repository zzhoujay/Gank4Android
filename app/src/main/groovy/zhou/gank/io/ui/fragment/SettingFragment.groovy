package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.widget.Toast;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R

@CompileStatic
public class SettingFragment extends PreferenceFragment {

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)

        ListPreference themes = findPreference(getString(R.string.key_theme)) as ListPreference
        themes.setOnPreferenceChangeListener({ preference, o ->
            App.themeChanged()
            getActivity().recreate()
            Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_SHORT).show()
            return true
        })
    }

    @Override
    boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        switch (preference.getKey()) {
            case getString(R.string.key_clear):
                def g = App.cacheFile().deleteDir()
                if (g) {
                    App.toast(R.string.success_clear)
                }
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }
}