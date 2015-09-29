package zhou.gank.io

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import groovy.transform.CompileStatic
import zhou.gank.io.database.DatabaseManager
import zhou.gank.io.model.Bookmark
import zhou.gank.io.ui.activity.HomeActivity

@CompileStatic
class MainActivity extends AppCompatActivity {

    FloatingActionButton fab
    TextView textView
    View background

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        if (App.hasStarted) {
            Open()
            return
        } else {
            App.hasStarted = true
        }
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab) as FloatingActionButton
        textView = findViewById(R.id.textView) as TextView
        background = findViewById(R.id.background)
        fab.postDelayed(this.&start, 200)
    }


    def void start() {
        View parent = fab.getParent() as View
        float scale = Math.sqrt(parent.getHeight() * parent.getHeight() + parent.getWidth() * parent.getWidth()) / fab.getHeight() as float
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("scaleX", scale)
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("scaleY", scale)
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fab as Object, holderX, holderY).setDuration(500)
        animator.setInterpolator(new AccelerateDecelerateInterpolator())
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation)
                background.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.material_purple_500) as int)
                fab.setVisibility(View.GONE)
                textView.setVisibility(View.VISIBLE)
            }
        })
        parent.getAlpha()


        PropertyValuesHolder holderA = PropertyValuesHolder.ofFloat("alpha", 0, 1)
        PropertyValuesHolder holderYm = PropertyValuesHolder.ofFloat("translationY", 0, 300)
        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(textView as Object, holderA, holderYm).setDuration(700)
        a.setInterpolator(new AccelerateDecelerateInterpolator())
        a.setStartDelay(500)

        a.addListener(new AnimatorListenerAdapter() {
            @Override
            void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation)
                Open()
            }
        })

        animator.start()
        a.start()
    }

    public void Open(View view = null) {
        startActivity(new Intent(this, HomeActivity.class));
        finish()
    }
}