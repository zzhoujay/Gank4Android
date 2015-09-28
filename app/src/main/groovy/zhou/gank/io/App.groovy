package zhou.gank.io

import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bettervectordrawable.VectorDrawableCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import groovy.transform.CompileStatic
import org.litepal.LitePalApplication
import zhou.gank.io.comment.Config
import zhou.gank.io.net.NetworkManager
import zhou.gank.io.util.Notifier

@CompileStatic
class App extends Application {

    public static final String SITE_URL = "http://gank.avosapps.com";
    public static final String TYPE_URL = SITE_URL + "/api/data";
    public static final String TIME_URL = SITE_URL + "/api/day";
    public static final String RANDOM_URL = SITE_URL + "/api/random/data";

    public static
    final File SAVE_PATH = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "gank")

    static {
        if (!SAVE_PATH.exists()) {
            SAVE_PATH.mkdir()
        }
    }

    private static App app;

    static App getInstance() {
        return app;
    }

    Gson gson;
    Handler mainHandler
    int cardLight, cardDark, textLight, textDark
    ArrayList<Notifier> notifiers

    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this)
        app = this;

        cardDark = getColor(R.color.cardview_dark_background)
        cardLight = getColor(R.color.cardview_light_background)
        textDark = getColor(R.color.material_grey_50)
        textLight = getColor(R.color.material_grey_1000)

        mainHandler = new Handler(Looper.getMainLooper())

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
        NetworkManager.getInstance().init(this, gson)
        NetworkManager.getInstance().setDefaultHandle { e ->
            switch (e) {
                case SocketTimeoutException:
                    return Config.Error.TIME_OUT
                default:
                    return Config.Error.UNKOWN
            }
        }
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(),
                R.drawable.ic_favorite_white_48px,
                R.drawable.wrong, R.drawable.ic_info_48px,
                R.drawable.ic_dashboard_48px, R.drawable.ic_event_48px,
                R.drawable.ic_extension_48px, R.drawable.ic_settings_black_48px,
                R.drawable.ic_menu_white_48px, R.drawable.ic_view_module_48px,
                R.drawable.ic_cloud_queue_48px, R.drawable.ic_cloud_off_48px,
                R.drawable.ic_insert_emoticon_48px)

        Config.Configurable.HANDLE_BY_ME = true

        notifiers = new ArrayList<>()
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

    static int getColor(int res) {
        return app.getResources().getColor(res)
    }

    static File cacheFile() {
        return app.getCacheDir();
    }

    static void copy(String text) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) app.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
    }

    static void copyUri(Uri uri) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) app.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;
        myClip = ClipData.newUri(app.getContentResolver(), "URI", uri);
        myClipboard.setPrimaryClip(myClip);
    }

    static void setTheme(Activity activity) {
        String theme = Config.getString(getStr(R.string.key_theme), "light")
        switch (theme) {
            case "light":
                activity.setTheme(R.style.AppTheme)
                break
            case "dark":
                activity.setTheme(R.style.AppThemeDark)
                break
        }
    }

    static boolean themeIsLight() {
        String theme = Config.getString(getStr(R.string.key_theme), "light")
        return theme == "light"
    }

    static boolean addNotifier(Notifier notifier) {
        app.notifiers.add(notifier)
    }

    static boolean removeNotifier(Notifier notifier) {
        app.notifiers.remove(notifier)
    }

    static void themeChanged() {
        app.notifiers.each {
            def notifier = it as Notifier
            notifier.notice(Config.Action.RESTART)
        }
    }

    static void setPrimaryColor(AppCompatActivity activity) {
    }
}