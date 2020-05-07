package com.example.spokenwapp.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Singleton;

@Singleton
@Retention(value= RetentionPolicy.RUNTIME)
public @interface LocalVideoScope {
}
