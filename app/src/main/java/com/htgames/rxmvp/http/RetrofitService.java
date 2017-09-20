package com.htgames.rxmvp.http;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.htgames.rxmvp.DemoApplication;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yudenghao on 2017/9/15.
 */

public class RetrofitService {
    private static final String TAG = RetrofitService.class.getSimpleName();
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    //设缓存有效期为1天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
    // TODO: 2017/9/17 需要一个url
    private static IApi iApi;

    private RetrofitService() {
        throw new AssertionError();
    }

    public static void init() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(DemoApplication.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
        OkHttpClient client = new OkHttpClient().newBuilder().cache(cache)
                .retryOnConnectionFailure(true)   //是否自动重连
                .addInterceptor(ADD_HEADER)       //添加请求头
                .addInterceptor(HEADAR_INTERCEPTOR)//aplication拦截器，他只会在response被调用一次
                .addNetworkInterceptor(HEADAR_INTERCEPTOR)      //addNetworkInterceptor添加的是网络拦截器，他会在在request和resposne是分别被调用一次
                .connectTimeout(10, TimeUnit.SECONDS)//超时时间15S
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())  //Goson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())  //Rxjava
                .baseUrl(NET.HOST)
                .build();
        iApi = retrofit.create(IApi.class);
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor HEADAR_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();//获取请求
            long t1 = System.nanoTime();//请求发起的时间
            Logger.i(String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(),
                    "请求头:" + request.headers(),
                    "请求体:" + request.body().toString()));
            ///判读的网络条件 要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
            if (!isNetworkAvailable(DemoApplication.getContext())) {
                // 如果没有网络，则启用 FORCE_CACHE
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Logger.i(TAG, "网络连接异常");
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            if (isNetworkAvailable(DemoApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                //这里不能直接使用response.body().string()的方式输出日志
                //因为response.body().string()之后，response中的流会被关闭，程序会报错，需要创建出一
                //个新的response给应用层处理
                ResponseBody responseBody = response.peekBody(1024 * 1024);
                Logger.i(TAG, String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                        response.request().url(),
                        responseBody.toString(),
                        (t2 - t1) / 1e6d,
                        response.headers()));
                Logger.i(TAG, "REQUEST_URL==>>" + response.headers().toString() + "/n" + "REQUEST_URL==>>"
                        + response.request().url() + "REQUEST_RESULT==>>" + response.body().toString());
                return response.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build();
            }
        }
    };


    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            return null != info && info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static final Interceptor ADD_HEADER = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            Map<String, String> header = createHeader();
            for (Map.Entry<String, String> ent : header.entrySet()) {
                builder.addHeader(ent.getKey(), ent.getValue());
            }
            Request build = builder.build();
            return chain.proceed(build);
        }
    };

    private static String parseParams(RequestBody requestBody, Buffer buffer) throws UnsupportedEncodingException {
        if (requestBody.contentType() != null && !requestBody.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(buffer.readUtf8(), "UTF-8");
        }
        return "null";
    }


    private static Map<String, String> createHeader() {
        Map<String, String> header = new HashMap<>();
        return header;
    }



}
