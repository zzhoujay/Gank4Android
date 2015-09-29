package zhou.gank.io.ui.activity

import android.os.Bundle
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.fragment.WebFragment

@CompileStatic
public class WebActivity extends ToolbarActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        quickFinish()
        def i = getIntent()

        String title
        title = i.getStringExtra(Config.Static.TITLE)
        if (title == null) {
            title = getString(R.string.app_name)
        }
        setTitle(title)

        add(WebFragment.newInstance(i.getStringExtra(Config.Static.URL), title))
    }
}