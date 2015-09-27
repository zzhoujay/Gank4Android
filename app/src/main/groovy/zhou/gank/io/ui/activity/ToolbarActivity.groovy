package zhou.gank.io.ui.activity

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.ui.fragment.BaseFragment

@CompileStatic
public class ToolbarActivity extends AppCompatActivity {

    Toolbar toolbar
    CoordinatorLayout coordinatorLayout
    BaseFragment currFragment
    boolean quick

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout

        setSupportActionBar(toolbar)
    }

    def quickFinish() {
        def actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        quick = true
    }

    def add(BaseFragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        currFragment = f
    }

    def replace(BaseFragment f) {
        coordinatorLayout.removeAllViews()
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit()
        currFragment = f
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (quick) {
                    finish()
                    return true
                }
                break
        }
        return super.onOptionsItemSelected(item)
    }

    @Override
    boolean onKeyDown(int keyCode, KeyEvent event) {
        return currFragment?.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}