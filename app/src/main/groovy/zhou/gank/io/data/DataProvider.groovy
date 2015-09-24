package zhou.gank.io.data

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import groovy.transform.CompileStatic;

@CompileStatic
interface DataProvider<T> {

    /**
     * 数据持久化（应异步进行）
     */
    void persistence();

    /**
     * 获取数据
     *
     * @return 数据
     */
    @Nullable
    T get();

    /**
     * 设置数据
     *
     * @param t 数据
     */
    void set(@Nullable T t, boolean more);

    /**
     * 从缓存中加载数据（应异步实现）
     *
     * @param loadCallback 回调
     */
    void loadByCache(Closure closure);

    /**
     * 加载数据（必须异步实现）
     *
     * @param loadCallback 回调
     */
    void load(Closure closure, boolean more);

    /**
     * 是否已经加载
     *
     * @return boolean
     */
    boolean hasLoad();

    /**
     * 是否需要缓存
     *
     * @return boolean
     */
    boolean needCache();

    /**
     * 清空缓存
     */
    boolean clearCache();

    /**
     * 获取该加载器的唯一标识
     *
     * @return key
     */
    @NonNull
    String key();

}