package zhou.gank.io.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.widget.SwipeRefreshLayout
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.database.DatabaseManager
import zhou.gank.io.model.Bookmark
import zhou.gank.io.util.LogKit

@CompileStatic
public class WebFragment extends BaseFragment {

    WebView webView
    String url, title
    ProgressBar progressBar
    SwipeRefreshLayout swipeRefreshLayout
    MenuItem itemCollect

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_web, container, false)
        initView(v)
        return v
    }

    @Override
    protected void initView(View v) {
        webView = v.findViewById(R.id.web_view) as WebView
        progressBar = v.findViewById(R.id.progressBar) as ProgressBar
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        webView.getSettings().setJavaScriptEnabled(true)

        Bundle bundle = getArguments()
        url = bundle?.getString(Config.Static.URL)
        title = bundle?.getString(Config.Static.TITLE)

        webView.loadUrl(url)

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE)
                    progressBar.setProgress(0)
                    swipeRefreshLayout.setRefreshing(false)
                } else {
                    // 加载中
                    progressBar.setVisibility(View.VISIBLE)
                    progressBar.setProgress(newProgress)
                }
            }

            @Override
            void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title)
                getActivity()?.setTitle(title)
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url)
                return true
            }
        })

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setSupportZoom(true)
        webView.getSettings().setDisplayZoomControls(true)

        swipeRefreshLayout.setOnRefreshListener({
            webView.reload()
        })
    }

    @Override
    boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()//返回上一页面
                return true
            } else {
                getActivity().finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_web, menu)
        itemCollect = menu.findItem(R.id.menu_collect)
        if (DatabaseManager.getInstance().isExist(url)) {
            itemCollect.setTitle(R.string.cancel_collect)
        } else {
            itemCollect.setTitle(R.string.menu_collect)
        }
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_copy:
                App.copyUri(Uri.parse(url))
                App.toast(R.string.success_copy)
                return true
            case R.id.menu_open:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            case R.id.menu_collect:
                collect()
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    def collect() {
        if (DatabaseManager.getInstance().isExist(url)) {
            try {
                DatabaseManager.getInstance().delete(url)
                Toast.makeText(getActivity(), R.string.success_uncollect, Toast.LENGTH_SHORT).show()
                itemCollect.setTitle(R.string.menu_collect)
            } catch (Exception e) {
                LogKit.d("uncollect", "failure", e)
                Toast.makeText(getActivity(), R.string.failure_uncollect, Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                DatabaseManager.getInstance().insert(new Bookmark(url, title == getString(R.string.app_name) ? webView.getTitle() : title))
                Toast.makeText(getActivity(), R.string.success_collect, Toast.LENGTH_SHORT).show()
                itemCollect.setTitle(R.string.cancel_collect)
            } catch (Exception e) {
                LogKit.d("collect", "failure", e)
                Toast.makeText(getActivity(), R.string.failure_collect, Toast.LENGTH_SHORT).show()
            }
        }
    }

    static WebFragment newInstance(String url, String title = null) {
        WebFragment webFragment = new WebFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.URL, url)
        bundle.putString(Config.Static.TITLE, title)
        webFragment.setArguments(bundle)
        return webFragment
    }
}