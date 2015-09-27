package zhou.gank.io

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import groovy.transform.CompileStatic
import zhou.gank.io.ui.activity.HomeActivity
import zhou.gank.io.ui.activity.TabActivity

@CompileStatic
class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }

    public void Open(View view) {
//        Intent intent = new Intent(Intent.ACTION_SEND)
        //Uri.parse("http://ww4.sinaimg.cn/large/7a8aed7bjw1ewdab2qvtmj20qo0hsdkg.jpg")
//        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File("/sdcard/UCDownloads/doge.jpg")) )
//        intent.setType("image/*")
//        startActivity(intent)
        startActivity(new Intent(this, HomeActivity.class));
    }
}