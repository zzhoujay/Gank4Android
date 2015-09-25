package zhou.gank.io.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import groovy.transform.CompileStatic
import zhou.gank.io.model.Result
import zhou.gank.io.util.LogKit

import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

//@CompileStatic
class NetworkManager {

    OkHttpClient client
    Gson gson
    Handler handler
    Closure defaultHandle
    Context context

    private static NetworkManager networkManager;

    static NetworkManager getInstance() {
        return networkManager
    }

    static void init(Context context, Gson gson) {
        networkManager = new NetworkManager(context, gson)
    }

    private NetworkManager(Context context, Gson gson) {
        client = new OkHttpClient()
        client.setConnectTimeout(3, TimeUnit.SECONDS)
        client.setReadTimeout(3, TimeUnit.SECONDS)
        client.setWriteTimeout(3, TimeUnit.SECONDS)
        this.gson = gson;
        this.context = context
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
                LogKit.d("requestString", body)
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
                handler.post({
                    closure(defaultHandle?.call(e))
                })
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
                handler.post({
                    closure(defaultHandle?.call(e))
                })
            }

            @Override
            void onResponse(Response response) throws IOException {
                String body = response.body().string()
                Log.d("success", body)
                def result = gson.fromJson(body, type)
                handler.post({
                    closure(result)
                })
            }
        })
    }

    boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo()
        networkInfo != null && networkInfo.isAvailable()
    }
}