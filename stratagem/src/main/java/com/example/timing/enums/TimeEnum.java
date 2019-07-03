package com.example.timing.enums;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public enum TimeEnum {
    MS("毫秒",1),S("秒",1000);
    private String name;
    long rate;
    TimeEnum(String name,long rate){
        this.name=name;
        this.rate=rate;
    }
}
