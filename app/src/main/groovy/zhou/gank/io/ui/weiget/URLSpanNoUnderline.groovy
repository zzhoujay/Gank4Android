package zhou.gank.io.ui.weiget

import android.content.Intent
import android.net.Uri
import android.os.Parcel
import android.support.annotation.NonNull
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.activity.WebActivity
import zhou.gank.io.util.LogKit;

@CompileStatic
class URLSpanNoUnderline extends URLSpan {
    URLSpanNoUnderline(String url) {
        super(url)
    }

    URLSpanNoUnderline(Parcel src) {
        super(src)
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    void onClick(View widget) {
        def open = Config.getBoolean(widget.getResources().getString(R.string.key_open), Config.Configurable.HANDLE_BY_ME);
        if (open) {
            Intent intent = new Intent(widget.getContext(), WebActivity.class)
            intent.putExtra(Config.Static.URL, getURL())
            widget.getContext().startActivity(intent)
        } else {
            super.onClick(widget)
        }
    }
}