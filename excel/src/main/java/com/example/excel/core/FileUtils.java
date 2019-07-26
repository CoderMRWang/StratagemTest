package com.example.excel.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : wanghaotian
 * @despriction :
 * @date : Created in 2019/7/26
 * @modify By:
 */
public class FileUtils {

    public static List<File> getallfile(String path) {
        List<File> allfilelist = new ArrayList<File>();
        return getallfile(new File(path), allfilelist);
    }

    private static List<File> getallfile(File file, List<File> allfilelist) {
        if (file.exists()) {
            //判断文件是否是文件夹，如果是，开始递归
            if (file.isDirectory()) {
                File f[] = file.listFiles();
                for (File file2 : f) {
                    getallfile(file2, allfilelist);
                }
            } else {
                allfilelist.add(file);
            }
        }
        return allfilelist;

    }
}