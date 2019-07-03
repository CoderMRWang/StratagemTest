package com.example.timing;

import com.example.core.annotation.RegeisterInterface;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;



/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public class TimeingResolver implements BeanPostProcessor , RegeisterInterface {



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
     public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public static void main(String[] args) {
        TimeingResolver timeingResolver=new TimeingResolver();

    }


    @Override
    public void regeister(Class<?> modelClass) {

    }
}
