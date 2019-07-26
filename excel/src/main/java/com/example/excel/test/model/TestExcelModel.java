package com.example.excel.test.model;

import com.example.excel.core.annotation.ExcelConvert;
import lombok.Data;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
@Data
public class TestExcelModel {
    @ExcelConvert(name="id",col = 1,type = Long.class)
    private Long id;
    @ExcelConvert(name = "用户名",col = 2)
    private String Name;
    @ExcelConvert(name = "密码",col = 3)
    private String password;
}
