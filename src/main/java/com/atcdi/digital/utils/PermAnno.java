package com.atcdi.digital.utils;

@Deprecated
public @interface PermAnno {
    String name() default "";

    boolean needAuth() default true;

    String dataAuth() default "";


}
