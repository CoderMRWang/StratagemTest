package com.example.stratagem;

import com.example.core.annotation.interfaces.RegeisterInterface;
import com.example.stratagem.annotation.Stratagem;
import com.example.timing.annotation.Timing;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
@Data
public class SimpleStratagemResolver implements RegeisterInterface {
    public static ConcurrentMap<String, String> stratagemMap = new ConcurrentHashMap();
    public static ConcurrentMap<String, Method> stratagemMethod = new ConcurrentHashMap();
    private ResourceLoader resourceLoader = new DefaultResourceLoader();


    /**
     * @param clazz Stratagem.proxyClass
     * @param name  Stratagem.name
     * @param type  Stratagem.value
     */
    protected String setKey(String clazz, String name, String type) {
        return "$" + clazz + "$" + name + "$" + type;
    }

    protected Object getKey(String key) {
        String value = stratagemMap.get(key);
        Method method = stratagemMethod.get(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        } else {
            return method;
        }
    }

    @Override
    public void regeister(Class<?> modelClass) {
        Method methods[] = modelClass.getMethods();
        for (Method method : methods) {
            Stratagem stratagem = AnnotationUtils.findAnnotation(method, Stratagem.class);
            if ((stratagem != null)) {
                String clazz = stratagem.proxyClass();
                String name = stratagem.name();
                String type = stratagem.value();
                String value = stratagem.result();
                String key = this.setKey(clazz, name, type);
                if ((!stratagem.model())) {
                    stratagemMap.put(key, value);
                } else {
                    stratagemMethod.put(key, method);
                }

            }

        }
    }
    @Timing
    public int Test(){
        return 1;
    }
}
