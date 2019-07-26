package com.example.excel.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelConvert {
    String name();//对应excel列名
    Class type() default String.class;
    String pattern() default "";
    int col() default -1;//第几列
}
