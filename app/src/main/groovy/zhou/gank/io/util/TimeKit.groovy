package zhou.gank.io.util;

import groovy.transform.CompileStatic

import java.text.SimpleDateFormat

@CompileStatic
public class TimeKit {

    public static final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日")

    public static String format(Date data){
        simpleDateFormat.format(data)
    }
}