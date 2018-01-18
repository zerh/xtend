package com.github.zerh.xtend;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by eliezer on 10/29/17.
 */

public class TabLayout extends android.support.design.widget.TabLayout {

    private OnTabSelected onTabSelected;
    private OnTabUnselected onTabUnselected;
    private OnTabReselected onTabReselected;

    private ViewPager viewPager;

    public TabLayout(Context context) {
        super(context);
        init(null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void addTab(String tabText){
        this.addTab(newTab().setText(tabText));
    }

    public void addTabs(String... tabsText){
        for(String ttext : tabsText){
            this.addTab(newTab().setText(ttext));
        }
    }

    private void init(AttributeSet attrs){
        if(attrs!=null && !isInEditMode()) {
            final TypedArray a = getContext()
                    .obtainStyledAttributes(attrs,R.styleable.TabLayout, 0, 0);

            this.addOnTabSelectedListener(new TabSelectedListener());
        }
    }

    public void setupWithViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.setOnPageSelected(new ViewPager.OnPageSelected() {
            @Override
            public void apply(int position) {
                Log.d("setupWithViewPager", "position: " + position);
                getTabAt(position).select();
            }
        });
    }

    public OnTabSelected getOnTabSelected() {
        return onTabSelected;
    }

    public void setOnTabSelected(OnTabSelected onTabSelected) {
        this.onTabSelected = onTabSelected;
    }

    public OnTabUnselected getOnTabUnselected() {
        return onTabUnselected;
    }

    public void setOnTabUnselected(OnTabUnselected onTabUnselected) {
        this.onTabUnselected = onTabUnselected;
    }

    public OnTabReselected getOnTabReselected() {
        return onTabReselected;
    }

    public void setOnTabReselected(OnTabReselected onTabReselected) {
        this.onTabReselected = onTabReselected;
    }

    public interface OnTabSelected {
        void apply(Tab tab);
    }

    public interface OnTabUnselected {
        void apply(Tab tab);
    }

    public interface OnTabReselected {
        void apply(Tab tab);
    }

    class TabSelectedListener implements OnTabSelectedListener {

        @Override
        public void onTabSelected(Tab tab) {

            if(viewPager!=null){
                int position = getSelectedTabPosition();
                viewPager.setCurrentItem(position);
            }

            if(onTabSelected!=null)
                onTabSelected.apply(tab);
        }

        @Override
        public void onTabUnselected(Tab tab) {

            if(onTabUnselected!=null)
                onTabUnselected.apply(tab);
        }

        @Override
        public void onTabReselected(Tab tab) {

            if(onTabReselected!=null)
                onTabReselected.apply(tab);
        }
    }
}
