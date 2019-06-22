package com.github.zerh.xtend;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.View;

import com.github.zerh.xtend.annotation.UI;
import com.github.zerh.xtend.annotation.Click;
import com.github.zerh.xtend.annotation.LongClick;
import com.github.zerh.xtend.annotation.Resource;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationLogic extends ContextWrapper {

    private Bundle bundle;
    private View contentView;
    private Object object;
    private ResourcesFactory resourcesFactory;

    public AnnotationLogic(Context context) {
        super(context);
        resourcesFactory = new ResourcesFactory(context);
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    void processLongClick(Object object, Method method) {

        boolean methodReturnBoolean = (method.getReturnType().isAssignableFrom(boolean.class)
                || method.getReturnType().isAssignableFrom(Boolean.class));

        LongClick longClick = method.getAnnotation(LongClick.class);
        if (longClick != null && methodReturnBoolean) {

            int resId = (longClick.value() != -1) ?
                    method.getAnnotation(LongClick.class).value() :
                    getResource(method.getName());


            method.setAccessible(true);
            View targetView = this.contentView.findViewById(resId);

            targetView.setOnLongClickListener(v -> {
                try {
                    if (method.getParameterTypes().length == 0) {
                        return (boolean) method.invoke(object);
                    } else if (method.getParameterTypes().length == 1) {
                        return (boolean) method.invoke(object, method.getParameterTypes()[0].cast(v));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return false;
            });

        }

    }

    void processUi(Object object, Field field) {
        UI UI = field.getAnnotation(UI.class);
        if (UI != null) {

            int resId = (UI.value() != -1) ?
                    UI.value() :
                    getResource(field.getName());

            field.setAccessible(true);

            try {
                field.set(object, field.getType().cast(this.contentView.findViewById(resId)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    void processClick(Object object, Method method) {

        Click click = method.getAnnotation(Click.class);

        if (click != null) {

            int resId = (click.value() != -1)?
                resId = click.value() : getResource(method.getName());

            method.setAccessible(true);
            View targetView = contentView.findViewById(resId);

            targetView.setOnClickListener(v -> {
                try {
                    if (method.getParameterTypes().length == 0) {
                        method.invoke(object);
                    } else if (method.getParameterTypes().length == 1) {
                        method.invoke(object, method.getParameterTypes()[0].cast(v));
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    void proccessResource(Object object, Field field) {
        Resource resource = field.getAnnotation(Resource.class);
        if (resource != null) {

            int value = resource.value();
            Object fieldValue = resourcesFactory.get(value, field.getType());
            try {
                field.set(object, fieldValue);
            } catch (IllegalAccessException | ClassCastException e) {
                try {
                    field.set(object, field.getType().cast(fieldValue));
                } catch (IllegalAccessException | ClassCastException e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
            }

            field.setAccessible(true);


        }
    }

    int getResource(String resourceName) {
        return getResource(resourceName, ResourceType.ID);
    }

    int getResource(String resourceName, ResourceType resourceType) {
        return getResources().getIdentifier(resourceName, resourceType.defType, getPackageName());
    }

}
