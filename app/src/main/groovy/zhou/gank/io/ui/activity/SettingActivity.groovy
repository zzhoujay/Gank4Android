package zhou.gank.io.ui.activity

import android.os.Bundle
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.ui.fragment.SettingFragment

@CompileStatic
public class SettingActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        quickFinish()
        setTitle(R.string.title_setting)

        getFragmentManager().beginTransaction().add(R.id.container, new SettingFragment()).commit()
    }


}