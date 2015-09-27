package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import groovy.transform.CompileStatic
import zhou.gank.io.R

@CompileStatic
public class AdvanceFragment extends BaseFragment {

    SwipeRefreshLayout swipeRefreshLayout
    RecyclerView recyclerView
    View error

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyler_view, container, false)
        initView(v)
        return v
    }

    @Override
    protected void initView(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView = v.findViewById(R.id.recyclerView) as RecyclerView

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeRefreshLayout.setOnRefreshListener(this.&requestRefresh)

        error = v.findViewById(R.id.error)

    }

    protected void onFailure() {
        swipeRefreshLayout.setRefreshing(false)
        error.setVisibility(View.VISIBLE)
    }

    protected void onSuccess() {
        swipeRefreshLayout.setRefreshing(false)
        recyclerView.setVisibility(View.VISIBLE)
        error.setVisibility(View.GONE)
    }

    protected void requestRefresh(View v = null) {
        swipeRefreshLayout.setRefreshing(true)
        error.setVisibility(View.GONE)
    }

}