package com.github.zerh.xtend;

import android.content.Context;
import android.content.ContextWrapper;

import com.github.zerh.xtend.annotation.*;

public class XtendFactory2 extends ContextWrapper {
    public XtendFactory2(Context base) {
        super(base);
    }

    public <T> T get() {
       return null;
    }
}
