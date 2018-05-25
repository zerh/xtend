package com.github.zerh.xtend.net;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.github.zerh.xtend.net.annotation.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eliezer on 11/19/17.
 */

public class RestBuilder {

    public static <T extends Activity, R> R build(T obj, Class<R> restInterface) {
        return restInterface.cast(Proxy.newProxyInstance(restInterface.getClassLoader(),
                new Class[]{restInterface}, new Handler<T>(obj)));
    }

    static class Handler<T extends Activity> implements InvocationHandler {

        T object;

        public Handler(T object){
            this.object = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            String prefix = "";
            String path = null;
            HttpRequest httpRequest;

            Map<String, MultiPartFile> multiPartFileMap = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            List<String> headers = new ArrayList<>();

            if(method.getDeclaringClass().getAnnotation(Prefix.class)!=null) {
                prefix = method.getDeclaringClass().getAnnotation(Prefix.class).value();
            }

            if (method.getAnnotation(Headers.class) != null) {
                String[] headersValues = method.getAnnotation(Headers.class).value();
                for(String val : headersValues)
                headers.add(val);
            }

            if (method.getAnnotation(GET.class) != null) {
                path = prefix + method.getAnnotation(GET.class).value();
                if(args!=null)
                    path = buildPath(method, args, path);

                httpRequest = new HttpRequest(this.object, path, RequestMethod.GET, params, multiPartFileMap, headers);

                prepareCall(httpRequest, method, args, null, null, headers);

                return httpRequest;

            } else if (method.getAnnotation(HEAD.class) != null) {
                path = prefix + method.getAnnotation(HEAD.class).value();
                if(args!=null)
                    path = buildPath(method, args, path);

                httpRequest = new HttpRequest(this.object, path, RequestMethod.HEAD, params, multiPartFileMap, headers);

                prepareCall(httpRequest, method, args, params, multiPartFileMap, headers);

                return httpRequest;

            } else if (method.getAnnotation(POST.class) != null) {
                path = prefix + method.getAnnotation(POST.class).value();
                if(args!=null)
                    path = buildPath(method, args, path);

                httpRequest = new HttpRequest(this.object, path, RequestMethod.POST, params, multiPartFileMap, headers);

                prepareCall(httpRequest, method, args, params, multiPartFileMap, headers);

                return httpRequest;

            } else if (method.getAnnotation(PUT.class) != null) {
                path = prefix + method.getAnnotation(PUT.class).value();
                if(args!=null)
                    path = buildPath(method, args, path);

                httpRequest = new HttpRequest(this.object, path, RequestMethod.PUT, params, multiPartFileMap, headers);

                prepareCall(httpRequest, method, args, params, multiPartFileMap, headers);

                return httpRequest;

            } else if (method.getAnnotation(DELETE.class) != null) {
                path = prefix + method.getAnnotation(DELETE.class).value();
                if(args!=null)
                    path = buildPath(method, args, path);

                httpRequest = new HttpRequest(this.object, path, RequestMethod.DELETE, params, multiPartFileMap, headers);

                prepareCall(httpRequest, method, args, params, multiPartFileMap, headers);

                return httpRequest;
            }

            return null;
        }

        private void prepareCall(HttpRequest httpRequest, Method method, Object[] args, Map<String, String> params,
                                 Map<String, MultiPartFile> multiPartFileMap, List<String> headers){

            if(args!=null && isMultiPart(method, args)) {
                httpRequest.multipart = true;
                buildMultiPartBody(method, args, params, multiPartFileMap);
                buildHeader(method, args, headers);
            } else if(args!=null){
                httpRequest.multipart = false;
                buildBody(method, args, params);
                buildHeader(method, args, headers);
            }
        }

        private boolean isMultiPart(Method method, Object[] args){
            Annotation[][] annotations = method.getParameterAnnotations();
            for(int i=0; i<args.length; i++) {
                if (annotations[i] != null) {
                    for (int j = 0; j < annotations[i].length; j++) {
                        if (annotations[i][j].annotationType().equals(Part.class)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private void buildHeader(Method method, Object[] args, List<String> headers){
            Annotation[][] annotations = method.getParameterAnnotations();
            for(int i=0; i<args.length; i++){
                if(annotations[i]!=null){
                    for(int j=0; j<annotations[i].length; j++){

                        if(annotations[i][j].annotationType().equals(Header.class)) {
                            Header head = ((Header) annotations[i][j]);
                            String headValue = head.value();

                            headers.add(headValue+": "+args[i].toString());
                        }

                    }
                }
            }
        }

        private void buildBody(Method method, Object[] args, Map<String, String> params){
            if(params!=null) {
                Annotation[][] annotations = method.getParameterAnnotations();
                for (int i = 0; i < args.length; i++) {
                    if (annotations[i] != null) {
                        for (int j = 0; j < annotations[i].length; j++) {

                            if (annotations[i][j].annotationType().equals(Field.class)) {
                                Field field = ((Field) annotations[i][j]);
                                String fieldValue = field.value();

                                params.put(fieldValue, args[i].toString());
                            }

                        }
                    }
                }
            }
        }

        private void buildMultiPartBody(Method method, Object[] args, Map<String, String> params, Map<String, MultiPartFile> multiPartFileMap){
            if(multiPartFileMap!=null){
                Annotation[][] annotations = method.getParameterAnnotations();
                for(int i=0; i<args.length; i++){
                    if(annotations[i]!=null){
                        for(int j=0; j<annotations[i].length; j++){

                            if(annotations[i][j].annotationType().equals(Part.class)) {
                                Part part = ((Part) annotations[i][j]);
                                String partName = part.value();

                                if (args[i] instanceof File[]) {
                                    for (File file : (File[]) args[i]) {
                                        multiPartFileMap.put(partName, getMultiPartFile(file));
                                    }
                                } else if (args[i] instanceof File) {
                                    multiPartFileMap.put(partName, getMultiPartFile((File) args[i]));
                                } else if (args[i] instanceof byte[][]) {
                                    byte[][] files = (byte[][]) args[i];
                                    for (byte[] file : files) {
                                        MultiPartFile multiPartFile = new MultiPartFile(file, "unknown", getMimeType(file));
                                        multiPartFileMap.put(partName, multiPartFile);
                                    }
                                } else if (args[i] instanceof byte[]) {
                                    MultiPartFile multiPartFile = new MultiPartFile((byte[]) args[i], "unknown", getMimeType((byte[]) args[i]) );
                                    multiPartFileMap.put(partName, multiPartFile);
                                } else {
                                    params.put(partName, args[i].toString());
                                }
                            }

                        }
                    }
                }
            }
        }

        private String buildPath(Method method, Object[] args, String path){
            Annotation[][] annotations = method.getParameterAnnotations();
            for(int i=0; i<args.length; i++){
                if(annotations[i]!=null){
                    for(int j=0; j<annotations[i].length; j++){

                        if(annotations[i][j].annotationType().equals(Query.class)){
                            Query param = ((Query)annotations[i][j]);
                            String value = param.value();
                            if(!path.contains("?")){
                                path += "?" + value + "=" + args[i].toString();
                            } else {
                                path += "&" + value + "=" + args[i].toString();
                            }
                        }

                        if(annotations[i][j].annotationType().equals(Path.class)){
                            Path param = ((Path)annotations[i][j]);
                            String value = param.value();
                            path = path.replace("{"+value+"}", args[i].toString());
                        }
                    }
                }
            }
            return path;
        }

        private MultiPartFile getMultiPartFile(File file){
            try {

                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] byteArrayFile = new byte[(int)file.length()];

                String mediaType = getMimeType(file);

                fileInputStream.read(byteArrayFile,0,byteArrayFile.length);
                MultiPartFile multiPartFile = new MultiPartFile(
                        byteArrayFile, file.getName(), mediaType);

                return multiPartFile;
            } catch (IOException e) {
                return null;
            }
        }

        private String getMimeType(File file) {
            Uri uri = Uri.fromFile(file);
            ContentResolver cR = ((Context)object).getContentResolver();
            return cR.getType(uri);
        }

        private String getMimeType(byte[] file){
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(file));
            String mimeType = null;
            try {
                mimeType = URLConnection.guessContentTypeFromStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mimeType;
        }


    }
}

