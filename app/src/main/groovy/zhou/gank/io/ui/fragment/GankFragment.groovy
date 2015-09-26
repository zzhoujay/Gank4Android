package zhou.gank.io.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.data.DataManager
import zhou.gank.io.data.DataProvider
import zhou.gank.io.data.RandomProvider
import zhou.gank.io.data.TypeProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.activity.WebActivity
import zhou.gank.io.ui.adapter.BaseAdapter
import zhou.gank.io.ui.adapter.GankAdapter
import zhou.gank.io.ui.adapter.ImageAdapter
import zhou.gank.io.util.NumKit
import zhou.widget.AdvanceAdapter

@CompileStatic
public class GankFragment extends AdvanceFragment {

    DataProvider provider
    String type
    boolean isRandom, isImage
    View more
    LinearLayoutManager manager

    private BaseAdapter adapter

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        def b = getArguments()
        if (b) {
            type = b.getString(Config.Static.TYPE)
            isRandom = b.getBoolean(Config.Static.IS_RANDOM, false)
            isImage = b.getBoolean(Config.Static.IS_IMAGE, false)
        }

        int size = NumKit.getNum(Config.getString(getString(R.string.key_num), "$Config.Configurable.DEFAULT_SIZE"), Config.Configurable.DEFAULT_SIZE)

        if (isRandom) {
            provider = new RandomProvider(type, size)
        } else {
            provider = new TypeProvider(type, size)
        }

        if (isImage) {
            adapter = new ImageAdapter()
        } else {
            adapter = new GankAdapter()
        }

        adapter.setClickListener { gank ->
            def gs = gank as Gank
            boolean flag = Config.getBoolean(getString(R.string.key_open), true)
            if (!flag) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gs.url))
                startActivity(intent)
            } else {
                Intent intent = new Intent(getActivity(), WebActivity.class)
                intent.putExtra(Config.Static.URL, gs.url)
                intent.putExtra(Config.Static.TITLE, gs.desc)
                startActivity(intent)
            }
        }
    }


    public void setUpData(List<Gank> ganks) {
        if (ganks != null) {
            if (ganks.isEmpty()) {
                hiddenMore()
            } else {
                onSuccess()
                adapter.setGanks(ganks)
                showMore()
            }
        } else {
            onFailure()
        }
    }


    @Override
    protected void initView(View v) {
        super.initView(v)

        manager = new LinearLayoutManager(getActivity())
        recyclerView.setLayoutManager(manager)

        if (!isRandom) {
            more = LayoutInflater.from(getActivity()).inflate(R.layout.layout_more, null)
            AdvanceAdapter advanceAdapter = new AdvanceAdapter(adapter)
            advanceAdapter.addFooter(more)
            recyclerView.setAdapter(advanceAdapter)

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (manager.findLastVisibleItemPosition() == advanceAdapter.getItemCount() - 1) {
                            more();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                }
            });

            hiddenMore()

        } else {
            recyclerView.setAdapter(adapter)
        }



        swipeRefreshLayout.setRefreshing(true)
        DataManager.getInstance().get(provider, this.&setUpData)

    }

    @Override
    protected void requestRefresh() {
        super.requestRefresh()
        DataManager.getInstance().update(provider, this.&setUpData)
    }

    protected void more() {
        DataManager.getInstance().more(provider, this.&setUpData)
    }

    protected void hiddenMore() {
        more?.setVisibility(View.GONE)
    }

    protected void showMore() {
        if (manager.getItemCount() > manager.findLastVisibleItemPosition() - manager.findFirstVisibleItemPosition() + 1)
            more?.setVisibility(View.VISIBLE)
    }

    void onClick(Gank gank) {
        println(this.class.name)
        Toast.makeText(getActivity(), gank.toString(), Toast.LENGTH_SHORT).show()
    }

    static GankFragment newInstance(String type, boolean isRandom = false, boolean isImage = false) {
        GankFragment fragment = new GankFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.TYPE, type)
        bundle.putBoolean(Config.Static.IS_RANDOM, isRandom)
        bundle.putBoolean(Config.Static.IS_IMAGE, isImage)
        fragment.setArguments(bundle)
        fragment
    }
}