package zhou.gank.io.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.model.Gank
import zhou.gank.io.util.TimeKit

@CompileStatic
public class GankAdapter extends BaseAdapter<Holder> {

    List<Gank> ganks

    @Override
    Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        def holder = new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gank, null))
        holder.setListener { p ->

            Gank gank = ganks?.get(p as int)
            assert clickListener!=null
            println(clickListener.class.name)
            println(gank)
            clickListener?.call(gank)
        }
        return holder
    }

    @Override
    void onBindViewHolder(Holder holder, int i) {
        Gank gank = ganks.get(i)

        holder.title.setText(gank.desc)
        holder.user.setText(gank.who)
        holder.time.setText(TimeKit.format(gank.createdAt))
    }

    @Override
    int getItemCount() {
        return ganks == null ? 0 : ganks.size()
    }

    static class Holder extends RecyclerView.ViewHolder {

        public TextView title, user, time

        Closure listener

        Holder(View itemView) {
            super(itemView)

            title = itemView.findViewById(R.id.title) as TextView
            user = itemView.findViewById(R.id.user) as TextView
            time = itemView.findViewById(R.id.time) as TextView

            itemView.setOnClickListener({ v ->
                listener?.call(getAdapterPosition())
            })
        }

        void setListener(Closure listener) {
            this.listener = listener
        }
    }

    void setGanks(List<Gank> ganks) {
        this.ganks = ganks
        notifyDataSetChanged()
    }
}