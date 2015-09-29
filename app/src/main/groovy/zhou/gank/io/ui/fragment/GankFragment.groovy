package zhou.gank.io.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.data.CollectProvider
import zhou.gank.io.data.DataManager
import zhou.gank.io.data.DataProvider
import zhou.gank.io.data.RandomProvider
import zhou.gank.io.data.TypeProvider
import zhou.gank.io.database.DatabaseManager
import zhou.gank.io.model.Bookmark
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.activity.ImageGalleryActivity
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
    boolean isRandom, isImage, loadMoreProgress, isBookmark
    View more
    LinearLayoutManager manager
    ProgressBar progressBar
    TextView moreText
    private BaseAdapter adapter

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        def b = getArguments()
        if (b) {
            type = b.getString(Config.Static.TYPE)
            isRandom = b.getBoolean(Config.Static.IS_RANDOM, false)
            isImage = b.getBoolean(Config.Static.IS_IMAGE, false)
            isBookmark = b.getBoolean(Config.Static.IS_BOOKMARK, false)
        }

        int size = NumKit.getNum(Config.getString(getString(R.string.key_num), "$Config.Configurable.DEFAULT_SIZE"), Config.Configurable.DEFAULT_SIZE)

        if (isBookmark) {
            provider = new CollectProvider()
        } else {
            if (isRandom) {
                provider = new RandomProvider(type, size)
            } else {
                provider = new TypeProvider(type, size)
            }
        }

        if (isImage) {
            adapter = new ImageAdapter()
        } else {
            adapter = new GankAdapter()
        }

        adapter.setClickListener { gank, p = null ->
            def gs = gank as Gank
            boolean flag = Config.getBoolean(getString(R.string.key_open), true)
            if (!flag) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gs.url))
                startActivity(intent)
            } else {
                Intent intent
                if (isImage) {
                    intent = new Intent(getActivity(), ImageGalleryActivity.class)
                    ArrayList<Gank> ganks = provider.get() as ArrayList<Gank>
                    ArrayList<String> urls = new ArrayList<>(ganks.size())
                    ganks.each {
                        urls << it.url
                    }
                    intent.putStringArrayListExtra(Config.Static.URLS, urls)
                    intent.putExtra(Config.Static.POSITION, p as int)
                } else {
                    intent = new Intent(getActivity(), WebActivity.class)
                    intent.putExtra(Config.Static.URL, gs.url)
                    intent.putExtra(Config.Static.TITLE, gs.desc)
                }
                startActivity(intent)
            }
        }

        if (isBookmark) {
            adapter.setLongClickListener { v, gank, p ->
                def g = gank as Gank
                def view = v as View
                PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END)
                Menu menu = popupMenu.getMenu()
                popupMenu.getMenuInflater().inflate(R.menu.menu_pop, menu)
                popupMenu.setOnMenuItemClickListener({ item ->
                    def i = item as MenuItem
                    switch (i.getItemId()) {
                        case R.id.menu_delete:
                            DatabaseManager.getInstance().delete(g.url)
                            adapter.removeItem(p as int)
                            Toast.makeText(getActivity(), R.string.success_delete, Toast.LENGTH_SHORT).show()
                            return true
                    }
                    return false
                })
                popupMenu.show()
                return true
            }
        } else {
            adapter.setLongClickListener { v, gank, p ->
                def g = gank as Gank
                def view = v as View
                PopupMenu popupMenu = new PopupMenu(getActivity(), view, Gravity.END)
                Menu menu = popupMenu.getMenu()
                if (DatabaseManager.getInstance().isExist(g.url)) {
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pop_remove, menu)
                } else {
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pop_add, menu)
                }
                popupMenu.setOnMenuItemClickListener({ item ->
                    def i = item as MenuItem
                    switch (i.getItemId()) {
                        case R.id.menu_add:
                            DatabaseManager.getInstance().insert(new Bookmark(g.url, g.desc))
                            Toast.makeText(getActivity(), R.string.success_collect, Toast.LENGTH_SHORT).show()
                            return true
                        case R.id.menu_remove:
                            DatabaseManager.getInstance().delete(g.url)
                            Toast.makeText(getActivity(), R.string.success_uncollect, Toast.LENGTH_SHORT).show()
                            return true
                    }
                    return false
                })
                popupMenu.show()
                return true
            }
        }


    }


    public void setUpData(List<Gank> ganks) {
        if (ganks != null) {
            if (ganks.isEmpty()) {
                onNoMoreData()
                Toast.makeText(getActivity(), R.string.no_data, Toast.LENGTH_SHORT).show()
            } else {
                onSuccess()
                adapter.setGanks(ganks)
                showMore()
            }
        } else {
            onLoadFailure()
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
            moreText = more.findViewById(R.id.textView) as TextView
            progressBar = more.findViewById(R.id.progressBar) as ProgressBar

            AdvanceAdapter advanceAdapter = new AdvanceAdapter(adapter)
            advanceAdapter.addFooter(more)
            recyclerView.setAdapter(advanceAdapter)

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (more.isShown()) {
                            more()
                        }
//                        if (manager.findLastVisibleItemPosition() == advanceAdapter.getItemCount() - 1) {
//                            more();
//                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                }
            });

            more.setVisibility(View.GONE)
            more.setClickable(false)

            more.setOnClickListener(this.&more)

        } else {
            recyclerView.setAdapter(adapter)
        }

        if (isBookmark) {
            swipeRefreshLayout.setEnabled(false)
        }

        swipeRefreshLayout.setRefreshing(true)
        provider.setNoticeable(true)
        DataManager.getInstance().get(provider, this.&setUpData)

        error.setOnClickListener({ view ->
            swipeRefreshLayout.setRefreshing(true)
            error.setVisibility(View.GONE)
            requestRefresh()
        })

    }

    @Override
    void onDestroyView() {
        super.onDestroyView()
        provider.setNoticeable(false)
    }

    @Override
    void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden)
        provider.setNoticeable(!hidden)
    }

    @Override
    protected void requestRefresh() {
        super.requestRefresh()
        DataManager.getInstance().update(provider, this.&setUpData)
    }

    protected void more(View v = null) {
        if (loadMoreProgress) {
            return
        }
        loadMoreProgress = true
        onLoading()
        DataManager.getInstance().more(provider, { ganks ->
            setUpData(ganks as List<Gank>)
            loadMoreProgress = false
        })
    }

    def onNoMoreData() {
        moreText?.setText(R.string.more_last)
        progressBar?.setVisibility(View.INVISIBLE)
        more?.setClickable(false)
    }

    def onLoading() {
        moreText?.setText(R.string.text_loading)
        progressBar?.setVisibility(View.VISIBLE)
        more?.setClickable(false)
    }

    def onLoadFailure() {
        moreText?.setText(R.string.text_loading_failure)
        progressBar?.setVisibility(View.VISIBLE)
        more?.setClickable(true)
    }

    protected void showMore() {
        if (manager.getItemCount() > manager.findLastVisibleItemPosition() - manager.findFirstVisibleItemPosition() + 1)
            more?.setVisibility(View.VISIBLE)
    }

    static GankFragment newInstance(String type, boolean isRandom = false, boolean isImage = false, boolean isBookmark = false) {
        GankFragment fragment = new GankFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.TYPE, type)
        bundle.putBoolean(Config.Static.IS_RANDOM, isRandom)
        bundle.putBoolean(Config.Static.IS_IMAGE, isImage)
        bundle.putBoolean(Config.Static.IS_BOOKMARK, isBookmark)
        fragment.setArguments(bundle)
        fragment
    }
}