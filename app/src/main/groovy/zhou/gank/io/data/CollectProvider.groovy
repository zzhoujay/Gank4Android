package zhou.gank.io.data

import android.support.annotation.Nullable;
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.database.DatabaseManager
import zhou.gank.io.model.Gank

@CompileStatic
public class CollectProvider implements DataProvider<List<Gank>> {

    private List<Gank> ganks

    @Override
    void persistence() {

    }

    @Override
    List<Gank> get() {
        return ganks
    }

    @Override
    void set(@Nullable List<Gank> ganks, boolean more) {
        this.ganks = ganks
    }

    @Override
    void loadByCache(Closure closure) {
        new Thread({
            List<Gank> gs = DatabaseManager.getInstance().selectToGank()
            App.getInstance().getMainHandler().post({
                closure?.call(gs)
            })
        }).start()
    }

    @Override
    void load(Closure closure, boolean more) {

    }

    @Override
    boolean hasLoad() {
        return ganks != null
    }

    @Override
    boolean needCache() {
        return false
    }

    @Override
    boolean clearCache() {
        return false
    }

    @Override
    String key() {
        return "bookmark"
    }

    @Override
    void setNoticeable(boolean noticeable) {

    }
}