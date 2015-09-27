package zhou.gank.io.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.fourmob.datetimepicker.date.DatePickerDialog
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.dialog.InfoDialog
import zhou.gank.io.ui.fragment.DailyFragment
import zhou.gank.io.util.Notifier
import zhou.gank.io.util.TextKit
import zhou.gank.io.util.TimeKit

@CompileStatic
class HomeActivity extends AppCompatActivity implements Notifier {

    static int needRecreate

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    private DailyFragment dailyFragment;
    private Fragment currFragment;
    private boolean isRecreate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        dailyFragment = DailyFragment.newInstance(-1, -1, -1, true)

        add(dailyFragment)

        App.addNotifier(this)
    }

    def initView() {
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout

        navigationView.setNavigationItemSelectedListener({ item ->
            def i = item as MenuItem
            drawerLayout.closeDrawers()
            switch (i.getItemId()) {
                case R.id.nav_daily:
                    replace(dailyFragment)
                    return true
                case R.id.nav_type:
                    Intent intent = new Intent(this, TabActivity.class)
                    intent.putExtra(Config.Static.IS_RANDOM, false)
                    App.getInstance().getMainHandler().postDelayed({
                        startActivity(intent)
                    }, 250)
                    return true
                case R.id.nav_random:
                    Intent intent = new Intent(this, TabActivity.class)
                    intent.putExtra(Config.Static.IS_RANDOM, true)
                    App.getInstance().getMainHandler().postDelayed({
                        startActivity(intent)
                    }, 250)
                    return true
                case R.id.nav_info:
                    def info = InfoDialog.newInstance(getString(R.string.nav_info), TextKit.getInfo())
                    info.show(getSupportFragmentManager(), "info")
                    return true
                case R.id.nav_setting:
                    def intent1 = new Intent(this, SettingActivity.class)
                    App.getInstance().getMainHandler().postDelayed({
                        startActivity(intent1)
                    }, 200)
                    return true
            }
            return false
        })

    }

    def add(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        this.currFragment = f;
    }

    def replace(Fragment f) {
        if (currFragment == f) {
            return
        }
        coordinatorLayout.removeAllViews()
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit()
        this.currFragment = f
    }

    def remove() {
        if (currFragment) {
            getSupportFragmentManager().beginTransaction().remove(currFragment).commit()
            coordinatorLayout.removeAllViews()
            this.currFragment = null
        }
    }

    @Override
    void notice(int noticeId) {
        switch (noticeId) {
            case Config.Action.OPEN_DRAWER_LAYOUT:
                drawerLayout.openDrawer(GravityCompat.START)
                break
            case Config.Action.CHANGE_DATE:
                def times = TimeKit.getTime()
                DatePickerDialog dialog = DatePickerDialog.newInstance({ picker, year, month, day ->
                    Intent i = new Intent(this, DailyActivity.class)
                    i.putExtra(Config.Static.YEAR, year as int)
                    i.putExtra(Config.Static.MONTH, (month as int) + 1)
                    i.putExtra(Config.Static.DAY, day as int)
                    startActivity(i)
                }, times[0], times[1] - 1, times[2])
                dialog.setVibrate(false)
                dialog.show(getSupportFragmentManager(), "date")
                break
            case Config.Action.RESTART:
                needRecreate = 1
                break
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy()
        App.removeNotifier(this)
    }

    @Override
    protected void onResume() {
        super.onResume()
        if (needRecreate == 1) {
            remove()
            recreate()
            needRecreate++
            return
        }
        if (needRecreate == 2) {
            App.getInstance().getMainHandler().postDelayed({
                dailyFragment.requestData()
            }, 200)
            needRecreate = 0
        }
    }
}