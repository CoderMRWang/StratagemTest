package com.example.timing.enums;

import lombok.Data;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public enum TimeEnum {
    MS("毫秒",1000000),S("秒",1000000000),NANOS("微秒",1);
    private String name;
    long rate;
    TimeEnum(String name,long rate){
        this.name=name;
        this.rate=rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }
}
