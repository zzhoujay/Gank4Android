package zhou.gank.io.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.adapter.ImageGalleryAdapter
import zhou.gank.io.util.FileKit

@CompileStatic
public class ImageGalleryFragment extends BaseFragment {

    public static final int ID_SHARE = 0x23456
    public static final int ID_SAVE = 0x34567

    List<String> urls
    int position
    ViewPager viewPager

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        def b = getArguments()
        urls = b?.getStringArrayList(Config.Static.URLS)
        position = b?.getInt(Config.Static.POSITION, 0)
        setTitle("${position + 1}/${urls == null ? 0 : urls.size()}")
    }

    @Override
    View onCreateView(LayoutInflater inflater,
                      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPager = inflater.inflate(R.layout.fragment_viewpager, container, false) as ViewPager
        ImageGalleryAdapter adapter = new ImageGalleryAdapter(urls)
        viewPager.setAdapter(adapter)
        viewPager.setCurrentItem(position, false)

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            void onPageSelected(int position) {
                setTitle("${position + 1}/${urls == null ? 0 : urls.size()}")
            }
        })
        return viewPager
    }

    @Override
    void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.add(0, ID_SHARE, 0, R.string.share)
        menu.add(0, ID_SAVE, 0, R.string.save)
    }

    @Override
    boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ID_SHARE:
                String url = urls?.get(viewPager.getCurrentItem())
                Picasso.with(getActivity()).load(url).into(new Target() {
                    @Override
                    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        File file = new File(App.getInstance().getExternalCacheDir(), "temp.jpg")
                        FileKit.saveBitmapFile(bitmap, file)
                        Intent intent = new Intent(Intent.ACTION_SEND)
                        intent.setType("image/*")
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                        startActivity(intent)
                    }

                    @Override
                    void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(getActivity(), R.string.error_get_image, Toast.LENGTH_SHORT).show()
                    }

                    @Override
                    void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                })
                return true
            case ID_SAVE:
                String url = urls?.get(viewPager.getCurrentItem())
                File file = new File(App.SAVE_PATH, FileKit.getFileRealName(url))
                Picasso.with(getActivity()).load(url).into(new Target() {
                    @Override
                    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        FileKit.saveBitmapFile(bitmap, file)
                        Toast.makeText(getActivity(), "图片保存在:${file.getAbsolutePath()}", Toast.LENGTH_LONG).show()
                    }

                    @Override
                    void onBitmapFailed(Drawable errorDrawable) {
                        Toast.makeText(getActivity(), R.string.error_get_image, Toast.LENGTH_SHORT).show()
                    }

                    @Override
                    void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                })
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    static ImageGalleryFragment newInstance(ArrayList<String> urls, int position = 0) {
        ImageGalleryFragment fragment = new ImageGalleryFragment()
        Bundle bundle = new Bundle()
        bundle.putStringArrayList(Config.Static.URLS, urls)
        bundle.putInt(Config.Static.POSITION, position)
        fragment.setArguments(bundle)
        return fragment
    }
}