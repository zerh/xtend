package com.github.zerh.xtend.net;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by eliezer on 12/17/17.
 */

class HttpUtil {

    static okhttp3.Response httpGet(String url) {
        Request request = new Request.Builder().url(url).build();
        return exec(request);
    }

    static okhttp3.Response httpHead(String url) {
        Request request = new Request.Builder().url(url).head().build();
        return exec(request);
    }


    static <T extends RequestBody> okhttp3.Response httpCall(RequestMethod method, String url, RequestBody requestBody, Class<T> t) {

        Request.Builder builder = new Request.Builder().url(url);

        if(method.equals(RequestMethod.GET)) builder.get();

        if(method.equals(RequestMethod.HEAD)) builder.head();

        if(method.equals(RequestMethod.POST)) {
            builder.post(t.cast(requestBody));
        } else if(method.equals(RequestMethod.PUT)) {
            builder.put(t.cast(requestBody));
        } else if(method.equals(RequestMethod.DELETE)) {
            builder.delete(t.cast(requestBody));
        }

        return exec(builder.build());
    }

    static okhttp3.Response exec(Request request){
        OkHttpClient client = new OkHttpClient();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
