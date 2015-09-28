package zhou.gank.io

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import groovy.transform.CompileStatic
import org.litepal.tablemanager.Connector
import zhou.gank.io.model.Bookmark
import zhou.gank.io.ui.activity.HomeActivity

@CompileStatic
class MainActivity extends AppCompatActivity {

    FloatingActionButton fab

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab) as FloatingActionButton
        fab.postDelayed(this.&start, 400)
    }


    def void start() {
        View parent = fab.getParent() as View
        float scale = Math.sqrt(parent.getHeight() * parent.getHeight() + parent.getWidth() * parent.getWidth()) / fab.getHeight() as float
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", scale)
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", scale)
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fab as Object, holderX, holderY).setDuration(500)
        animator.setInterpolator(new AccelerateDecelerateInterpolator())
        animator.start()
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation)
                Open()
            }
        })
    }

    public void Open(View view = null) {
//        Intent intent = new Intent(Intent.ACTION_SEND)
        //Uri.parse("http://ww4.sinaimg.cn/large/7a8aed7bjw1ewdab2qvtmj20qo0hsdkg.jpg")
//        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File("/sdcard/UCDownloads/doge.jpg")) )
//        intent.setType("image/*")
//        startActivity(intent)
        startActivity(new Intent(this, HomeActivity.class));
        finish()
    }
}