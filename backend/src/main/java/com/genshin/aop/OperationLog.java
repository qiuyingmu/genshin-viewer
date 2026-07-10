package com.genshin.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String action() default "";
    String resource() default "";
    String detail() default "";
}
