package com.yt.app.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yt.app.common.enums.YtDataSourceEnum;

/**
 * 
 * @author zj
 * 
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface YtDataSourceAnnotation {
	YtDataSourceEnum datasource();
}
