package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.model.Gank
import zhou.gank.io.model.Result
import zhou.gank.io.ui.adapter.GankAdapter
import zhou.gank.io.util.NetworkKit

@CompileStatic
public class GankFragment extends BaseFragment {

    SwipeRefreshLayout swipeRefreshLayout
    RecyclerView recyclerView

    String type

    private GankAdapter adapter

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        def b = getArguments()
        if (b) {
            type = b.getString(Config.Static.TYPE)
        }
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def v = inflater.inflate(R.layout.fragment_recyler_view, container, false)
        initView(v)

        adapter = new GankAdapter()

        NetworkKit.type(type, 20, 1, getDate as Closure)

        return v
    }

    def getDate = { result ->
        if (result instanceof Result) {
            if (result.isSuccess()) {
                adapter.setGanks(result.results as List<Gank>)
            } else {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Override
    def initView(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView = v.findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()))
    }
}