package zhou.gank.io.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.fragment.ImageGalleryFragment

@CompileStatic
public class ImageGalleryActivity extends ToolbarActivity {

    ArrayList<String> urls

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        quickFinish()

        Intent i = getIntent()
        urls = i.getStringArrayListExtra(Config.Static.URLS)
        add(ImageGalleryFragment.newInstance(urls, i.getIntExtra(Config.Static.POSITION, 0)))
    }
}