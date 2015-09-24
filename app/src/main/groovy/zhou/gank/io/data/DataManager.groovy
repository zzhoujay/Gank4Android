package zhou.gank.io.data

import android.util.Log
import groovy.transform.CompileStatic

@CompileStatic
class DataManager {

    private static DataManager dataManager

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager()
        }
        dataManager
    }

    private HashMap<String, DataProvider> providers

    private DataManager() {
        providers = new HashMap<>()
    }

    /**
     * 添加数据提供器（不进行重复检查）
     *
     * @param provider 提供器
     */
    public void add(DataProvider provider) {
        if (provider != null)
            providers.put(provider.key(), provider)
    }

    /**
     * 添加数据提供器
     *
     * @param provider 数据提供器
     * @param check 是否进行重复检查
     */
    public void add(DataProvider provider, boolean check) {
        if (provider == null) {
            return
        }
        if (check) {
            if (!providers.containsKey(provider.key())) {
                providers.put(provider.key(), provider)
            }
        } else {
            providers.put(provider.key(), provider)
        }
    }

    /**
     * 移除数据提供器
     *
     * @param key key
     */
    public void remove(String key) {
        providers.remove(key)
    }

    /**
     * 移出数据提供器
     *
     * @param provider 数据提供器
     */
    public void remove(DataProvider provider) {
        if (provider != null) {
            providers.remove(provider.key())
        }
    }

    /**
     * 获取数据 按照（内存->本地缓存->网络）的顺序获取
     *
     * @param key key
     * @param loadCallback 回调
     * @param < T >             type
     */
    public <T> void get(String key, Closure loadCallback) {
        try {
            def provider = providers.get(key) as DataProvider<T>
            get(provider, loadCallback)
        } catch (Exception e) {
            Log.d("get", "DataManager", e)
            loadCallback?.call()
        }
    }

    /**
     * 获取数据
     *
     * @param provider 数据提供器
     * @param loadCallback 回调
     * @param < T >             type
     */
    public <T> void get(DataProvider<T> provider, Closure loadCallback) {
        if (provider.hasLoad()) {
            loadCallback?.call(provider.get())
        } else {
            provider.loadByCache({t ->
                if (t != null) {
                    provider.set(t as T, false)
                    if (provider.needCache()) {
                        provider.persistence()
                    }
                    loadCallback?.call(provider.get())
                } else {
                    provider.load({tn ->
                        provider.set(tn as T, false);
                        if (provider.needCache()) {
                            provider.persistence()
                        }
                        loadCallback?.call(provider.get())
                    }, false)
                }
            });
        }
    }

    /**
     * 加载数据
     *
     * @param key key
     * @param loadCallback 回调
     * @param < T >             type
     */
    @SuppressWarnings("unchecked")
    public <T> void load(String key, Closure loadCallback, boolean more) {
        try {
            DataProvider<T> provider = (DataProvider<T>) providers.get(key)
            load(provider, loadCallback, more)
        } catch (Exception e) {
            Log.d("load", "DataManager", e)
            loadCallback?.call(null)
        }
    }

    /**
     * 加载数据
     *
     * @param provider 数据提供器
     * @param loadCallback 回调
     * @param < T >             type
     */
    public <T> void load(DataProvider<T> provider, Closure loadCallback, boolean more) {
        provider.load({t ->
            provider.set(t as T, more)
            if (provider.needCache()) {
                provider.persistence()
            }
            loadCallback?.call(provider.get())
        }, more);
    }

    public <T> void update(DataProvider<T> provider, Closure loadCallback) {
        load(provider, loadCallback, false)
    }

    void update(String key, Closure loadCallback) {
        load(key, loadCallback, false)
    }

    public <T> void more(DataProvider<T> provider, Closure loadCallback) {
        load(provider, loadCallback, true)
    }

    void more(String key, Closure loadCallback) {
        load(key, loadCallback, true)
    }

    /**
     * 持久化所有数据
     */
    void persistence() {
        for (Map.Entry<String, DataProvider> entry : providers.entrySet()) {
            DataProvider dataProvider = entry.getValue();
            if (dataProvider.needCache()) {
                dataProvider.persistence()
            }
        }
    }

    void clearAllCache() {
        for (Map.Entry<String, DataProvider> entry : providers.entrySet()) {
            DataProvider provider = entry.getValue()
            provider.clearCache()
        }
    }

    void clearCache(String key) {
        DataProvider provider = providers.get(key)
        if (provider != null) {
            provider.clearCache()
        }
    }

    boolean hasLoad(String key) {
        DataProvider provider = providers.get(key)
        provider != null && provider.hasLoad()
    }

    boolean exist(String key) {
        providers.containsKey(key)
    }

    public void reset() {
        providers.clear()
    }

}