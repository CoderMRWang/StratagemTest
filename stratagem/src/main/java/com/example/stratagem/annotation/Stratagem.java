package com.example.stratagem.annotation;

import java.lang.annotation.*;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
@Target(ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Stratagem {
    String name();//策略名称
    String result() default "";//默认返回值
    String resultType() default "String";//默认返回类型
    String value() default "0";//比较值
    String proxyClass() default "";//代理方法名
    boolean model() default false ;//是否是方法处理
}
