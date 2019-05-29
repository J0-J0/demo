package com.jojo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
		InputStream inputStream = new FileInputStream("D:\\Workspace\\jiatu\\新建 XLSX 工作表.xlsx");
		ExcelReader excelReader = new ExcelReader(inputStream, null, new AnalysisEventListener<List<String>>() {

			@Override
			public void invoke(List<String> object, AnalysisContext context) {
				String libcode = object.get(0);
				String readertypecode = object.get(1);
				String cirtype = object.get(2);
				String total = object.get(3);
				String readertypename = object.get(4);
				String cirtypename = object.get(5);
				StringBuffer buffer = new StringBuffer();
				buffer.append("list.add").append("(new ReaderLoanRuleDTO(");
				buffer.append("\"").append(libcode).append("\", ");
				buffer.append("\"").append(readertypecode).append("\", ");
				buffer.append("\"").append(cirtype).append("\", ");
				buffer.append("").append(total).append(", ");
				buffer.append("\"").append(readertypename).append("\", ");
				buffer.append("\"").append(cirtypename).append("\"");
				buffer.append("));");
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
