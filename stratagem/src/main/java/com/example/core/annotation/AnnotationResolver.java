package com.example.core.annotation;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/2
 * @modify By:
 */
public class AnnotationResolver {
    private static final AnnotationResolver instance=new AnnotationResolver();
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private List<String> registerList = new ArrayList<>();

    AnnotationResolver(){
        try {
            this.getAllClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getAllClass() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources = resolver.getResources("classpath:**/*.class");
        Class<?> modelClass = null;
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            modelClass = Class.forName(reader.getClassMetadata().getClassName());
            setRegisterList(modelClass);
        }
        for (Resource r : resources) {
            MetadataReader reader = metaReader.getMetadataReader(r);
            modelClass = Class.forName(reader.getClassMetadata().getClassName());
            getAllAnnotation(modelClass);
        }

    }

    private void setRegisterList(Class<?> modelClass) throws IOException, ClassNotFoundException {
        Class<?>[] interfaces = null;
        if (modelClass.getDeclaringClass() != null) {
            interfaces = modelClass.getDeclaringClass().getInterfaces();
        } else {
            interfaces = modelClass.getInterfaces();
        }

        if (interfaces != null) {
            for (Class clazz : interfaces) {
                if (!Modifier.isInterface(modelClass.getModifiers()) && clazz.getName().equals(RegeisterInterface.class.getName())) {
                    registerList.add(modelClass.getName());
                }
            }
        }
    }

    private void getAllAnnotation(Class<?> modelClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        for (String clazz : registerList) {
            Class regeisterclass = Class.forName(clazz);
            Object instance= regeisterclass.newInstance();
            Method regeister = regeisterclass.getMethod("regeister",Class.class);
            Class invoke=null;
            if (modelClass.getDeclaringClass() != null) {
                invoke = modelClass.getDeclaringClass();
            } else {
                invoke = modelClass;
            }
            regeister.invoke(instance, invoke);
        }
    }
    public static AnnotationResolver getInstance(){
        return instance;
    }


}
