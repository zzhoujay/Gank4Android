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
import zhou.gank.io.data.DataProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.adapter.GankAdapter

@CompileStatic
public class GankFragment extends BaseFragment {

    SwipeRefreshLayout swipeRefreshLayout
    RecyclerView recyclerView
    DataProvider provider
    String type
    boolean isRandom

    private GankAdapter adapter

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        def b = getArguments()
        if (b) {
            type = b.getString(Config.Static.TYPE)
            isRandom = b.getBoolean(Config.Static.IS_RANDOM, false)
        }
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        def v = inflater.inflate(R.layout.fragment_recyler_view, container, false)
        initView(v)

        adapter = new GankAdapter()

        return v
    }

    def setUpData = { list ->
        if (list instanceof List<Gank>) {
            if (list.isEmpty()) {
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setGanks(list)
            }
        } else {
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show()
        }
    }


    @Override
    def initView(View v) {
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        recyclerView = v.findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()))
    }

    def static newInstance(String type, boolean isRandom = false) {
        GankFragment fragment = new GankFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.TYPE, type)
        bundle.putBoolean(Config.Static.IS_RANDOM, isRandom)
        fragment.setArguments(bundle)
        fragment
    }
}