package zhou.gank.io.net

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import groovy.transform.CompileStatic

import java.lang.reflect.Type

@CompileStatic
class NetworkManager {

    OkHttpClient client
    Gson gson
    Handler handler;
    Closure defaultHandle

    private static NetworkManager networkManager;

    static NetworkManager getInstance() {
        return networkManager
    }

    static void init(Gson gson) {
        networkManager = new NetworkManager(gson)
    }

    private NetworkManager(Gson gson) {
        client = new OkHttpClient();
        this.gson = gson;
        handler = new Handler(Looper.getMainLooper())
    }

    void requestString(Request r, Closure closure) {
        client.newCall(r).enqueue(new Callback() {
            @Override
            void onFailure(Request request, IOException e) {
                handler.post({
                    closure(e)
                })
            }

            @Override
            void onResponse(Response response) throws IOException {
                String body = response.body().string();
                handler.post({
                    closure(body)
                })
            }
        })
    }

    public <T> void request(Request r, Closure closure, Class<T> aClass) {
        client.newCall(r).enqueue(new Callback() {
            @Override
            void onFailure(Request request, IOException e) {

            }

            @Override
            void onResponse(Response response) throws IOException {
                String body = response.body().string()
                Log.d("success", body)
                def result = gson.fromJson(body, aClass);
                handler.post({
                    closure(result)
                })
            }
        })
        Log.d("request", r.urlString())
    }

    public <T> void request(Request r, Closure closure, Type type) {
        client.newCall(r).enqueue(new Callback() {
            @Override
            void onFailure(Request request, IOException e) {

            }

            @Override
            void onResponse(Response response) throws IOException {
                String body = response.body().string()
                def result = gson.fromJson(body, type)
                handler.post({
                    closure(result)
                })
            }
        })
    }
}