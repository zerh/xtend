package com.github.zerh.xtend.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import com.github.zerh.xtend.function.Function;

import java.util.List;

public class Adapters {

    FragmentManager fragmentManager;
    Context context;


    public Adapters(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    /**
     *
     * @param count receive the number of fragments to be generated
     * @param functionalPagerAdapter lambda expression with the detail of creation of fragments
     * @return ViewPagerAdapter, a subclass of FragmentPageAdapter
     */
    public ViewPagerAdapter fragmentPagerAdapter(
            int count,
            Function<Integer, Object> functionalPagerAdapter){

        return new ViewPagerAdapter(context, fragmentManager);//, count, functionalPagerAdapter);
    }

    @SuppressWarnings("unchecked")
    public <V extends RecyclerView.ViewHolder, T> RecyclerViewAdapter recyclerViewAdapter(Class<V> clazz, List<T> data) {
        RecyclerViewAdapter<V, T> rvAdapter = new RecyclerViewAdapter<>(clazz, data);
        return rvAdapter;
    }

    @SuppressWarnings("unchecked")
    public <V extends RecyclerView.ViewHolder, T> RecyclerViewAdapter recyclerViewAdapter(Class<V> clazz, T... data) {
        RecyclerViewAdapter<V, T> rvAdapter = new RecyclerViewAdapter<>(clazz, data);
        return rvAdapter;
    }

    public <T> ArrayAdapter arrayAdapter(Context context, List<T> data){
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(context, android.R.layout.simple_list_item_1, data);
        return null;
    }
}
