package com.github.zerh.xtend.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.zerh.xtend.function.Function;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Function<Integer, Fragment> onGetItemListener;
    private int count;

    public ViewPagerAdapter(
            Context context,
            FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public ViewPagerAdapter onGetItemListener(int count, Function<Integer, Fragment> onGetItemListener) {
        this.count = count;
        this.onGetItemListener = onGetItemListener;
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        Object object = onGetItemListener.apply(position);
        return (Fragment) object;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}