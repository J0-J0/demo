package com.jojo.util;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    public static final String XLS = "xls";

    public static final String XLSX = "xlsx";

    private static final String DEFAULT_DIRECTORY = "C:\\Users\\72669\\Desktop\\";

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
                        String title = "";
                        String boxNo = object.get(13);

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

    /**
     * @param filepath
     */
    public static void readByPoI(String filepath) throws IOException {
        if (StringUtils.isBlank(filepath)) {
            return;
        }

        Workbook workbook = getWorkBook(filepath);

        String targetFileName = DEFAULT_DIRECTORY + "Excel-"
                + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".sql";
        File targetFile = new File(targetFileName);
        targetFile.createNewFile();
        CharSink charSink = Files.asCharSink(targetFile, Charsets.UTF_8, FileWriteMode.APPEND);

        Sheet sheet = workbook.getSheetAt(0);
        List<StringBuffer> lines = Lists.newArrayList();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            Cell barcodeCell = row.getCell(6);
            barcodeCell.setCellType(CellType.STRING);
            String barcode = barcodeCell.getStringCellValue();

            Cell boxNOCell = row.getCell(13);
            boxNOCell.setCellType(CellType.STRING);
            String boxNO = boxNOCell.getStringCellValue();
            StringBuffer buffer = new StringBuffer();
            buffer.append("insert into allot_quick_pool (id, barcode, box_no, cirtype) values(")
                    .append("'").append(UUIDUtil.getRandomUUID()).append("', ")
                    .append("'").append(barcode).append("', ")
                    .append("'").append(boxNO).append("', ")
                    .append("'").append("009").append("'")
                    .append(");");
            lines.add(buffer);
        }
        charSink.writeLines(lines);
    }


    /**
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    private static Workbook getWorkBook(String filepath) throws IOException {
        String suffixName = FileUtil.getSuffixName(filepath);
        Workbook workbook = null;
        InputStream inputStream = IOUtils.toBufferedInputStream(new FileInputStream(new File(filepath)));
        if (StringUtils.equals(suffixName, XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (StringUtils.equals(suffixName, XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Users\\72669\\Desktop\\wms_holding(4).xlsx";
//        readByEasyExcel(filePath);
        readByPoI(filePath);
    }

}
