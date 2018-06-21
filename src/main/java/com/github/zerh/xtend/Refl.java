package com.github.zerh.xtend;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

class Refl {

    private static final String errorTag = "Xtend Error";
    private static final String message = "Parameter needed with method ";

    private Object object;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

    Refl (Object object){
        this.object = object;
    }

    Refl methodName(String methodName){
        this.methodName = methodName;
        return this;
    }

    Refl paramTypes(Class<?>... paramTypes) {
        this.paramTypes = paramTypes;
        return this;
    }

    Refl params(Object... params) {
        this.params = params;
        return this;
    }

    <R> R invoke(Class<R> returnType){
        return invoke(returnType, true);
    }

    <R> R invokeOfSuper(Class<R> returnType){
        return invokeOfSuper(returnType, true);
    }

    <R> R invoke(Class<R> returnType, boolean showLog){
        try {
            Method m = object.getClass()
                    .getMethod(methodName, paramTypes);

            if (returnType!=null) {
                return returnType.cast(m.invoke(object, params));
            } else { m.invoke(object, params); }
        }catch (Exception ex) {
            if(showLog)
                Log.e(Refl.errorTag, Refl.message + "\""+methodName+"\"");
        }
        return null;
    }

    <R> R invokeOfSuper(Class<R> returnType, boolean showLog){
        try {
            Method m = object.getClass()
                    .getSuperclass()
                    .getMethod(methodName, paramTypes);

            if (returnType!=null) {
                return returnType.cast(m.invoke(object, params));
            } else { m.invoke(object, params); }
        }catch (Exception ex) {
            if(showLog)
                Log.e(Refl.errorTag, Refl.message + "\""+methodName+"\"");
        }
        return null;
    }
}
