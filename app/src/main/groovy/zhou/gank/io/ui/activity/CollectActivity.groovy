package zhou.gank.io.ui.activity

import android.os.Bundle
import android.support.annotation.Nullable;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.ui.fragment.GankFragment

@CompileStatic
public class CollectActivity extends ToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (App.themeIsLight()) {
            setTheme(R.style.AppTheme_Tab)
        } else {
            setTheme(R.style.AppTheme_TabDark)
        }
        super.onCreate(savedInstanceState)
        quickFinish()
        setTitle(R.string.nav_collect)

        add(GankFragment.newInstance(null, true, false, true))
    }
}