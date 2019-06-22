package com.github.zerh.xtend.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.zerh.xtend.annotation.Layout;
import com.github.zerh.xtend.function.BiConsumer;

import java.util.Arrays;
import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;

public class RecyclerViewAdapter<V extends ViewHolder, T> extends RecyclerView.Adapter <V> {

    private List<T> data;
    private BiConsumer<V, T> onBindViewHolder;
    private Class<V> clazz;

    public RecyclerViewAdapter(Class<V> clazz, List<T> data) {
        this.data = data;
        this.clazz = clazz;
    }

    public RecyclerViewAdapter(Class<V> clazz, T... data) {
        this.data = Arrays.asList(data);
        this.clazz = clazz;
    }

    public RecyclerViewAdapter onBindViewHolderListener(BiConsumer<V, T> onBindViewHolder){
        this.onBindViewHolder = onBindViewHolder;
        return this;
    }

    private V getInstanceOfViewHolder(View view) {
        try {
            V v = clazz.getConstructor(View.class).newInstance(view);
            return v;
        } catch (Exception e) {
            throw new RuntimeException("class " + clazz.getSimpleName() + " must be static when used as an internal class");
        }
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {

        Layout layout = clazz.getAnnotation(Layout.class);

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout.value(), parent, false);
        return getInstanceOfViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(V viewHolder, int position) {
        onBindViewHolder.accept(viewHolder, data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
