package com.github.zerh.xtend;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.zerh.xtend.annotation.*;
import com.github.zerh.xtend.net.RestBuilder;
import com.github.zerh.xtend.net.annotation.RestService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class Xtend {

    public static Context context;

    public static ListView l;

    public static <T extends Activity> void map(T object) {

        if (object.getClass().getAnnotation(ContentView.class) != null) {
            int viewId = object.getClass().getAnnotation(ContentView.class).value();
            object.setContentView(viewId);
        }
        View view = object.findViewById(android.R.id.content);

        String pkName = object.getPackageName();

        for (Field field : object.getClass().getDeclaredFields()) {
            processUi(object.getResources(), pkName, object, field, view);
            processRestService(object, field);
        }

        for (Method method : object.getClass().getDeclaredMethods()) {
            processClick(object.getResources(), pkName, object, method, view);
            processLongClick(object.getResources(), pkName, object, method, view);
            processSectionPagerAdapter(object::findViewById, object, method);
            processPostInflated(object, method);
        }
    }

    public static <T extends Fragment> void map(T object, View view) {

        if (object.getClass().getAnnotation(ContentView.class) != null) {
            int viewId = object.getClass().getAnnotation(ContentView.class).value();
            LayoutInflater.from(object.getActivity())
                    .inflate(viewId, ((ViewGroup)object.getView().getParent()), false);
        }

        String pkName = object.getActivity().getPackageName();

        for (Field field : object.getClass().getDeclaredFields()) {
            processUi(object.getResources(), pkName, object, field, view);
            processRestService(object.getActivity(), field);
        }

        for (Method method : object.getClass().getDeclaredMethods()) {
            processClick(object.getResources(), pkName, object, method, view);
            processLongClick(object.getResources(), pkName, object, method, view);
            processSectionPagerAdapter(object.getView()::findViewById, object, method);
            processPostInflated(object, method);
        }

    }


    static <T extends Fragment> View map(T object, LayoutInflater inflater, ViewGroup container) {

        View view = null;
        if (object.getClass().getAnnotation(ContentView.class) != null) {

            ContentView contentView = object.getClass().getAnnotation(ContentView.class);
            int id = contentView.value();
            boolean attachToRoot = contentView.attachToRoot();

            view = inflater.inflate(id, container, attachToRoot);
        }

        String pkName = object.getActivity().getPackageName();

        for (Field field : object.getClass().getDeclaredFields()) {
            processUi(object.getResources(), object.getActivity().getPackageName(), object, field, view);
            processRestService(object.getActivity(), field);
        }

        for (Method method : object.getClass().getDeclaredMethods()) {
            processClick(object.getResources(), pkName, object, method, view);
            processLongClick(object.getResources(), pkName, object, method, view);
            //processSectionPagerAdapter(object.getView()::findViewById, object, method);
            processPostInflated(object, method);
        }


        return view;
    }

    static <T extends Activity> void processRestService(T object, Field field) {
        if (field.getAnnotation(RestService.class) != null) {
            field.setAccessible(true);
            try {
                field.set(object, field.getType().cast(RestBuilder.build(object, field.getType())));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    static void processPostInflated(Object object, Method method) {
        if (method.getAnnotation(PostInflated.class) != null) {
            try {
                if (method.getParameterTypes().length == 0) {
                    method.invoke(object);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    static void processUi(Resources resources, String pkName, Object object, Field field, View view) {
        if (field.getAnnotation(UI.class) != null) {

            int resId = -1;

            if (field.getAnnotation(UI.class).value() != -1) {
                resId = field.getAnnotation(UI.class).value();
            } else {
                resId = resources.getIdentifier(field.getName(), "id", pkName);
            }

            field.setAccessible(true);

            try {
                field.set(object, field.getType().cast(view.findViewById(resId)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    static void processClick(Resources resources, String pkName, Object object, Method method, View view) {
        if (method.getAnnotation(Click.class) != null) {

            int resId = -1;

            if (method.getAnnotation(Click.class).value() != -1) {
                resId = method.getAnnotation(Click.class).value();
            } else {
                resId = resources.getIdentifier(method.getName(), "id", pkName);
            }

            method.setAccessible(true);
            View targetView = view.findViewById(resId);

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

    static void processLongClick(Resources resources, String pkName, Object object, Method method, View view) {

        boolean methodReturnBoolean = (method.getReturnType().isAssignableFrom(boolean.class)
                || method.getReturnType().isAssignableFrom(Boolean.class));

        if (method.getAnnotation(LongClick.class) != null && methodReturnBoolean) {

            int resId = -1;

            if (method.getAnnotation(LongClick.class).value() != -1) {
                resId = method.getAnnotation(LongClick.class).value();
            } else {
                resId = resources.getIdentifier(method.getName(), "id", pkName);
            }

            method.setAccessible(true);
            View targetView = view.findViewById(resId);

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

    static void processSectionPagerAdapter(Function<Integer, View> findById, Object object, Method method) {
        if (method.getAnnotation(SectionPagerAdapter.class) != null) {

            int id = method.getAnnotation(SectionPagerAdapter.class).id();
            int count = method.getAnnotation(SectionPagerAdapter.class).count();

            ViewPager viewPager = (ViewPager) findById.apply(id);
            viewPager.setAdapter(new SectPagerAdapter(new Refl(object)
                    .methodName("getSupportFragmentManager")
                    .invoke(FragmentManager.class), object, method, count));
        }

    }

    private static class SectPagerAdapter extends FragmentPagerAdapter {

        Object object;
        Method method;
        int count;

        public SectPagerAdapter(FragmentManager fragmentManager, Object object, Method method, int count) {
            super(fragmentManager);
            this.object = object;
            this.method = method;
            this.count = count;
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return (Fragment) method.invoke(object, position);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public int getCount() {
            return count;
        }
    }

}
