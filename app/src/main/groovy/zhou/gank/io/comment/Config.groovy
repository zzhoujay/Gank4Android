package zhou.gank.io.comment

import groovy.transform.CompileStatic;

@CompileStatic
class Config {

    public static class Configurable {

        public static final int DEFAULT_SIZE = 10

        public static final boolean HANDLE_BY_ME = false
    }

    public static class Static {

        public static final String TYPE = "type"

        public static final String IS_RANDOM = "is_random"

        public static final String IS_IMAGE = "is_image"
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
}