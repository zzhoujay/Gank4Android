package zhou.gank.io.model

import groovy.transform.CompileStatic
import groovy.transform.ToString;

@CompileStatic
@ToString
class ResultDaily extends BaseResult {

    public GankDaily results;

    ResultDaily(boolean error, GankDaily results) {
        this.error = error
        this.results = results
    }

    ResultDaily() {
    }

    @Override
    public String toString() {
        return "ResultDaily{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }

    @Override
    boolean isSuccess() {
        !error
    }
}