package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.preference.PreferenceFragment;
import groovy.transform.CompileStatic
import zhou.gank.io.R

@CompileStatic
public class SettingFragment extends PreferenceFragment {

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.setting)
    }
}