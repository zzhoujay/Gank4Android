package zhou.gank.io.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import butterknife.Bind
import butterknife.ButterKnife
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.ui.fragment.DailyFragment

@CompileStatic
class HomeActivity extends AppCompatActivity{

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private DailyFragment dailyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        dailyFragment = new DailyFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, dailyFragment).commit();
    }

}