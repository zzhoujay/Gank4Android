package zhou.gank.io.model

import groovy.transform.CompileStatic;

@CompileStatic
class Result extends BaseResult {

    public List<Gank> results

    Result(boolean error, List<Gank> results) {
        this.error = error
        this.results = results
    }

    Result() {
    }


    @Override
    public String toString() {
        return "Result{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }

    @Override
    boolean isSuccess() {
        !error
    }
}