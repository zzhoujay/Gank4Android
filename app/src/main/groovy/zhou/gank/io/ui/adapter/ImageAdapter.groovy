package zhou.gank.io.ui.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.model.Gank
import zhou.gank.io.util.TimeKit

@CompileStatic
public class ImageAdapter extends BaseAdapter<Holder> {

    private List<Gank> ganks

    @Override
    Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null))
        holder.setListener { p ->
            Gank gank = ganks?.get(p as int)
            clickListener?.call(gank, p)
        }
        return holder
    }

    @Override
    void onBindViewHolder(Holder holder, int position) {
        Gank gank = ganks.get(position)

        Picasso.with(holder.icon.getContext()).load(gank.url).into(holder.icon)
        holder.who.setText(gank.who)
        holder.time.setText(TimeKit.format(gank.createdAt))
    }

    @Override
    int getItemCount() {
        return ganks == null ? 0 : ganks.size()
    }

    public static class Holder extends RecyclerView.ViewHolder {

        ImageView icon
        TextView who, time

        Closure listener

        Holder(View itemView) {
            super(itemView)

            icon = itemView.findViewById(R.id.icon) as ImageView
            who = itemView.findViewById(R.id.who) as TextView
            time = itemView.findViewById(R.id.time) as TextView

            if (itemView instanceof CardView) {
                def card = itemView as CardView
                if (App.themeIsLight()) {
                    card.setCardBackgroundColor(App.getInstance().getCardLight())
                } else {
                    card.setCardBackgroundColor(App.getInstance().getCardDark())
                }
            }

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