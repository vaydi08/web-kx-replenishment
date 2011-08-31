package com.sol.kx.web.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class PoiUtil {
	private File file;
	private FileInputStream is;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	
	private Iterator<Row> rowIt;
	private Row thisrow;
	private Iterator<Cell> cellIt;
	private Cell thiscell;
	
	private FormulaEvaluator formula;
	
	public PoiUtil(File file) throws IOException {
		this.file = file;
		
		openExcel();
	}
	
	private void openExcel() throws IOException {
		this.is = new FileInputStream(file);
		this.workbook = new HSSFWorkbook(is);
		this.sheet = workbook.getSheetAt(0);
		
		rowIt = sheet.rowIterator();
		
		this.formula = workbook.getCreationHelper().createFormulaEvaluator();
	}
	
	public boolean hasRow() {
		if(rowIt.hasNext()) {
			thisrow = rowIt.next();
			cellIt = thisrow.cellIterator();
			return true;
		} else
			return false;
	}
	
	public boolean hasCell() {
		if(cellIt.hasNext()) {
			thiscell = cellIt.next();
			return true;
		} else
			return false;
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
}
