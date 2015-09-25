package zhou.gank.io.data

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.model.Gank
import zhou.gank.io.model.Result
import zhou.gank.io.net.NetworkManager
import zhou.gank.io.util.FileKit
import zhou.gank.io.util.HashKit
import zhou.gank.io.util.LogKit
import zhou.gank.io.util.NetworkKit

@CompileStatic
class RandomProvider implements DataProvider<List<Gank>> {

    private List<Gank> ganks;
    private int size;
    private String key, type;
    private File file;

    RandomProvider(String type, int size) {
        this.type = type;
        this.size = size;
        key = HashKit.md5("$type-$size-random.cache");
        file = new File(App.cacheFile(), key);
    }

    @Override
    void persistence() {
        if (hasLoad()) {
            new Thread({
                try {
                    FileKit.writeObject(file, ganks)
                } catch (Exception e) {
                    LogKit.d("persistence", "random", e)
                }
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
        def gks = null
        if (file.exists()) {
            try {
                gks = FileKit.readObject(file)
            } catch (Exception e) {
                LogKit.d("loadByCache", "random", e)
            }
        }
        closure?.call(gks)
    }

    @Override
    void load(Closure closure, boolean more) {
        if (NetworkManager.getInstance().isNetworkConnected()) {
            NetworkKit.random(type, size, { result ->
                def gks = null
                if(result instanceof Result){
                    def r=result as Result
                    if (r?.isSuccess()) {
                        gks = r.results
                    } else {
                        App.toast(R.string.failure_get)
                    }
                }else {
                    App.toast(result as String)
                }
                closure?.call(gks)
            })
        } else {
            closure?.call()
            App.toast(R.string.error_network)
        }
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