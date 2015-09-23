package zhou.gank.io.model

import groovy.transform.CompileStatic;

@CompileStatic
class Result {

    public boolean error
    public List<Gank> results

    Result(boolean error, List<Gank> results) {
        this.error = error
        this.results = results
    }

    Result() {
    }

    boolean isSuccess() {
        !error
    }


    @Override
    public String toString() {
        return "Result{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}