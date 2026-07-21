package com.iy.api.common.annotation;

import com.iy.api.common.enums.SensitiveType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {
    
    SensitiveType type() default SensitiveType.CUSTOM;
    
    int start() default 0;
    
    int end() default 0;
    
    String mask() default "*";
}