package zhou.gank.io.comment

import android.content.SharedPreferences
import android.preference.PreferenceManager
import groovy.transform.CompileStatic
import zhou.gank.io.App;

@CompileStatic
class Config {

    public static class Configurable {

        public static int DEFAULT_SIZE = 10

        public static boolean HANDLE_BY_ME = true
    }

    public static class Static {

        public static final String TYPE = "type"

        public static final String URL = "url"

        public static final String TITLE = "title"

        public static final String CONTENT = "content"

        public static final String IS_RANDOM = "is_random"

        public static final String IS_IMAGE = "is_image"

        public static final String YEAR = "year"

        public static final String MONTH = "month"

        public static final String DAY = "day"
    }

    public static class Type {

        public static final String ANDROID = "Android"

        public static final String IOS = "iOS"

        public static final String RECOMMEND = "瞎推荐"

        public static final String RESOURCES = "拓展资源"

        public static final String WELFARE = "福利"

        public static final String VIDEO = "休息视频"

    }

    public static class Error {

        public static final String UNKOWN = "未知错误"

        public static final String TIME_OUT = "网络连接超时"

        public static final String FORMDATA_ = "数据格式出错"
    }

    public static class Action {

        //打开drawerLayout
        public static final int OPEN_DRAWER_LAYOUT = 0x111111
    }

    public static String getString(String key, String d) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())
        preferences.getString(key, d)
    }

    public static boolean getBoolean(String key, boolean d) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())
        preferences.getBoolean(key, d)
    }

    public static int getInt(String key, int d) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance())
        preferences.getInt(key, d)
    }
}