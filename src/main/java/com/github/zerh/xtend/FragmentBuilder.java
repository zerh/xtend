package com.github.zerh.xtend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBuilder {

    public static Fragment build(Object object) {
        return build(object, null);
    }

    public static Fragment build(Class clazz){
        return build(clazz, null);
    }

    public static Fragment build(Object object, Bundle bundle){
        Frag frag = new Frag();
        frag.setAnnotatedObject(object);
        frag.setArguments(bundle);
        return frag;
    }

    public static Fragment build(Class clazz, Bundle bundle){

        try {
            Fragment frag = build(clazz.newInstance());
            frag.setArguments(bundle);
            return frag;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Frag extends Fragment {

        private Object object;

        public void setAnnotatedObject(Object object){
            this.object = object;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
            return Xtend.map(this, object, inflater, viewGroup);
        }
    }
}
