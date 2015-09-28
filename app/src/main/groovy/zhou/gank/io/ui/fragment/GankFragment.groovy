package zhou.gank.io.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.etiennelawlor.imagegallery.library.activities.FullScreenImageGalleryActivity
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
    boolean isRandom, isImage, loadMoreProgress, noMoreData
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

        adapter.setClickListener { gank, p = null ->
            def gs = gank as Gank
            boolean flag = Config.getBoolean(getString(R.string.key_open), true)
            if (!flag) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gs.url))
                startActivity(intent)
            } else {
                Intent intent
                if (isImage) {
                    intent = new Intent(getActivity(), FullScreenImageGalleryActivity.class)
                    ArrayList<Gank> ganks = provider.get() as ArrayList<Gank>
                    ArrayList<String> urls = new ArrayList<>(ganks.size())
                    ganks.each {
                        urls << it.url
                    }
                    intent.putStringArrayListExtra("images", urls)
//                    intent.putExtra("palette_color_type", PaletteColorType.VIBRANT)
                    intent.putExtra("position", p as int)
                } else {
                    intent = new Intent(getActivity(), WebActivity.class)
                    intent.putExtra(Config.Static.URL, gs.url)
                    intent.putExtra(Config.Static.TITLE, gs.desc)
                }
                startActivity(intent)
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