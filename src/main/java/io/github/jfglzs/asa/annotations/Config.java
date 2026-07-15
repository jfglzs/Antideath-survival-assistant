package io.github.jfglzs.asa.annotations;

import io.github.jfglzs.asa.config.Tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    Tab[] tab() default Tab.ALL;
}
