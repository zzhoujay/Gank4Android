package zhou.gank.io.model

import groovy.transform.CompileStatic;

@CompileStatic
class Result {

    public boolean  error
    public Object results

    Result(boolean error, Object results) {
        this.error = error
        this.results = results
    }

    Result() {
    }

    boolean isSuccess(){
        !error
    }
}