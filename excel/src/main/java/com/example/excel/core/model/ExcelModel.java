package com.example.excel.core.model;

import lombok.Data;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
@Data
public class ExcelModel {
    private String title;
    private int col;
    private Class type;
    private String fiedlName;
    private String pattern;
}
