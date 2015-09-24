package zhou.gank.io.model

import groovy.transform.CompileStatic;

@CompileStatic
abstract class BaseResult implements Serializable {

    boolean error

    abstract boolean isSuccess()

}