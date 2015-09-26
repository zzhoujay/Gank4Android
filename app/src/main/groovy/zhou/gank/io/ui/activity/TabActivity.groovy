package zhou.gank.io.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem;
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.fragment.GankFragment

@CompileStatic
public class TabActivity extends AppCompatActivity {

    boolean isRandom

    Toolbar toolbar
    TabLayout tabLayout
    ViewPager viewPager
    int[] ids = [R.string.nav_android, R.string.nav_ios, R.string.nav_recommend, R.string.nav_resource, R.string.nav_welfare, R.string.nav_video] as int[]
    Fragment[] fragments

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        Intent intent = getIntent()
        isRandom = intent.getBooleanExtra(Config.Static.IS_RANDOM, false)

        fragments = new Fragment[6]

        fragments[0] = GankFragment.newInstance(Config.Type.ANDROID, isRandom)
        fragments[1] = GankFragment.newInstance(Config.Type.IOS, isRandom)
        fragments[2] = GankFragment.newInstance(Config.Type.RECOMMEND, isRandom)
        fragments[3] = GankFragment.newInstance(Config.Type.RESOURCES, isRandom)
        fragments[4] = GankFragment.newInstance(Config.Type.WELFARE, isRandom, true)
        fragments[5] = GankFragment.newInstance(Config.Type.VIDEO, isRandom)

        initView()
    }

    void initView() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        ActionBar actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)

        tabLayout = findViewById(R.id.tabs) as TabLayout
        viewPager = findViewById(R.id.viewpager) as ViewPager

        ids.each { tabLayout.addTab(tabLayout.newTab().setText(getString(it as Integer))) }

        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            Fragment getItem(int position) {
                fragments[position]
            }

            @Override
            int getCount() {
                return fragments.length
            }

            @Override
            CharSequence getPageTitle(int position) {
                return getString(ids[position])
            }
        }
        viewPager.setAdapter(adapter)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setTabsFromPagerAdapter(adapter)

        if (isRandom) {
            setTitle(R.string.nav_random)
        } else {
            setTitle(R.string.nav_type)
        }
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish()
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}