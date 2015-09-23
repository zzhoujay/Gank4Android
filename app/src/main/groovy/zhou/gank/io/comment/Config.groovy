package zhou.gank.io.comment

import groovy.transform.CompileStatic;

@CompileStatic
class Config {

    public static class Static {

        public static final String TYPE = "type"

    }

    public static class Type {

        public static final String ANDROID = "Android"

        public static final String IOS = "iOS"

        public static final String RECOMMEND = "瞎推荐"

        public static final String RESOURCES = "扩展资源"

        public static final String WELFARE = "福利"

        public static final String VIDEO = "休息视频"

    }

    public static class Action {

        //打开drawerLayout
        public static final int OPEN_DRAWER_LAYOUT = 0x111111
    }
}