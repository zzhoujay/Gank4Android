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

    private GankDaily daily
    int year, month, day
    private String key
    private File file
    private boolean noticeable
    private boolean needCache

    TimeProvider(int year, int month, int day) {
        this.year = year
        this.month = month
        this.day = day
        key = HashKit.md5("year:$year,month:$month,day:$day-cache")
        file = new File(App.cacheFile(), key)

        Calendar now = Calendar.getInstance()
        int y = now.get(Calendar.YEAR)
        if (year < y) {
            needCache = true
        } else if (year == y) {
            int m = now.get(Calendar.MONTH) + 1
            if (month < m) {
                needCache = true
            } else if (month == m) {
                int d = now.get(Calendar.DAY_OF_MONTH)
                needCache = day <= d
            } else {
                needCache = false
            }
        } else {
            needCache = false
        }
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
            }).start()
        }
    }

    @Nullable
    @Override
    GankDaily get() {
        return daily
    }

    @Override
    void set(@Nullable GankDaily ganHuos, boolean more) {
        this.daily = ganHuos
    }

    @Override
    void loadByCache(Closure closure) {
        def d = null
        if (file.exists()) {
            new Thread({
                try {
                    d = FileKit.readObject(file)
                } catch (Exception e) {
                    LogKit.d("loadByCache", "time", e)
                }
                App.getInstance().getMainHandler().post({
                    closure?.call(d)
                })
            }).start()
        } else {
            closure?.call(d)
        }
    }

    @Override
    void load(Closure closure, boolean more) {
        if (NetworkManager.getInstance().isNetworkConnected()) {
            NetworkKit.time(year, month, day, { result ->
                def d = null
                if (result instanceof ResultDaily) {
                    def r = result as ResultDaily
                    if (r.isSuccess()) {
                        d = r.results
                    } else {
                        if (noticeable)
                            App.toast(R.string.failure_get)
                    }
                }
                closure?.call(d)
            })
        } else {
            closure?.call()
            if (noticeable)
                App.toast(R.string.error_network)
        }
    }

    @Override
    public boolean hasLoad() {
        return daily != null
    }

    @Override
    public boolean needCache() {
        return needCache
    }

    @Override
    public boolean clearCache() {
        return file.exists() && file.delete()
    }

    @NonNull
    @Override
    public String key() {
        return key
    }

    @Override
    void setNoticeable(boolean noticeable) {
        this.noticeable = noticeable
    }

    public TimeProvider getNextDay() {
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return new TimeProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
    }

    public TimeProvider getPrevDay() {
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return new TimeProvider(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
    }
}