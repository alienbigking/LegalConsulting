package com.gkzxhn.autoespresso.util;

import com.gkzxhn.autoespresso.entity.MergedRegionEntity;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExcelUtil {
	//默认单元格内容为数字时格式
	private static DecimalFormat df = new DecimalFormat("0");
	// 默认单元格格式化日期字符串
	private static SimpleDateFormat sdf = new SimpleDateFormat(  "yyyy-MM-dd HH:mm:ss");
	// 格式化数字
	private static DecimalFormat nf = new DecimalFormat("0.00");

	public static int getSheetsNumber(File file){
		int size=0;
		try {
			if(file != null) {
				if (file.getName().endsWith("xlsx")) {
					//处理ecxel2007
					XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
					size = wb.getNumberOfSheets();
				} else {
					//处理ecxel2003
					HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
					size = wb.getNumberOfSheets();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}
	public static String getSheetName(File file,int index){
		String  name="";
		try {
			if(file != null) {
				if (file.getName().endsWith("xlsx")) {
					//处理ecxel2007
					XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
					name = wb.getSheetName(index);
				} else {
					//处理ecxel2003
					HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
					name = wb.getSheetName(index);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}
	public static Sheet getSheet(File file,int SheetIndex){
		if(file == null){
			return null;
		}
		try{
			if(file.getName().endsWith("xlsx")){
				ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
				ArrayList<Object> colList;
				XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
				XSSFSheet sheet = wb.getSheetAt(SheetIndex);
				//处理ecxel2007
				return sheet;
			}else{
				ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
				ArrayList<Object> colList;
				HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
				HSSFSheet sheet = wb.getSheetAt(SheetIndex);
				//处理ecxel2003
				return sheet;
			}
		}catch (Exception e){

		}
		return null;
	}



	/**
	 * 判断指定的单元格是否是合并单元格
	 * @param sheet
	 * @param row 行下标
	 * @param column 列下标
	 * @return
	 */
	public static MergedRegionEntity isMergedRegion(Sheet sheet, int row , int column) {
		MergedRegionEntity mergedRegion=null;
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if(row >= firstRow && row <= lastRow){
				if(column >= firstColumn && column <= lastColumn){
					mergedRegion=new MergedRegionEntity();
					mergedRegion.setMergedRegion(true);
					mergedRegion.setFirstColumn(firstColumn);
					mergedRegion.setFirstRow(firstRow);
					mergedRegion.setLastColumn(lastColumn);
					mergedRegion.setLastRow(lastRow);
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					mergedRegion.setValue(getCellValue(fCell));
					break;
				}
			}
		}
		return mergedRegion;
	}
	public static String getCellValue(Sheet sheet,int row,int col){
		Row fRow = sheet.getRow(row);
		Cell fCell = fRow.getCell(col);
		return ExcelUtil.getCellValue(fCell).trim();
	}
	/**
	 * 获取单元格的值
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell){
		String value="";
		if(cell == null) return "";
		switch(cell.getCellType()){
			case XSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				try {
					value = cell.getStringCellValue();
				}catch (Exception e){
//					e.printStackTrace();
					value=df.format(cell.getNumericCellValue());
				}
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				if ("@".equals(cell.getCellStyle().getDataFormatString())) {
					value = df.format(cell.getNumericCellValue());
				} else if ("General".equals(cell.getCellStyle()
						.getDataFormatString())) {
					String valueStr= nf.format(cell.getNumericCellValue());
					if(valueStr.endsWith("00")){
						value =df.format(cell.getNumericCellValue());
					}else{
						value =valueStr;
					}
				} else {
					value = sdf.format(HSSFDateUtil.getJavaDate(cell
							.getNumericCellValue()));
				}
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				value = Boolean.valueOf(cell.getBooleanCellValue()).toString();
				break;
			case XSSFCell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				value = cell.toString();
				break;
		}// end switch

		return value;
	}


	/**
	 * 一个单元格一个单元格读取
	 * @param file
	 * @param SheetIndex
	 * @return
	 */
	public static ArrayList<ArrayList<Object>> readExcel(File file,int SheetIndex){
		if(file == null){
			return null;
		}
		ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> colList;
		try{
			Sheet sheet=null;
			Row row=null;
			Cell cell=null;
			if(file.getName().endsWith("xlsx")){
				//处理ecxel2007
				XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
				sheet = wb.getSheetAt(SheetIndex);

			}else{
				//处理ecxel2003
				HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
				sheet = wb.getSheetAt(SheetIndex);
			}
			for(int i = sheet.getFirstRowNum() , rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows() ; i++ ){
				row = sheet.getRow(i);
				colList = new ArrayList<Object>();
				if(row == null){
					//当读取行为空时
					if(i != sheet.getPhysicalNumberOfRows()){//判断是否是最后一行
						rowList.add(colList);
					}
					continue;
				}else{
					rowCount++;
				}
				for( int j = row.getFirstCellNum() ; j <= row.getLastCellNum() ;j++){
					cell = row.getCell(j);
					if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
						//当该单元格为空
						if(j != row.getLastCellNum()){//判断是否是该行中最后一个单元格
							colList.add("");
						}
						continue;
					}
					colList.add(getCellValue(cell));
				}//end for j
				rowList.add(colList);
			}//end for i

		}catch(Exception e){
			return null;
		}
		return rowList;
	}

	public static void writeExcel(ArrayList<ArrayList<Object>> result,String path){
		if(result == null){
			return;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		for(int i = 0 ;i < result.size() ; i++){
			HSSFRow row = sheet.createRow(i);
			if(result.get(i) != null){
				for(int j = 0; j < result.get(i).size() ; j ++){
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(result.get(i).get(j).toString());
				}
			}
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try
		{
			wb.write(os);
		} catch (IOException e){
//			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		File file = new File(path);//Excel文件生成后存储的位置。
		OutputStream fos  = null;
		try
		{
			fos = new FileOutputStream(file);
			fos.write(content);
			os.close();
			fos.close();
		}catch (Exception e){
//			e.printStackTrace();
		}
	}
}

	
