package com.scy.core.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: SCY
 * @date: 2020/11/21   15:19
 * @version: 1.0
 * @desc: 对baseUrl的注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseUrl {

}
