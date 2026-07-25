package com.iy.api.common.annotation;

import com.iy.api.common.enums.OperationType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

    String module() default "";

    OperationType type() default OperationType.OTHER;

    String description() default "";

    boolean saveParams() default true;

    boolean saveResult() default false;
}
