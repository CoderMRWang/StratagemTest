package com.example.excel.core;

import com.example.excel.core.model.ExcelModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public <T> void parse(String path, Class<T> convertClass, List<T> data) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            List<ExcelModel> excelModels = registMap.get(convertClass.getName());
            Assert.notEmpty(excelModels, "该类型没有注册!");
            this.setExcelTitle(workbook, excelModels);
            XSSFSheet newSheet = workbook.getSheetAt(0);
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < excelModels.size(); j++) {
                    Class type = excelModels.get(j).getType();
                    CellType value;
                    String fieldName = excelModels.get(j).getFiedlName();
                    int colIndex = excelModels.get(j).getCol();
                    XSSFRow xssfRow;
                    if (j == 0) {
                        xssfRow = newSheet.createRow(i + 1);
                    } else {
                        xssfRow = newSheet.getRow(i + 1);
                    }
                    XSSFCell xssfCell = xssfRow.createCell(colIndex - 1);
                    switch (type.getName()) {
                        case "java.utils.Date":
                            value = CellType.NUMERIC;
                            CreationHelper creationHelper = workbook.getCreationHelper();
                            XSSFCellStyle cellStyle = workbook.createCellStyle();
                            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
                            xssfCell.setCellStyle(cellStyle);
                            break;
                        case "java.lang.Long":
                            value = CellType.NUMERIC;
                            break;
                        case "java.lang.Integer":
                            value = CellType.NUMERIC;
                            break;
                        case "java.lang.Double":
                            value = CellType.NUMERIC;
                            break;
                        case "java.lang.Boolean":
                            value = CellType.BOOLEAN;
                            break;
                        default:
                            value = CellType.STRING;
                            break;
                    }
                    xssfCell.setCellType(value);
                    T refrence = data.get(i);
                    Field field = refrence.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    String switchKey=type.getName();
                    switch (switchKey){
                        case "java.util.Date":
                            xssfCell.setCellValue((Date)field.get(refrence));
                            break;
                        case "java.lang.Integer":
                            xssfCell.setCellValue((Integer)field.get(refrence));
                            break;
                        case "java.lang.Long":
                            xssfCell.setCellValue((Long)field.get(refrence));
                            break;
                        case "java.lang.Boolean":
                            xssfCell.setCellValue((Boolean)field.get(refrence));
                            break;
                        default:
                            xssfCell.setCellValue((String)field.get(refrence));
                    }
                }
            }
        }  catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
            log.error("写入Excel异常!");
        }
        try{
            FileOutputStream outputStream=new FileOutputStream(path);
            workbook.write(outputStream);
        }catch (IOException e){
            e.printStackTrace();
            log.error("写入Excel异常!");
        }
    }

    private XSSFWorkbook setExcelTitle(XSSFWorkbook workbook, List<ExcelModel> excelModels) {
        XSSFSheet newsheet = workbook.createSheet();
        XSSFRow xssfRow = newsheet.createRow(0);
        for (int i = 0; i < excelModels.size(); i++) {
            XSSFCell xssfCell = xssfRow.createCell(i, CellType.STRING);
            xssfCell.setCellValue(excelModels.get(i).getTitle());
        }
        return workbook;
    }


}
