package com.example.excel.core;

import com.example.excel.core.annotation.ExcelConvert;
import com.example.excel.core.model.ExcelModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
@Slf4j
public class ExcelRegisterResolver<T> {

     static synchronized Map<String,List<ExcelModel>> registerExcel(){
        Map<String,List<ExcelModel>> map=new HashMap<>();
        PathMatchingResourcePatternResolver patternResolver=new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory=new CachingMetadataReaderFactory();
        Resource[] resources=null;
        try {
            resources=patternResolver.getResources("classpath:**/*.class");
            for(Resource resource:resources){
                MetadataReader metadataReader=metadataReaderFactory.getMetadataReader(resource);
                Class clazz=Class.forName(metadataReader.getClassMetadata().getClassName());
                Field[] fields=clazz.getDeclaredFields();
                Field.setAccessible(fields,true);
                List<ExcelModel> excelModels=new ArrayList<>();
                for(Field field:fields){
                    if (field.isAnnotationPresent(ExcelConvert.class)){
                        ExcelConvert excelConvert=field.getAnnotation(ExcelConvert.class);
                        ExcelModel excelModel=new ExcelModel();
                        excelModel.setType(excelConvert.type());
                        excelModel.setCol(excelConvert.col());
                        excelModel.setFiedlName(field.getName());
                        Assert.isTrue(field.getType().equals(excelConvert.type()),
                                "类:"+clazz.getName()+"属性:"+field.getName()+"强类型不匹配!");
                        excelModels.add(excelModel);
                        map.put(clazz.getName(),excelModels);
                    }
                }

            }
        } catch (IOException|ClassNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }


}
