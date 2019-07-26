package com.example.excel.annotation;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/23
 * @modify By:
 */
public @interface ExcelConvert {
    String name();//对应列名
    Class type() default  String.class;
    String pattern() default "";
    int col()default -1;
}
