package zhou.gank.io.util;

import groovy.transform.CompileStatic

@CompileStatic
public class NumKit {


    public static int getNum(String str, int num) {
        def gg
        try {
            gg = Integer.valueOf(str)
        } catch (Exception e) {
            gg = num
        }
        return gg
    }
}