package com.example.stratagem.interfaces;

import java.lang.reflect.InvocationTargetException;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public interface Resolver {
    Object resolve(Object... object) throws InvocationTargetException, IllegalAccessException;
}
