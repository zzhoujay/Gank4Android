package zhou.gank.io.util

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import groovy.transform.CompileStatic
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.weiget.URLSpanNoUnderline;

@CompileStatic
class TextKit {

    public static SpannableStringBuilder generate(List<Gank> ganHuos, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int start;
        for (Gank gh : ganHuos) {
            start = builder.length();
            builder.append(" • ");
            builder.setSpan(new StyleSpan(Typeface.BOLD), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = builder.length();
            builder.append(gh.desc);
            builder.setSpan(new URLSpanNoUnderline(gh.url), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(color), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append("（");
            builder.append(gh.who);
            builder.append("）\n");
        }
        return builder;
    }

    public static CharSequence getInfo() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int start = 0
        builder.append("Gank.IO Android 客户端\n")
        builder.setSpan(new StyleSpan(Typeface.BOLD), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append("项目地址：")
        start = builder.length()
        builder.append("git@osc")
        builder.setSpan(new URLSpanNoUnderline("https://git.oschina.net/zzhoujay/Gank4Android"), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append("、")
        start = builder.length()
        builder.append("github")
        builder.setSpan(new URLSpanNoUnderline("https://github.com/zzhoujay/Gank4Android"), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append("\n")
        start = builder.length()
        builder.append("by zzhoujay")
        builder.setSpan(new StyleSpan(Typeface.ITALIC), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }
}