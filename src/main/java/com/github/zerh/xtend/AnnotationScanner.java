package com.github.zerh.xtend;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.github.zerh.xtend.annotation.Layout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnnotationScanner extends ContextWrapper {

    private List<Map.Entry<Class, Object>> objectsToInject;
    private Object object;
    private View contentView;
    private AnnotationLogic annotationLogic;

    public AnnotationScanner(Context context){

        super(context);
        objectsToInject = new ArrayList<>();
    }

    public <T extends Activity> void mapActivity(T activity) {

        this.object = activity;

        Layout layoutAnnotation = getLayoutAnnotation(object);
        if (layoutAnnotation != null) {
            int viewId = activity.getClass().getAnnotation(Layout.class).value();
            activity.setContentView(viewId);
        }

        View view = activity.findViewById(android.R.id.content);
        mapObject(activity, view);
    }

    public <T extends Fragment> void mapFragment(T fragment) {

        this.object = fragment;
        Layout layout = getLayoutAnnotation(fragment);

        if (layout != null) {
            int viewId = layout.value();
            View view = LayoutInflater.from(fragment.getContext())
                    .inflate(viewId, null);

            mapObject(fragment, view);
        }
    }

    public void mapObject(Object object, View view) {
        this.contentView = view;
        initAnnotationProcessor();
        mapFields(object);
        mapMethods(object);
    }

    private Layout getLayoutAnnotation(Object object){
        return object.getClass().getAnnotation(Layout.class);
    }

    public View getContentView(){
        return this.contentView;
    }

    void initAnnotationProcessor() {
        annotationLogic = new AnnotationLogic(this);
        annotationLogic.setObject(this.object);
        annotationLogic.setContentView(this.contentView);
    }

    void mapFields(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            annotationLogic.proccessResource(object, field);
            annotationLogic.processUi(object, field);
        }
    }

    void mapMethods(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            annotationLogic.processClick(object, method);
            annotationLogic.processLongClick(object, method);
            //annotationLogic.processPostInflated(object, method);
        }
    }
}
