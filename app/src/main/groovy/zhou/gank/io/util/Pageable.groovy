package zhou.gank.io.util

import groovy.transform.CompileStatic;

@CompileStatic
class Pageable {

    public int pageNo
    public int pageSize

    Pageable(int pageNo, int pageSize) {
        this.pageNo = pageNo
        this.pageSize = pageSize
    }

    def next(){
        pageNo++
    }

    def prev(){
        pageNo--
    }
}