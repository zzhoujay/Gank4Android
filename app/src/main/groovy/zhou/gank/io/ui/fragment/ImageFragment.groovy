package zhou.gank.io.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast;
import groovy.transform.CompileStatic
import zhou.gank.io.comment.Config
import zhou.gank.io.data.DataProvider
import zhou.gank.io.data.RandomProvider
import zhou.gank.io.data.TypeProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.adapter.GankAdapter
import zhou.gank.io.ui.adapter.ImageAdapter

@CompileStatic
public class ImageFragment extends AdvanceFragment {

    ImageAdapter adapter
    LinearLayoutManager manager
    DataProvider provider
    boolean isRandom
    String type
    View more

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)

        def b = getArguments()
        if (b) {
            type = b.getString(Config.Static.TYPE)
            isRandom = b.getBoolean(Config.Static.IS_RANDOM, false)
        }

        if (isRandom) {
            provider = new RandomProvider(type, Config.Configurable.DEFAULT_SIZE)
        } else {
            provider = new TypeProvider(type, Config.Configurable.DEFAULT_SIZE)
        }
        adapter = new ImageAdapter()
    }

    public void setUpData(List<Gank> ganks) {
        if (ganks != null) {
            if (ganks.isEmpty()) {
                hiddenMore()
                Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show()
            } else {
                onSuccess()
                adapter.setGanks(ganks)
            }
        } else {
            onFailure()
        }
    }

    protected void hiddenMore() {
        more?.setVisibility(View.GONE)
    }

    @Override
    protected void initView(View v) {
        super.initView(v)

        manager = new LinearLayoutManager(getActivity())
        recyclerView.setLayoutManager(manager)


    }
}