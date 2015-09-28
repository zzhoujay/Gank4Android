package zhou.gank.io.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso;
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.activity.ImageGalleryActivity

@CompileStatic
public class ImagePageFragment extends Fragment {

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ImageView icon = inflater.inflate(R.layout.fragment_image_page, container, false) as ImageView
        icon.setClickable(true)
        def b = getArguments()
        if (b) {
            String url = b.getString(Config.Static.URL)
            Picasso.with(getActivity()).load(url).into(icon)
            icon.setOnClickListener({ v ->
                if (icon.getDrawable()) {
                    ArrayList<String> urls = new ArrayList<>(1)
                    urls << url
                    Intent intent = new Intent(getActivity(), ImageGalleryActivity.class)
                    intent.putStringArrayListExtra(Config.Static.URLS, urls)
                    intent.putExtra(Config.Static.POSITION, 0)
                    startActivity(intent)
                }
            })
        }
        return icon
    }


    static ImagePageFragment newInstance(String url) {
        ImagePageFragment imagePageFragment = new ImagePageFragment()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.URL, url)
        imagePageFragment.setArguments(bundle)
        return imagePageFragment
    }
}