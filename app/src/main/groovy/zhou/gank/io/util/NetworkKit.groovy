package zhou.gank.io.util

import com.squareup.okhttp.Request
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.model.Result
import zhou.gank.io.net.NetworkManager

@CompileStatic
class NetworkKit {

    static void time(int year, int month, int day, Closure closure) {
        Request request = new Request.Builder().get().url("$App.TIME_URL/$year/$month/$day").build()
        NetworkManager.getInstance().requestString(request, { result ->
            closure(JsonKit.generate(result as String, App.getInstance().getGson()))
        })
    }

    static void type(String type, int pageSize, int pageNo, Closure closure) {
        Request request = new Request.Builder().get().url("$App.TYPE_URL/$type/$pageSize/$pageNo").build()
        NetworkManager.getInstance().request(request, closure, Result.class)
    }

    static void random(String type, int size, Closure closure) {
        Request request = new Request.Builder().get().url("$App.RANDOM_URL/$type/$size").build()
        NetworkManager.getInstance().request(request, closure, Result.class)
    }
}