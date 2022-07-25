package com.ytx.erp.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：自定义响应ResponseBody
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */

    @Override
    public T convert(ResponseBody responseBody) throws IOException {

        try {
            String response = responseBody.string();
            return adapter.fromJson(response);
        } catch(Exception e) {
        } finally {
            responseBody.close();
        }

        return null;
    }

}