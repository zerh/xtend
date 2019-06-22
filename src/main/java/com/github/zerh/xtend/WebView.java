package com.github.zerh.xtend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

public class WebView extends android.webkit.WebView {
    public WebView(Context context) {
        super(context);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                super.onPageFinished(view, url);
                Activity.Doc doc = new Activity.Doc(webView);
                //doc.execute(ugly);
               // onRendered(doc);

                String hola = "hola " +
                        "mndo ";

                StringBuilder sb;

                Map<String, String> map = new HashMap<>();

                Iterator testIterator = map.entrySet().iterator();
                while (testIterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) testIterator.next();


                }

                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {

                }


                File file = new File("");

                System.out.println(new File(file.getPath()).getParentFile().getParentFile().getAbsolutePath());
                System.out.println(file.getParentFile().getParentFile().getAbsoluteFile());
            }
        });
    }



    class Test
    {

        private List<String> testList = new ArrayList<String>();

        public Test(List<String> testList)
        {
            if(testList != null)
            {
                this.testList.clear();
            }

        }
    }


    void p(List<Object> pepe) throws InterruptedException{
//        try {
            Thread.sleep(1000);
//        }catch (InterruptedException ex){
//            throw ex;
//        }
    }

    private final List<String> testList = Collections.EMPTY_LIST;

    public static class Doc {

        private android.webkit.WebView webView;
        private StringBuilder stringBuilder;
        private Object objectToBind;


        public void bind(Object objectToBind) {
            this.objectToBind = objectToBind;
        }

        Object getObjectToBind() {
            return objectToBind;
        }


        Doc(android.webkit.WebView webView) {
            this.webView = webView;

        }

        public Doc $(String el) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("$('" + el + "')");
            return this;
        }

        public Doc css(String prop, String value) {
            if (stringBuilder != null)
                stringBuilder.append(".css('" + prop + "','" + value + "')");
            return this;
        }

        public Doc execute() {
            execute(null);
            return this;
        }

        @SuppressLint("ObsoleteSdkInt")
        public Doc execute(String javascript) {

            String script = javascript != null ? javascript : stringBuilder.toString();
            stringBuilder = new StringBuilder();

            Log.d("pepe", stringBuilder.toString());

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(script, null);
            } else {
                webView.loadUrl("javascript:" + script);
            }

            return this;
        }
    }


}
