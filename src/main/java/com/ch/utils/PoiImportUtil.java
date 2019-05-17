package com.ch.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * excel解析
 * @author chenHuang
 */
@Component
public class PoiImportUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(PoiImportUtil.class);

	/**
	 * 去除空行数据
	 * @param dates 待处理的数据
	 */
	public void trimCell(List<List> dates){
		for (List date:dates) {
			Iterator iterator = date.iterator();
			while (iterator.hasNext()) {
				if (StringUtils.isEmpty(iterator.next())) {
					iterator.remove();
				}
			}
		}
	}
	/**
	 * 创建工作簿
	 * @param path 文件路径
	 */
	public Workbook readExcelToObj(String path) {
		Workbook wb = null;
		try {
			if (path.contains(".xlsx")) {
				wb = new XSSFWorkbook(new FileInputStream(new File(path)));
			} else if (path.contains(".xls")) {
				wb = WorkbookFactory.create(new File(path));
			}
		} catch (IOException | InvalidFormatException e) {
			LOGGER.error(e+"解析失败");
		}
		return wb;
	}

	/**
	 * 读取excel文件
	 */
	public List<List> readExcel(Workbook wb, int sheetIndex, int startRow, int endRow) {
			Sheet sheet = wb.getSheetAt(sheetIndex);
			if(sheet == null) {
				return null;
			}
			Row row = null;
			startRow = startRow == -1 ? 0:startRow;
			endRow = endRow == -1 ? sheet.getLastRowNum():endRow;
			List<List> datas = new ArrayList<>();
			for (int i = startRow;i <= endRow;i++) {
				List data = new ArrayList();
				row = sheet.getRow(i);
				for (Cell cell:row) {
					Map<String,Object> isMerged = isMergedRegion(sheet,i,cell.getColumnIndex());
					if ((boolean)isMerged.get("isMerged")) {
						data.add(isMerged.get("value"));
					}else {
						try {
							data.add(getCellValue(cell));
							int index = cell.getColumnIndex();
						} catch (Exception e) {
							String errorInfo = "第"+(i+1)+"行，"+"第"+cell.getColumnIndex();
							LOGGER.error(errorInfo+e.getMessage());
							System.out.println(errorInfo);
						}
					}
				}
				datas.add(data);
			}
			return datas;
	}

	/**
	 * 判断是否为合并单元格
	 */
	private Map<String,Object> isMergedRegion(Sheet sheet, int row, int column) {
		Map<String,Object> map = new HashMap<>(0);
		int sheetMereCount = sheet.getNumMergedRegions();
		for (int i = 0;i <sheetMereCount;i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					String value = getCellValue(fCell);
					map.put("isMerged",true);
					map.put("value",value);
					return map;
				}
			}
		}
		map.put("isMerged",false);
		return map;
	}

	/**
	 * 获取合并单元格的值
	 */
	private String getMergeRegionValue(Sheet sheet,int row,int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0;i < sheetMergeCount;i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					Row fRow = sheet.getRow(firstRow);
					Cell fCell = fRow.getCell(firstColumn);
					return getCellValue(fCell);
				}
			}
		}
		return null;
	}

	/**
	 * 获取单元格的值
	 * @param cell 单元格
	 * @return 单元格的值
	 */
	private String getCellValue(Cell cell) {
		if (cell == null) {return "";}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}
		if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		}
		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		}
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}

	/**
	 * 将解析数据转成Po对象
	 * @param dates 解析后的数据
	 * @param cls 转换的类型
	 * @return 转化后的数据
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List importExcel(List<List> dates,Class cls) throws IllegalAccessException, InstantiationException {
		List names = dates.get(0);
		for (int i = 0;i < names.size();i++) {
			Field[]fields;
			fields = cls.getDeclaredFields();
			for (Field field:fields) {
				if (field.getAnnotation(ExcelAnno.class).head().equals(names.get(i))) {
					names.set(i,field.getName());
				}
			}
		}
		List ret = new ArrayList<>();
		for (int i = 1;i < dates.size();i++) {
			Object bean = cls.newInstance();
			for (int j = 0;j < dates.get(i).size();j++) {
				try {
					BeanUtils.copyProperty(bean,names.get(j).toString().trim(),dates.get(i).get(j));
				} catch (IllegalAccessException | InvocationTargetException e) {
					LOGGER.error(e+"字段类型转换失败："+names.get(j).toString());
				}
			}
			ret.add(bean);
		}
		return ret;
	}
}
