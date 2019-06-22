package com.github.zerh.xtend.annotation;

import com.github.zerh.xtend.ResourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Resource {
    int value();
    ResourceType resourceType() default ResourceType.NULL;
}
