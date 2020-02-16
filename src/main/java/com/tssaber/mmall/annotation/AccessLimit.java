package com.tssaber.mmall.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:tssaber 这个注解放在需要保护的handler上(防止同一个用户多次点击)
 * @Date: 2020/1/31 21:27
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AccessLimit {
    //多少秒刷新一次
    int seconds() default 1;
    //最大次数
    int maxCount() default 3;
    //是否需要登录
    boolean needLogin() default true;
}
