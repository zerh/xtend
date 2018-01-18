package xtend;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eliezer on 10/30/17.
 */

public class ViewPager extends android.support.v4.view.ViewPager {

    private OnPageSelected onPageSelected;
    private OnPageScrolled onPageScrolled;
    private OnPageScrollStateChanged onPageScrollStateChaged;

    private String[] pageTitles;
    private List<Fragment> fragmentList;

    public ViewPager(Context context) {
        super(context);
        init(null);
    }

    public ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.setCurrentItem(0);
        this.addOnPageChangeListener(new PageChangeListener());
    }

    public void setFragments(List<Fragment> fragments){
        FragmentManager fm = ((Activity)getContext()).getSupportFragmentManager();
        fragmentList =  fragments;
        this.setAdapter(new FragPagerAdapter(fm));
    }

    public void setFragments(Fragment... fragments){
        setFragments(Arrays.asList(fragments));
    }

    public List<Fragment> getFragments(){
        return fragmentList;
    }

    public void setPageTitles(String... pageTitles){
        this.pageTitles = pageTitles;
    }

    public String[] getPageTitles(){
        return pageTitles;
    }

    public OnPageSelected getOnPageSelected() {
        return onPageSelected;
    }

    public void setOnPageSelected(OnPageSelected onPageSelected) {
        this.onPageSelected = onPageSelected;
    }

    public OnPageScrolled getOnPageScrolled() {
        return onPageScrolled;
    }

    public void setOnPageScrolled(OnPageScrolled onPageScrolled) {
        this.onPageScrolled = onPageScrolled;
    }

    public OnPageScrollStateChanged getOnPageScrollStateChaged() {
        return onPageScrollStateChaged;
    }

    public void setOnPageScrollStateChaged(OnPageScrollStateChanged onPageScrollStateChaged) {
        this.onPageScrollStateChaged = onPageScrollStateChaged;
    }

    public interface OnPageScrolled{
        void apply(int position, float positionOffset, int positionOffsetPixels);
    }

    public interface OnPageSelected{
        void apply(int position);
    }

    public interface OnPageScrollStateChanged{
        void apply(int state);
    }

    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(onPageScrolled!=null)
                onPageScrolled.apply(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if(onPageSelected!=null)
                onPageSelected.apply(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(onPageScrollStateChaged!=null)
                onPageScrollStateChaged.apply(state);
        }
    }

    private class FragPagerAdapter extends FragmentPagerAdapter {

        public FragPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public List<Fragment> getFragments(){
            return fragmentList;
        }

        @Override
        public Fragment getItem(int pos) {
            return fragmentList.get(pos);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles!=null?pageTitles[position]:null;
        }
    }
}
