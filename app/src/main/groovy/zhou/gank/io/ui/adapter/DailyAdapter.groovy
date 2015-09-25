package zhou.gank.io.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.model.GankDaily
import zhou.gank.io.util.TextKit

@CompileStatic
class DailyAdapter extends BaseAdapter<Holder> {

    private GankDaily daily;

    @Override
    Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily, null));
        return holder;
    }

    @Override
    void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(daily.getType(position));
        holder.content.setText(TextKit.generate(daily.getGanhuo(position), App.getInstance().getResources().getColor(R.color.material_lightBlue_500)));
    }

    @Override
    int getItemCount() {
        return ganks == null ? 0 : ganks.size()
    }

    static class Holder extends RecyclerView.ViewHolder {

        public TextView title, content;

        public Holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);

            content.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    void setDaily(GankDaily daily) {
        this.daily = daily;
        notifyDataSetChanged();
    }
}