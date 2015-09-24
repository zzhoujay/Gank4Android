
package zhou.gank.io.data
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.model.Gank
import zhou.gank.io.util.HashKit

@CompileStatic
class RandomProvider implements DataProvider<List<Gank>> {
    private List<Gank> ganks;
    private int size;
    private String key, type;
    private File file;

    RandomProvider(String type, int size) {
        this.type = type;
        this.size = size;
        key = HashKit.md5(String.format("%s-%d-random.cache", type, size));
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
    List<Gank> get() {
        return ganks;
    }

    @Override
    void set(@Nullable List<Gank> ganHuos, boolean more) {
        this.ganks = ganHuos;
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
        return false;
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