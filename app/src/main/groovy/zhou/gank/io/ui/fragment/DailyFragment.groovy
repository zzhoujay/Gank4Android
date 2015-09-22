package zhou.gank.io.ui.fragment
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.model.GankDaily
import zhou.gank.io.model.ResultDaily
import zhou.gank.io.ui.adapter.DailyAdapter
import zhou.gank.io.util.JsonKit

@CompileStatic
class DailyFragment extends BaseFragment {

    ImageView icon;
    RecyclerView recyclerView;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private DailyAdapter dailyAdapter;


    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);

        toolbar = view.findViewById(R.id.toolbar) as Toolbar
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        icon = view.findViewById(R.id.icon) as ImageView

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_48px);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        String title = getString(R.string.app_name);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);

        dailyAdapter = new DailyAdapter();
        ResultDaily result = JsonKit.generate(JsonKit.test, App.getInstance().getGson());
        dailyAdapter.setDaily(result.daily);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(dailyAdapter);

        GankDaily daily = result.daily;
        Picasso.with(getActivity()).load(daily.ganks.get(daily.types.indexOf("福利")).get(0).url).into(icon);

        return view;
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}