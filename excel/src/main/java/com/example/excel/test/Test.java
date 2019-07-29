package com.example.excel.test;

import com.example.excel.core.ExcelDealResolverFactory;
import com.example.excel.test.model.TestExcelModel;

import java.util.List;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */

public class Test {
    public static void main(String[] args) {
        try {
            ExcelDealResolverFactory<TestExcelModel> factory=ExcelDealResolverFactory.newInstance();
            factory.setFileLocalUrl("excel/test.xlsx");
            factory.setConvertClass(TestExcelModel.class);
            List<TestExcelModel> list = factory.getExcelDealResolver().convert();
            factory.getExcelDealResolver().parse("excel/parsetest.xlsx", TestExcelModel.class, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
