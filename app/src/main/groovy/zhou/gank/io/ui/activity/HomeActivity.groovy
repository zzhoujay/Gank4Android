package zhou.gank.io.ui.activity

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.ui.fragment.DailyFragment

@CompileStatic
class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;

    private DailyFragment dailyFragment;
    private Fragment currFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        dailyFragment = new DailyFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, dailyFragment).commit();
    }

    def initView() {
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        navigationView = findViewById(R.id.nav_view) as NavigationView
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout
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