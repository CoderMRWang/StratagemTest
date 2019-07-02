package com.example.stratagem;

import com.kayak.stratagem.annotation.Stratagem;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
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
public class SimpleStratagemResolver {
    protected static ConcurrentMap<String, String> stratagemMap = new ConcurrentHashMap();
    protected static ConcurrentMap<String, Method> stratagemMethod = new ConcurrentHashMap();
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public SimpleStratagemResolver() throws IOException, ClassNotFoundException {
        this.registerStratagem();
    }

    public static void main(String[] args) {
        try {
            SimpleStratagemResolver stratagemResolver = new SimpleStratagemResolver();
            stratagemResolver.registerStratagem();
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
        }
    }

    private void registerStratagem() throws IOException, ClassNotFoundException {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:**/*.class");
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            Class<?> modelClass = Class.forName(reader.getClassMetadata().getClassName());
            for (Method method : modelClass.getMethods()) {
                Stratagem stratagem = AnnotationUtils.findAnnotation(method, Stratagem.class);
                if ((stratagem != null) ) {
                    String clazz = stratagem.proxyClass();
                    String name = stratagem.name();
                    String type = stratagem.value();
                    String value = stratagem.result();
                    String key=this.setKey(clazz, name, type);
                     if ((!stratagem.model()))
                     {
                         stratagemMap.put(key, value);
                     }else{
                         stratagemMethod.put(key, method);
                     }

                }

            }

        }
        System.out.println(stratagemMap);
    }

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


}
