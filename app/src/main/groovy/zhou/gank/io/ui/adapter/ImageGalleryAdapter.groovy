package zhou.gank.io.ui.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso;
import groovy.transform.CompileStatic
import zhou.gank.io.R

@CompileStatic
public class ImageGalleryAdapter extends PagerAdapter {

    List<String> urls

    ImageGalleryAdapter(List<String> urls) {
        this.urls = urls
    }

    @Override
    int getCount() {
        return urls == null ? 0 : urls.size()
    }

    @Override
    boolean isViewFromObject(View view, Object object) {
        return view.is(object)
    }

    @Override
    Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext()
        View v = LayoutInflater.from(context).inflate(R.layout.layout_gallery, null)
        ImageView imageView = v.findViewById(R.id.image) as ImageView

        View error = v.findViewById(R.id.error_layout)
//        View progress = v.findViewById(R.id.progressBar)

        Closure loadImage = { view ->
//            progress.setVisibility(View.VISIBLE)
            error.setVisibility(View.GONE)
            Picasso.with(context).load(urls[position]).into(imageView, new Callback() {
                @Override
                void onSuccess() {
//                    progress.setVisibility(View.GONE)
                }

                @Override
                void onError() {
//                    progress.setVisibility(View.GONE)
                    error.setVisibility(View.VISIBLE)
                }
            })
        }

//        error.setOnClickListener(loadImage)

        loadImage(null)

        (container as ViewPager).addView(v, 0);
        return v
    }

    @Override
    void destroyItem(ViewGroup container, int position, Object object) {
        (container as ViewPager).removeView(object as View);
    }
}