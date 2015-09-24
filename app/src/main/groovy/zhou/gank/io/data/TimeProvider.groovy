package zhou.gank.io.data

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.model.GankDaily
import zhou.gank.io.model.ResultDaily
import zhou.gank.io.net.NetworkManager
import zhou.gank.io.util.FileKit
import zhou.gank.io.util.HashKit
import zhou.gank.io.util.LogKit
import zhou.gank.io.util.NetworkKit

@CompileStatic
class TimeProvider implements DataProvider<GankDaily> {

    private GankDaily daily;
    private int year, month, day;
    private String key;
    private File file;

    TimeProvider(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        key = HashKit.md5("year:$year,month:$month,day:$day-cache")
        file = new File(App.cacheFile(), key);
    }

    @Override
    void persistence() {
        if (hasLoad()) {
            new Thread({
                try {
                    FileKit.writeObject(file, daily)
                } catch (Exception e) {
                    LogKit.d("persistence", "time", e)
                }
            }).start();
        }
    }

    @Nullable
    @Override
    GankDaily get() {
        return daily;
    }

    @Override
    void set(@Nullable GankDaily ganHuos, boolean more) {
        this.daily = ganHuos;
    }

    @Override
    void loadByCache(Closure closure) {
        def d = null
        if (file.exists()) {
            try {
                d = FileKit.readObject(file)
            } catch (Exception e) {
                LogKit.d("loadByCache", "time", e)
            }
        }
        closure?.call(d)
    }

    @Override
    void load(Closure closure, boolean more) {
        if (NetworkManager.getInstance().isNetworkConnected()) {
            NetworkKit.time(year, month, day, { result ->
                def d = null
                def r = result as ResultDaily
                if (r.isSuccess()) {
                    d = r.results
                } else {
                    App.toast(R.string.failure_get)
                }
                closure?.call(d)
            })
        } else {
            closure?.call()
            App.toast(R.string.error_network)
        }
    }

    @Override
    public boolean hasLoad() {
        return daily != null;
    }

    @Override
    public boolean needCache() {
        return true;
    }

    @Override
    public boolean clearCache() {
        return file.exists() && file.delete();
    }

    @NonNull
    @Override
    public String key() {
        return key;
    }


}