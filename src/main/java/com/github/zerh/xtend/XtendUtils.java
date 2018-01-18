package com.github.zerh.xtend;

/**
 * Created by eliezer on 10/29/17.
 */
public class XtendUtils {

    /*public static <T> void action(XActivity xActivity, String mName, Class<T> tClass, T t){
        if (xActivity.getMethodMap().get(mName) != null) {
            Method m = xActivity.getMethodMap().get(mName);
            try {

                if(m.getParameterTypes().length==0) {
                    m.invoke(xActivity);
                } else if(m.getParameterTypes().length==1
                        && m.getParameterTypes()[0].isAssignableFrom(tClass)) {

                    m.invoke(xActivity, tClass.cast(t));
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ERROR", "Not method found with name " + mName);
        }
    }*/

//    public static void action(Object obj, String mName, Class[] tClass, Object[] params){
//        if (obj != null) {
//            try {
//                Method m = obj.getClass().getMethod(mName, tClass);
//                if(params==null) {
//                    m.invoke(obj);
//                } else {
//                    m.invoke(obj, params);
//                }
//
//            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e("ERROR", "Not method found with name " + mName);
//        }
//    }
}
