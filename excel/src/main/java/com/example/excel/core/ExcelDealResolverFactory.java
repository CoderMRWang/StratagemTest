package com.example.excel.core;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
public class ExcelDealResolverFactory<T> {
    private String fileLocalUrl;//扫描包地址
    private ExcelRegisterResolver excelRegisterResolver;
    private Class convertClass;


    public String getFileLocalUrl() {
        return fileLocalUrl;
    }

    public void setFileLocalUrl(String fileLocalUrl) {
        this.fileLocalUrl = fileLocalUrl;
    }

    public Class getConvertClass() {
        return convertClass;
    }

    public void setConvertClass(Class convertClass) {
        this.convertClass = convertClass;
    }

    public ExcelDealResolverFactory() {
        this.excelRegisterResolver = new ExcelRegisterResolver();
    }
    public ExcelDealResolver<T>getExcelDealResolver(){
        List<File> files= FileUtils.getallfile(fileLocalUrl);
       List<File> data=files.stream().filter(file -> (file.getName().endsWith(".xls")||file.getName().endsWith(".xlsx"))).
               collect(Collectors.toList());
        return new ExcelDealResolver<>(data ,ExcelRegisterResolver.registerExcel(),convertClass);
    }

    public ExcelDealResolver<T> getExcelDealResolver(List<File> fileList,Class convertClass){
        return new ExcelDealResolver<>(fileList,ExcelRegisterResolver.registerExcel(),convertClass);
    }


    public static ExcelDealResolverFactory newInstance(){
        return Singleton.singleton;
    }

     private static class Singleton {
      private  static   ExcelDealResolverFactory  singleton = new ExcelDealResolverFactory<>();

    }
}
