package com.ytx.erp.retrofit;


import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：拦截器
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain != null) {
            Request request = chain.request();
            Log.e("返回值", "请求连接：" + request.url().toString());
            Response response = chain.proceed(chain.request());
            Log.e("返回值", response.request().url() + " request:" + request.toString());
            long t1 = System.nanoTime();
            long t2 = System.nanoTime();
            Log.e("返回值", String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.e("返回值", response.request().url() + " response body:" + content);
            if (response.body() != null) {
                ResponseBody body = ResponseBody.create(mediaType, content);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        }
        return null;
    }
}
