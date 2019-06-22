package com.github.zerh.xtend;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.InputStream;
import java.util.Arrays;

public class ResourcesFactory extends ContextWrapper {

    public ResourcesFactory(Context base) {
        super(base);
    }

    public <T> T get(int id, Class<T> t) {
        Object result = null;

        if(t.equals(int.class) || t.equals(Integer.class)) result = getAsInt(id);
        if(t.equals(float.class) || t.equals(Float.class)) result = getResources().getDimension(id);
        if(t.equals(String.class)) result = getAsString(id);
        if(t.equals(Drawable.class)) result = ContextCompat.getDrawable(this, id);
        if(t.equals(Animation.class)) result = AnimationUtils.loadAnimation(this, id);
        if(t.equals(ColorStateList.class)) result = ContextCompat.getColorStateList(this, id);
        if(t.equals(Boolean.class)) result = getResources().getBoolean(id);
        if(t.equals(AssetFileDescriptor.class)) result = getResources().openRawResourceFd(id);
        if(t.equals(Movie.class)) result = getResources().getMovie(id);
        if(t.equals(XmlResourceParser.class)) result = getResources().getLayout(id);
        if(t.equals(InputStream.class)) result = getResources().openRawResource(id);
        if(t.equals(Configuration.class)) result = getResources().getConfiguration();
        if(t.equals(AssetManager.class)) result = getResources().getAssets();
        if(t.equals(Bitmap.class)) result = BitmapFactory.decodeResource(getResources(), id);
        if(t.equals(Animator.class)) result = AnimatorInflater.loadAnimator(this, id);
        if(t.equals(StateListAnimator.class)) result = AnimatorInflater.loadStateListAnimator(this, id);
        if(t.equals(ColorStateList.class)) result = getColorStateList(id);

        if(t.equals(Resources.class)) result = getResources();

        if(t.isArray()) {
            if (t.getSimpleName().equals("int[]"))
                result = getResources().getIntArray(id);

            if(t.equals(Integer[].class))
                result = ArrayUtils.toObject(getResources().getIntArray(id));

            if (t.equals(String[].class)) result = getResources().getStringArray(id);
        }
        
        if(t.equals(TypedArray.class)) result = getResources().obtainTypedArray(id);

        if(Build.VERSION.SDK_INT > 26) {
            if(t.equals(Color.class)) {
                int color = ContextCompat.getColor(this, id);
                result = Color.valueOf(color);
            }
            if(t.equals(Typeface.class)) result = getResources().getFont(id);
        }

        try {
            return t.cast(result);
        } catch (ClassCastException e){
            return (T)result;
        }
    }

    public String getAsString(int id){

        return new Persistent<String>(id)
                .tryGet(i -> getResources().getString(id))
                .ifCatch(i -> String.valueOf(ContextCompat.getColor(this, id)))
                .ifCatch(i -> String.valueOf(getResources().getDimension(id)))
                .ifCatch(i -> String.valueOf(getResources().getBoolean(id)))
                .ifCatch(i -> Arrays.toString(getResources().getStringArray(id)))
                .ifCatch(i -> Arrays.toString(getResources().getIntArray(id)))
                .get();
    }

    public int getAsInt(int id){
        return new Persistent<Integer>(id)
                .tryGet(i -> ContextCompat.getColor(this, id))
                .ifCatch(i -> getResources().getBoolean(id)?1:0)
                .ifCatch(i -> Math.round(getResources().getDimension(id)))
                .ifCatch(i -> getResources().getDimensionPixelSize(id))
                .ifCatch(i -> getResources().getDimensionPixelOffset(id))
                .orElse(0);
    }
}
