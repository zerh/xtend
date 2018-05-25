package com.github.zerh.xtend.net;

import android.app.Activity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by eliezer on 11/19/17.
 */

public class HttpRequest {

    private HttpRequest me;
    private final Activity act;
    private final String url;
    private Response response;
    private Map<String, String> params;
    private Map<String, MultiPartFile> multiPartFileMap;
    private List<String> headers;
    private boolean done;
    private RequestMethod requestMethod;

    boolean multipart;

    <T extends Activity> HttpRequest(T act,
                                     String url,
                                     RequestMethod requestMethod,
                                     Map<String, String> params,
                                     Map<String, MultiPartFile> multiPartFileMap,
                                     List<String> headers){
        this.act = act;
        this.url = url;
        this.params = params;
        this.multiPartFileMap = multiPartFileMap;
        this.headers = headers;
        this.requestMethod = requestMethod;
        me = this;
    }

    public void callback(Callback<Response> callback){
        response = new Response();
        Runnable runnable = new Runnable() {
            okhttp3.Response okResp;
            @Override
            public void run() {

                if(requestMethod.equals(RequestMethod.GET)) {
                    okResp = HttpUtil.httpGet(url);
                } else if(requestMethod.equals(RequestMethod.HEAD)) {
                    okResp = HttpUtil.httpHead(url);
                } else {

                    Class type = multipart ? MultipartBody.class : FormBody.class;

                    okResp = HttpUtil
                            .httpCall(
                                    requestMethod, url,
                                    multipart?getMultipartBody():getFormBody(), type);
                }

                HttpRequest.this.response.setHeaders(okResp.headers().toMultimap());

                if(okResp.body()!=null) {
                    try {
                        HttpRequest.this.response.rawBody(okResp.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                HttpRequest.this.response.setStatus(okResp.code());

                done = true;

                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(callback!=null){
                            callback.accept(HttpRequest.this.response);
                        }
                    }
                });

            }
        };

        new Thread(runnable).start();
    }

    private FormBody getFormBody(){
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (String key : params.keySet())
            formBuilder.add(key, params.get(key));

        return formBuilder.build();
    }

    private MultipartBody getMultipartBody(){
        MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder();
        for (String key : params.keySet())
            multiPartBuilder.addFormDataPart(key, params.get(key));

        for (String key : multiPartFileMap.keySet()) {

            MultiPartFile mpFile = multiPartFileMap.get(key);

            multiPartBuilder.addFormDataPart(key,
                    mpFile.getName(),
                    RequestBody
                            .create(MediaType.parse(mpFile.getMediaType()),
                                    mpFile.getFile())
            );

        }

        return multiPartBuilder.build();
    }

    public Response response() {
        return response;
    }

    public boolean isDone() {
        return done;
    }
}
