package com.example.spokenwapp.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;
import dagger.android.DaggerIntentService;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface LoadVideoKey {
    Class<? extends DaggerIntentService> value();
}
