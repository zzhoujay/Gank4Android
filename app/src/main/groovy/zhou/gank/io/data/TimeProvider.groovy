package zhou.gank.io.data

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.model.GankDaily
import zhou.gank.io.util.HashKit

@CompileStatic
class TimeProvider implements DataProvider<GankDaily> {

    private GankDaily ganHuos;
    private int year, month, day;
    private String key;
    private File file;

    TimeProvider(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        key = HashKit.md5(String.format("%dyear%dmonth%dday.cache", year, month, day));
        file = new File(App.cacheFile(), key);
    }

    @Override
    void persistence() {
        if (hasLoad()) {
            new Thread({

            }).start();
        }
    }

    @Nullable
    @Override
    GankDaily get() {
        return ganHuos;
    }

    @Override
    void set(@Nullable GankDaily ganHuos, boolean more) {
        this.ganHuos = ganHuos;
    }

    @Override
    void loadByCache(Closure closure) {

    }

    @Override
    void load(Closure closure, boolean more) {

    }

    @Override
    public boolean hasLoad() {
        return ganHuos != null;
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