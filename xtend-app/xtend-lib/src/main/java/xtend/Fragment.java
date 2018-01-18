package xtend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by eliezer on 10/30/17.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = Xtend.map(this, inflater,container);
        return view;
    }

    public Fragment arg(String key, int value){
        if(this.getArguments()==null){
            this.setArguments(new Bundle());
        }
        this.getArguments().putInt(key, value);

        return this;
    }

}
