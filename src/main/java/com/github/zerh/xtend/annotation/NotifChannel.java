package com.github.zerh.xtend.annotation;

import android.app.Notification;
import android.app.NotificationManager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotifChannel {
    String value();
    //String name();
    int importance() default NotificationManager.IMPORTANCE_DEFAULT;
    boolean enableLight() default  false;
    boolean enableVibration() default false;
    int lightColor() default -1;
    int lockscreenVisibility() default Notification.VISIBILITY_PRIVATE;
}
