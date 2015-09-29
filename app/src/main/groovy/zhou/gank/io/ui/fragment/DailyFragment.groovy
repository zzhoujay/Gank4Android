package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.data.DataManager
import zhou.gank.io.data.TimeProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.model.GankDaily
import zhou.gank.io.ui.adapter.DailyAdapter
import zhou.gank.io.util.TimeKit

@CompileStatic
class DailyFragment extends BaseFragment {

//    ImageView icon;
    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TimeProvider provider
    DailyAdapter dailyAdapter;
    ViewPager viewPager
    FloatingActionButton fab
    View loading, loadingProgress, empty, error
    boolean isMain = false
    int year, month, day
    int count


    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        def b = getArguments()
        List<Integer> time = TimeKit.getTime()
        year = time[0]
        month = time[1]
        day = time[2]
        if (b) {
            year = b.getInt(Config.Static.YEAR, year)
            month = b.getInt(Config.Static.MONTH, month)
            day = b.getInt(Config.Static.DAY, day)
            isMain = b.getBoolean(Config.Static.IS_MAIN, false)
        }

        provider = new TimeProvider(year, month, day)
    }

    @Nullable
    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        viewPager = view.findViewById(R.id.viewpager) as ViewPager
        fab = view.findViewById(R.id.fab) as FloatingActionButton
        loading = view.findViewById(R.id.loading)
        loadingProgress = view.findViewById(R.id.progressBar)
        empty = view.findViewById(R.id.no_data)
        error = view.findViewById(R.id.error)

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            if (isMain)
                ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setTitle(R.string.app_name);

        dailyAdapter = new DailyAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()))
        recyclerView.setAdapter(dailyAdapter)
        requestDaily()

        fab.setOnClickListener({ v ->
            noticeActivity(Config.Action.CHANGE_DATE)
        })

        error.setOnClickListener({ v ->
            requestDaily()
        })

        return view;
    }


    protected void setUpData(GankDaily daily) {
        if (daily != null) {
            if (daily.isEmpty()) {
                if (isMain) {
                    //为主页的情况
                    if (count > Config.Configurable.MAX_iteration) {
                        //重复加载次数过多(到达了底端)
                        setTitle(provider.year, provider.month, provider.day)
                        setEmpty()
                    } else {
                        //加载前一天的数据
                        if (count == 0) {
                            // 重新加载今天的内容
                            DataManager.getInstance().update(provider, this.&setUpData)
                            count++
                        } else {
                            count++
                            provider = provider.getPrevDay()
                            if (TimeKit.future(provider.year, provider.month, provider.day)) {
                                //如如果要加载的数据是今天或以后
                                DataManager.getInstance().update(provider, this.&setUpData)
                            } else {
                                DataManager.getInstance().get(provider, this.&setUpData)
                            }
                        }
                    }
                } else {
                    // Empty
                    setTitle(provider.year, provider.month, provider.day)
                    setEmpty()
                }
            } else {
                // Success
                setTitle(provider.year, provider.month, provider.day)
                List<List<Gank>> ganks = daily.ganks
                List<String> types = daily.types
                List<Gank> welfares = ganks.get(types.indexOf(Config.Type.WELFARE))
                int size = welfares?.size()
                Fragment[] fs = new Fragment[size]
                size.times {
                    int index = it as int
                    fs[index] = ImagePageFragment.newInstance(welfares.get(index).url)
                }

                PagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
                    @Override
                    Fragment getItem(int i) {
                        fs[i]
                    }

                    @Override
                    int getCount() {
                        return fs.length
                    }
                } as PagerAdapter

                viewPager.setAdapter(adapter)

                dailyAdapter.setDaily(daily)

                setSuccess()
            }
        } else {
            if(daily){
                println("not null")
            }else {
                println("is null")
            }
            // Error
            setTitle(provider.year, provider.month, provider.day)
            setError()
        }
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isMain) {
                    noticeActivity(Config.Action.OPEN_DRAWER_LAYOUT)
                } else {
                    noticeActivity(Config.Action.FINISH)
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
    }

    def requestDaily() {
        setTitle(getString(R.string.loading))
        setLoading()
        DataManager.getInstance().get(provider, this.&setUpData)
    }

    def requestData() {
        DataManager.getInstance().get(provider, this.&setUpData)
    }

    def setLoading() {
        loading.setVisibility(View.VISIBLE)
        loadingProgress.setVisibility(View.VISIBLE)
        empty.setVisibility(View.GONE)
        error.setVisibility(View.GONE)
    }

    def setSuccess() {
        loading.setVisibility(View.INVISIBLE)
    }

    def setError() {
        loading.setVisibility(View.VISIBLE)
        loadingProgress.setVisibility(View.GONE)
        empty.setVisibility(View.INVISIBLE)
        error.setVisibility(View.VISIBLE)
    }

    def setEmpty() {
        loading.setVisibility(View.VISIBLE)
        loadingProgress.setVisibility(View.GONE)
        empty.setVisibility(View.VISIBLE)
        error.setVisibility(View.GONE)
    }

    protected void setTitle(int year, int month, int day) {
        setTitle("${year}${getString(R.string.year)}${month}${getString(R.string.month)}${day}${getString(R.string.day)}")
    }

    static DailyFragment newInstance(int year = -1, int month = -1, int day = -1, boolean isMain = false) {
        DailyFragment fragment = new DailyFragment()
        Bundle bundle = new Bundle()
        if (year > 0 && month > 0 && day > 0) {
            bundle.putInt(Config.Static.YEAR, year)
            bundle.putInt(Config.Static.MONTH, month)
            bundle.putInt(Config.Static.DAY, day)
        }
        bundle.putBoolean(Config.Static.IS_MAIN, isMain)
        fragment.setArguments(bundle)
        return fragment
    }
}