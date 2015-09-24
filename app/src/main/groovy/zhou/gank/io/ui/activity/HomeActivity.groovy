package zhou.gank.io.ui.activity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.fragment.DailyFragment
import zhou.gank.io.ui.fragment.GankFragment

@CompileStatic
class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    private DailyFragment dailyFragment;
    private GankFragment androidFragment;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        dailyFragment = new DailyFragment();
        androidFragment = GankFragment.newInstance(Config.Type.ANDROID,true) as GankFragment

        add(dailyFragment)
    }

    def initView() {
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout

        navigationView.setNavigationItemSelectedListener({ item ->
            def i=item as MenuItem
            drawerLayout.closeDrawers()
            switch (i.getItemId()) {
                case R.id.nav_android:
                    replace(androidFragment)
                    return true
                case R.id.nav_daily:
                    replace(dailyFragment)
                    return true
            }
        })
    }

    def add(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        this.currFragment = f;
    }

    def replace(Fragment f) {
        coordinatorLayout.removeAllViews()
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit()
        this.currFragment = f
    }

}