package zhou.gank.io.model

import groovy.transform.CompileStatic
import groovy.transform.ToString;

@CompileStatic
@ToString
class ResultDaily {

    public boolean error
    public GankDaily daily;

    ResultDaily(boolean error, GankDaily daily) {
        this.error = error
        this.daily = daily
    }

    ResultDaily() {
    }
}