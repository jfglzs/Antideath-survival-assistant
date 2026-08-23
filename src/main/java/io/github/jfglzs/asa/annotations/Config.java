package io.github.jfglzs.asa.annotations;

import io.github.jfglzs.asa.config.Tab;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    Tab[] tab() default Tab.ALL;
}
