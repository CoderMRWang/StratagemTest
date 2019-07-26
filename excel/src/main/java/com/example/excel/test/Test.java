package com.example.excel.test;

import com.example.excel.core.ExcelDealResolverFactory;
import com.example.excel.test.model.TestExcelModel;
import lombok.Data;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */

public class Test {
    public static void main(String[] args) {
        ExcelDealResolverFactory<TestExcelModel> factory=ExcelDealResolverFactory.newInstance();
        factory.setFileLocalUrl("excel/test.xlsx");
        factory.setConvertClass(TestExcelModel.class);
        System.out.println(factory.getExcelDealResolver().convert());
    }

}
