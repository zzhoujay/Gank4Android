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
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config

@CompileStatic
public class WebFragment extends BaseFragment {

    WebView webView
    String url
    ProgressBar progressBar
    SwipeRefreshLayout swipeRefreshLayout

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

        swipeRefreshLayout.setOnRefreshListener({
            webView.reload()
        })
    }

    @Override
    boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
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
                App.toast(R.string.hehe)
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    static WebFragment newInstance(String url) {
        WebFragment webFragment = new WebFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.URL, url)
        webFragment.setArguments(bundle)
        return webFragment
    }
}