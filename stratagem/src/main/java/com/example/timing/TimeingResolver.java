package com.example.timing;
import com.example.core.annotation.RegeisterInterface;
import com.example.stratagem.ExcelStratagemResolver;
import com.example.timing.annotation.Timing;
import lombok.Data;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
@Data
public class TimeingResolver implements RegeisterInterface {
    public static ConcurrentMap<Class, Object> methodMap = new ConcurrentHashMap();

    @Override
    public void regeister(Class<?> modelClass) {
        if (!Modifier.isInterface(modelClass.getModifiers())) {
            Enhancer enhancer = new Enhancer();
            for (Method omethod : modelClass.getMethods()) {
                Timing timingmethod = AnnotationUtils.findAnnotation(omethod, Timing.class);
                if (timingmethod != null) {
                    Long rate = timingmethod.type().getRate();
                    String type = timingmethod.type().getName();
                    enhancer.setSuperclass(modelClass);
                    enhancer.setCallback(new MethodInterceptor() {
                        @Override
                        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                            Long start = 0L;
                            if (omethod.getName().equals(method.getName())) {
                                start = System.currentTimeMillis();
                                System.out.println("方法代理执行");
                            }
                            Object object = methodProxy.invokeSuper(o, objects);
                            if (omethod.getName().equals(method.getName())) {
                                Long end = System.currentTimeMillis();
                                System.out.println(end - start);
                                System.out.println("方法代理完成,耗时:" + new BigDecimal((end - start)).setScale(4).divide(new BigDecimal(rate)) + ",单位:" + type);
                            }
                            return object;
                        }
                    });
                }
            }
            try {
                Object proxyClass = enhancer.create();
                methodMap.put(modelClass, proxyClass);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }
}
