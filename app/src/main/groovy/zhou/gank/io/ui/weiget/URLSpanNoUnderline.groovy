package zhou.gank.io.ui.weiget

import android.os.Parcel
import android.support.annotation.NonNull
import android.text.TextPaint
import android.text.style.URLSpan
import groovy.transform.CompileStatic;

@CompileStatic
class URLSpanNoUnderline extends URLSpan{
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
}