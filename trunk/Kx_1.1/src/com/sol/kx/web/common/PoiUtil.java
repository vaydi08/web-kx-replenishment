package com.sol.kx.web.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PoiUtil {
	private File file;
	private FileInputStream is;
	private Workbook workbook;
	private Sheet sheet;
	
	private Iterator<Row> rowIt;
	private Row thisrow;
	private Iterator<Cell> cellIt;
	private Cell thiscell;
	
	private int rowNo;
	private int cellNo;
	
	private FormulaEvaluator formula;
	
	public PoiUtil(File file) throws IOException {
		this.file = file;

		openExcel();
	}
	
	private void openExcel() throws IOException {
		this.is = new FileInputStream(file);
		try {
			this.workbook = new XSSFWorkbook(is);
		} catch (Exception e) {
			this.is = new FileInputStream(file);
			this.workbook = new HSSFWorkbook(is);
		}
		this.sheet = workbook.getSheetAt(0);
		
		rowIt = sheet.rowIterator();
		
		this.formula = workbook.getCreationHelper().createFormulaEvaluator();
		
		this.rowNo = -1;
	}
	
	public PoiUtil(String newsheet) {
		this.workbook = new HSSFWorkbook();
		newSheet(newsheet);
	}
	
	public PoiUtil() {
		this.workbook = new HSSFWorkbook();
	}
	
	public void newSheet(String newsheet) {
		this.sheet = workbook.createSheet(newsheet);
		this.rowNo = -1;
	}
	
	public boolean hasRow() {
		if(rowIt.hasNext()) {
			thisrow = rowIt.next();
			cellIt = thisrow.cellIterator();
			
			++ this.rowNo;
			this.cellNo = -1;
			return true;
		} else
			return false;
	}
	
	public boolean hasCell() {
		if(cellIt.hasNext()) {
			thiscell = cellIt.next();
			
			++ this.cellNo;
			return true;
		} else
			return false;
	}
	
	public void mergeCells(int colfrom,int rowfrom,int colto,int rowto) {
		CellRangeAddress r = new CellRangeAddress(rowfrom, rowto, colfrom, colto);
		sheet.addMergedRegion(r);
	}
	
	public void autoSize(int col) {
//		sheet.autoSizeColumn(col);
		sheet.setColumnWidth(col, 3000);
	}
	
	public void newRow() {
		thisrow = sheet.createRow(++ rowNo);
	}
	
	public void gotoRow(int row) {
		thisrow = sheet.getRow(rowNo = row);
	}
	
	public void setValue(int col,String value) {
		if(value != null) {
			Cell cell = thisrow.createCell(col);
			cell.setCellValue(value);
		}
	}
	
	public void setValue(int col,double value) {
		Cell cell = thisrow.createCell(col);
		cell.setCellValue(value);
	}
	
	public void setValue(int col,int value) {
		Cell cell = thisrow.createCell(col);
		cell.setCellValue(value);
	}
	
	public Object getValue(int col,Object defaultvalue) {
		Cell cell = thisrow.getCell(col);
		if(cell == null)
			return defaultvalue;
		
		Object ret;
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			ret = defaultvalue;
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			ret = getFormulaValue(formula.evaluate(cell));
			break;
		case Cell.CELL_TYPE_NUMERIC:
			ret = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		default:
			ret = cell.getStringCellValue();
		}
		
		return ret == null ? defaultvalue : ret;
	}
	
	public Object getValue() {
		switch (thiscell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return thiscell.getBooleanCellValue();
		case Cell.CELL_TYPE_ERROR:
			return thiscell.getErrorCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return getFormulaValue(formula.evaluate(thiscell));
		case Cell.CELL_TYPE_NUMERIC:
			return thiscell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:
			return thiscell.getStringCellValue();
		default:
			return thiscell.getStringCellValue();
		}
	}
	
	private Object getFormulaValue(CellValue cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanValue();
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumberValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringValue();
		default:
			return cell.getStringValue();
		}
	}
	
	public InputStream getExcel() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] ba = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        return bais;
	}
	
	public void close() {
		if(is != null)
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) {
		PoiUtil p = null;
		try {
			p = new PoiUtil(new File("F:/Book1.xls"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			p.close();
		}
	}

	public int getRowNo() {
		return rowNo;
	}

	public int getCellNo() {
		return cellNo;
	}
}
