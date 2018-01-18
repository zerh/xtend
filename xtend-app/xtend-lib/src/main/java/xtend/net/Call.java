package xtend.net;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

public class Call {

    private final Object object;
    private final String url;

    private Response response;
    private Map<String, String> params;
    private Map<String, MultiPartFile> multiPartFileMap;
    private List<String> headers;
    private boolean done;
    private RequestMethod requestMethod;
    private Gson gson;

    boolean multipart;

    Call(Object object, String url, RequestMethod requestMethod,
         Map<String, String> params, Map<String, MultiPartFile> multiPartFileMap, List<String> headers){

        this.object = object;
        this.url = url;
        this.params = params;
        this.multiPartFileMap = multiPartFileMap;
        this.headers = headers;
        this.requestMethod = requestMethod;

        gson = new Gson();
    }

    public <T> void execute(Callback<T> callback, Class<T> clazz){
        execute(r -> {
            T obj = null;
            try {
                Object fromJson = gson.fromJson(response.body(), clazz);
                if(fromJson!=null) {
                    obj = clazz.cast(fromJson);
                    callback.apply(obj);
                }
            } catch (JsonSyntaxException ex) {
                //ex.printStackTrace();
                Log.e("JsonSyntaxException", ex.getMessage());

            }

        });
    }

    public void execute(ResponseCallback callback){
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

                Call.this.response.setHeaders(okResp.headers().toMultimap());

                if(okResp.body()!=null) {
                    try {
                        Call.this.response.rawBody(okResp.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Call.this.response.setStatus(okResp.code());

                done = true;

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(callback!=null){
                            callback.apply(Call.this.response);
                        }
                    }
                };

                if(object instanceof Fragment) {
                    ((Fragment) object).getActivity().runOnUiThread(runnable);
                } else if ( object instanceof Activity ) {
                    ((Activity) object).runOnUiThread(runnable);
                }


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
