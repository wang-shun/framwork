package com.gome.test.gtp.model;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({METHOD, FIELD, TYPE})
@Retention(RUNTIME)

public @interface DictionaryMap {

    String parent() default "";

    String keyColName() default "";
}
