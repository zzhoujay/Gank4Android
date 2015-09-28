package zhou.gank.io.util;

import groovy.transform.CompileStatic

import java.text.SimpleDateFormat

@CompileStatic
public class TimeKit {

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日")

    public static String format(Date data) {
        simpleDateFormat.format(data)
    }

    public static List<Integer> getTime() {
        Calendar calendar = Calendar.getInstance()
        return [calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)]
    }

    public static boolean future(int year, int month, int day) {
        Calendar now = Calendar.getInstance()
        Calendar calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.compareTo(now) >= 0
    }
}