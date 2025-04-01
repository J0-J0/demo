package com.jojo.util.db2word;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Db2Excel {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://172.18.1.103:3306/bookmessage_st?useUnicode=true&characterEncoding=utf-8&useSSL=false";
        String username = "mmlib";
        String password = "Jiatu@2020";
        String excelFilePath = "D:\\workspace\\test\\table_structure.xlsx";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Workbook workbook = new XSSFWorkbook()) {

            // 创建一个工作表
            Sheet sheet = workbook.createSheet("All Tables");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"表名", "列名", "数据类型", "是否可为空", "默认值", "注释"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 获取数据库中的所有表名
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            int rowNum = 1;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                // 获取表的列信息
                ResultSet columns = metaData.getColumns(null, null, tableName, null);
                while (columns.next()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(tableName);
                    row.createCell(1).setCellValue(columns.getString("COLUMN_NAME"));
                    row.createCell(2).setCellValue(columns.getString("TYPE_NAME"));
                    row.createCell(3).setCellValue(columns.getString("IS_NULLABLE"));
                    row.createCell(4).setCellValue(columns.getString("COLUMN_DEF"));
                    row.createCell(5).setCellValue(columns.getString("REMARKS"));
                }
                columns.close();
            }
            tables.close();

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 保存 Excel 文件
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
            }
            System.out.println("表结构信息已成功导出到 " + excelFilePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
