package zhou.gank.io.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.model.Gank

@CompileStatic
public class GankAdapter extends RecyclerView.Adapter<Holder> {

    private List<Gank> ganks

    @Override
    Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        def holder = new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gank, null))
        return holder
    }

    @Override
    void onBindViewHolder(Holder holder, int i) {
        Gank gank=ganks.get(i)

        holder.title.setText(gank.desc)
        holder.user.setText(gank.who)
    }

    @Override
    int getItemCount() {
        return ganks == null ? 0 : ganks.size()
    }

    static class Holder extends RecyclerView.ViewHolder {

        public TextView title, user, time

        Holder(View itemView) {
            super(itemView)

            title = itemView.findViewById(R.id.title) as TextView
            user = itemView.findViewById(R.id.user) as TextView
            time = itemView.findViewById(R.id.time) as TextView
        }
    }

    void setGanks(List<Gank> ganks) {
        this.ganks = ganks
        notifyDataSetChanged()
    }
}