package com.jojo.util;

import java.io.*;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

public class ExcelUtil {

    private static final String DEFAULT_DIRECTORY = "D:\\Workspace\\test\\poi\\";

    public static XSSFWorkbook createByList(List<List<String>> dataList) throws Exception {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        // 工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 第二步创建sheet
        XSSFSheet sheet = workbook.createSheet();

        int rowIndex = 0;
        for (List<String> rowData : dataList) {
            XSSFRow row = sheet.createRow((rowIndex++));
            int cellIndex = 0;
            for (String value : rowData) {
                XSSFCell cell = row.createCell((cellIndex++), CellType.STRING);
                cell.setCellValue(value);
            }
        }
        File file = FileUtil.createFileNamedByDateTime(DEFAULT_DIRECTORY, "xlsx");
        workbook.write(new FileOutputStream(file));

        return workbook;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\72669\\Desktop\\wms_accept_order_item.xlsx";
        readByEasyExcel(filePath);
    }

    private static void readByEasyExcel(String filePath) throws FileNotFoundException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
        ExcelReader excelReader = new ExcelReader(inputStream, null,
                new AnalysisEventListener<List<String>>() {

                    @Override
                    public void invoke(List<String> object, AnalysisContext context) {
                        int rowNO = context.getCurrentRowNum();
                        if (rowNO == 0) { // 跳过列名
                            return;
                        }
                        String barcode = object.get(6);
                        String title = object.get(7);
                        String boxNo = object.get(9);

                        StringBuffer buffer = new StringBuffer();
                        buffer.append("insert into allot_quick_pool (id, title, barcode, box_no, cirtype) values(")
                                .append("'").append(UUIDUtil.getRandomUUID()).append("', ")
                                .append("'").append(title).append("', ")
                                .append("'").append(barcode).append("', ")
                                .append("'").append(boxNo).append("', ")
                                .append("'").append("001").append("'")
                                .append(");");
                        System.out.println(buffer.toString());
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // TODO Auto-generated method stub

                    }
                });

        excelReader.read();
    }

}
