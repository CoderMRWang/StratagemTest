package com.example.excel.core;

import com.example.excel.core.model.ExcelModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
@Slf4j
public class ExcelDealResolver<T> {
    private List<File> data;
    private Map<String, List<ExcelModel>> registMap=new ConcurrentReferenceHashMap<>();
    private Class convertClass;

    public ExcelDealResolver(List<File> data, Map<String, List<ExcelModel>> registMap, Class convertClass) {
        this.data = data;
        this.registMap = registMap;
        this.convertClass = convertClass;
    }

    public List<File> getData() {
        return data;
    }

    public void setData(List<File> data) {
        this.data = data;
    }

    public Map<String, List<ExcelModel>> getRegistMap() {
        return registMap;
    }

    public void setRegistMap(Map<String, List<ExcelModel>> registMap) {
        this.registMap = registMap;
    }

    public Class getConvertClass() {
        return convertClass;
    }

    public void setConvertClass(Class convertClass) {
        this.convertClass = convertClass;
    }

    public List<T> convert() {
        return convert(this.convertClass);
    }

    private synchronized List<T> convert(Class convertClass) {
        Assert.isTrue(registMap.containsKey(convertClass.getName()), "该实体类未注册!");
        List<T> objects = new ArrayList<>();
        Object object = null;
        List<ExcelModel> excelModels = registMap.get(convertClass.getName());
        for (File url : data) {
            XSSFWorkbook workbook = null;
            XSSFRow row = null;
            int Erow, Ecol;
            try {
                workbook = new XSSFWorkbook(url);
                int sheetNum = workbook.getNumberOfSheets();
                for (int i = 0; i < sheetNum; i++) {
                    Field.setAccessible(convertClass.getDeclaredFields(), true);
                    XSSFSheet sheet = workbook.getSheetAt(i);
                    int rowNum = sheet.getLastRowNum();
                    for (int j = 1; j <= rowNum; j++) {
                        object = convertClass.newInstance();
                        row = sheet.getRow(j);
                        Erow = j;
                        for (ExcelModel excelModel : excelModels) {
                            Assert.isTrue(excelModel.getCol() > 0,
                                    excelModel.getFiedlName() + "col属性填写错误!需要大于0的整数!");
                            int col = excelModel.getCol() - 1;
                            Class type = excelModel.getType();
                            String filedName = excelModel.getFiedlName();
                            String parttern = excelModel.getPattern();
                            XSSFCell xssfCell = row.getCell(col);
                            Ecol = col;
                            Object data = null;
                            if (xssfCell != null) {
                                switch (type.getName()) {
                                    case "java.util.Date":
                                        data = new Date(xssfCell.getDateCellValue().getTime());
                                        break;
                                    case "java.lang.Long":
                                        xssfCell.setCellType(CellType.STRING);
                                        data = Long.parseLong(xssfCell.getRichStringCellValue().getString());
                                        break;
                                    case "java.lang.Integer":
                                        xssfCell.setCellType(CellType.STRING);
                                        data = Integer.parseInt(xssfCell.getRichStringCellValue().getString());
                                        break;
                                    case "java.lang.Double":
                                        xssfCell.setCellType(CellType.STRING);
                                        data = Double.parseDouble(xssfCell.getRichStringCellValue().getString());
                                        break;
                                    default:
                                        xssfCell.setCellType(CellType.STRING);
                                        data = xssfCell.getRichStringCellValue().toString();
                                        break;
                                }
                            }
                            if (data != null) {
                                Field field = convertClass.getDeclaredField(filedName);
                                field.setAccessible(true);
                                field.set(object, data);
                            }
                        }
                        objects.add((T) object);
                    }

                }
            } catch (IOException | InvalidFormatException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                log.error("Excel转化出现异常!");
                e.printStackTrace();
            }
        }
        return objects;

    }
}
