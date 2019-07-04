package com.example.timing.annotation;

import com.example.timing.enums.TimeEnum;

import java.lang.annotation.*;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timing {
    TimeEnum type() default  TimeEnum.MS;
}
