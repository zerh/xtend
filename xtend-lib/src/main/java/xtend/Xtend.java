package xtend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.github.zerh.xtend.annotation.ContentView;
import com.github.zerh.xtend.annotation.OnCreateOptionsMenu;
import com.github.zerh.xtend.annotation.PostInflated;
import com.github.zerh.xtend.annotation.UI;
import com.github.zerh.xtend.net.RestBuilder;
import com.github.zerh.xtend.net.annotation.RestService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by eliezer on 11/12/17.
 */

public class Xtend {

    private static String ID = "id";

    public static void map(AppCompatActivity activity, Bundle bundle){

        int optionsMenuRes;

        processRestService(activity);

        if(activity.getClass().getAnnotation(ContentView.class) != null) {
            int viewId = activity.getClass().getAnnotation(ContentView.class).value();
            activity.setContentView(viewId);
            View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            Log.d("Yaa", view + "");
            processUi(activity, view);
            processPostInflated(activity);
        }

        if(activity.getClass().getAnnotation(OnCreateOptionsMenu.class) != null) {
            optionsMenuRes = activity.getClass().getAnnotation(OnCreateOptionsMenu.class).value();
            PopupMenu p  = new PopupMenu(activity, null);
            Menu menu = p.getMenu();
            activity.getMenuInflater().inflate(optionsMenuRes, menu);
        }
    }

    public static View map(Fragment fragment, LayoutInflater inflater, ViewGroup container){

        processRestService(fragment);

        if(fragment.getClass().getAnnotation(ContentView.class) != null) {
            int id = fragment.getClass()
                    .getAnnotation(ContentView.class).value();
            View view = inflater.inflate(id, container, false);
            processUi(fragment, view);
            processPostInflated(fragment);
            return view;
        }

        return null;
    }

    public static void processRestService(Object obj){
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getAnnotation(RestService.class) != null) {
                field.setAccessible(true);
                try {
                    field.set(obj, field.getType().cast(RestBuilder.build(obj, field.getType())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void processPostInflated(Object object){
        for (Method method : object.getClass().getDeclaredMethods()) {
            if(method.getAnnotation(PostInflated.class)!=null) {
                try {
                    if(method.getParameterTypes().length==0) {
                        method.invoke(object);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void processUi(Object obj, View view){
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getAnnotation(UI.class) != null) {

                int resId = getAnnotatedFieldId(field, obj);

                field.setAccessible(true);

                try {
                    field.set(obj, field.getType().cast(view.findViewById(resId)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static int getAnnotatedFieldId(Field field, Object objectContainer){
        int resId = field.getAnnotation(UI.class).value();
        if(resId==-1) {
            if(objectContainer instanceof Activity) {
                resId = ((Activity) objectContainer).getResources()
                        .getIdentifier(field.getName(), ID, ((Activity) objectContainer)
                                .getPackageName());
            } else if(objectContainer instanceof Fragment) {
                resId = ((Fragment) objectContainer).getResources()
                        .getIdentifier(field.getName(), ID, ((Fragment) objectContainer)
                                .getActivity()
                                .getPackageName());
            }
        }
        return resId;
    }
}
