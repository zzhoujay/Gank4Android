package zhou.gank.io.ui.adapter

import android.support.v7.widget.RecyclerView
import groovy.transform.CompileStatic
import zhou.gank.io.model.Gank

@CompileStatic
public
abstract class BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    List<Gank> ganks
    Closure clickListener

    void setGanks(List<Gank> ganks) {
        this.ganks = ganks
        notifyDataSetChanged()
    }

    void setClickListener(Closure clickListener) {
        this.clickListener = clickListener
    }
}