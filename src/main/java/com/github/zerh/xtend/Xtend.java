package com.github.zerh.xtend;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class Xtend {

    private Context context;
    private AnnotationScanner annotationMapper;
    private View contentView;

    public Xtend(Activity activity) {
        this.context=activity;
        annotationMapper = new AnnotationScanner(activity);
        annotationMapper.mapActivity(activity);
    }

    public Xtend(Fragment fragment) {
        this.context=fragment.getContext();
        annotationMapper = new AnnotationScanner(fragment.getContext());
        annotationMapper.mapFragment(fragment);
        this.contentView = annotationMapper.getContentView();
    }

    public Xtend(Object object, View view) {
        this.context = view.getContext();
        annotationMapper = new AnnotationScanner(view.getContext());
        annotationMapper.mapObject(object, view);
        this.contentView = annotationMapper.getContentView();
    }

    public View getContentView(){
        return this.contentView;
    }
}
