package zhou.gank.io

import android.app.Application
import android.widget.Toast
import com.bettervectordrawable.VectorDrawableCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic;

@CompileStatic
class App extends Application {

    public static final String SITE_URL = "http://gank.avosapps.com";
    public static final String TYPE_URL = SITE_URL + "/api/data/%s/%d/%d";
    public static final String TIME_URL = SITE_URL + "/api/day/%d/%d/%d";
    public static final String RANDOM_URL = SITE_URL + "/api/random/data/%s/%d";

    private static App app;

    static App getInstance() {
        return app;
    }

    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

        VectorDrawableCompat.enableResourceInterceptionFor(getResources(),
                R.drawable.ic_favorite_white_48px);
    }

    static void toast(int id) {
        Toast.makeText(app, id, Toast.LENGTH_SHORT).show();
    }

    static void toast(String msg) {
        Toast.makeText(app, msg, Toast.LENGTH_SHORT).show();
    }

    static String getStr(int res) {
        return app.getResources().getString(res);
    }

    static File cacheFile() {
        return app.getCacheDir();
    }
}