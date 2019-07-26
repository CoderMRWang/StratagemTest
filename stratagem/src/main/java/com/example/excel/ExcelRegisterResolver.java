package com.example.excel;

import com.example.core.annotation.interfaces.RegeisterInterface;

import java.lang.reflect.Field;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/23
 * @modify By:
 */
public class ExcelRegisterResolver implements RegeisterInterface {
    @Override
    public void regeister(Class<?> modelClass) {
        Field.setAccessible(modelClass.getDeclaredFields(),true);
        


    }
}
