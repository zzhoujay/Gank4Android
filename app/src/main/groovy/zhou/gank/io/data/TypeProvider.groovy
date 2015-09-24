package zhou.gank.io.data

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.model.Gank
import zhou.gank.io.util.HashKit
import zhou.gank.io.util.Pageable

@CompileStatic
class TypeProvider implements DataProvider<List<Gank>> {

    private List<Gank> ganks;
    private File file;
    private String type, key;
    private Pageable pageable;

    TypeProvider(String type, int size) {
        this.type = type;
        key = HashKit.md5(type + ".cache");
        file = new File(App.cacheFile(), key);
        pageable = new Pageable(1, size);
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
    List<Gank> get() {
        return ganks;
    }

    @Override
    void set(@Nullable List<Gank> ganHuos, boolean more) {
        if (more && ganHuos != null && hasLoad()) {
            this.ganks.addAll(ganHuos);
        } else {
            this.ganks = ganHuos;
        }
    }

    @Override
    void loadByCache(Closure closure) {

    }

    @Override
    void load(Closure closure, boolean more) {

    }

    @Override
    boolean hasLoad() {
        return ganks != null;
    }

    @Override
    boolean needCache() {
        return true;
    }

    @Override
    boolean clearCache() {
        return file.exists() && file.delete();
    }

    @NonNull
    @Override
    String key() {
        return key;
    }

}