/* =============================================================
 * Created: [2016年3月21日] by Administrator
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */

package com.ch.utils;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lsx
 * @since
 */
public class ExpPoiExcel<T> {

    public void exportExcel(String title, Collection<T> dataset, OutputStream out) throws Exception {
        excelExport(title, null, dataset, out, "yyyy-MM-dd HH:mm:ss");
    }

    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out) throws Exception {
        excelExport(title, headers, dataset, out, "yyyy-MM-dd HH:mm:ss");
    }

    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) throws Exception {
        excelExport(title, headers, dataset, out, pattern);
    }

    public void excelExport(String title,
                            List<String> headers,
                            List<HashMap<String, String>> dataset,
                            OutputStream out,
                            String pattern) throws Exception {

        // 声明一个工作薄
        HSSFWorkbook workBook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = workBook.createSheet(title);

        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);

        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));

        // 生成一个样式
        HSSFCellStyle styleTitle = workBook.createCellStyle();

        // 设置这些样式
        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一个字体
        HSSFFont font = workBook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        // 把字体应用到当前的样式
        styleTitle.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle styleHead = workBook.createCellStyle();
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workBook.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        styleHead.setFont(font2);

        HSSFCellStyle styleBody = workBook.createCellStyle();

        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

        HSSFFont font3 = workBook.createFont();
        font3.setFontHeightInPoints((short) 10);
        styleBody.setFont(font3);// 选择需要用到的字体格式

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (40 * 20));
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(title);
        }

        row = sheet.createRow(1);
        row.setHeight((short) (30 * 20));
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleHead);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        // Iterator<HashMap<String, String>> it = dataset.iterator();
        int index = 2;
        for (Map<String, String> map : dataset) {
            row = sheet.createRow(index);
            row.setHeight((short) (25 * 20));

            Object[] fields = map.keySet().toArray();

            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(styleBody);
                String value = map.get(fields[i]);
                cell.setCellValue(value);
            }
            index++;
        }

        workBook.write(out);
    }


    public void excelExport(String firstTitle,
                            String secondTitle,
                            List<String> headers,
                            List<HashMap<String, String>> dataset,
                            OutputStream out,
                            String pattern) throws Exception {

        // 声明一个工作薄
        HSSFWorkbook workBook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = workBook.createSheet(secondTitle);

        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);

        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));

        // 生成一个样式
        HSSFCellStyle styleTitle = workBook.createCellStyle();

        // 设置这些样式
        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一个字体
        HSSFFont font = workBook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        // 把字体应用到当前的样式
        styleTitle.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle styleHead = workBook.createCellStyle();
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workBook.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        styleHead.setFont(font2);

        HSSFCellStyle styleBody = workBook.createCellStyle();

        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

        HSSFFont font3 = workBook.createFont();
        font3.setFontHeightInPoints((short) 10);
        styleBody.setFont(font3);// 选择需要用到的字体格式

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (40 * 20));
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(firstTitle);
        }

        HSSFRow SecondRow = sheet.createRow(1);
        row.setHeight((short) (40 * 20));
        // row.set
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(secondTitle);
        }

        row = sheet.createRow(2);
        row.setHeight((short) (30 * 20));
        for (short i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleHead);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        // Iterator<HashMap<String, String>> it = dataset.iterator();
        int index = 3;
        for (Map<String, String> map : dataset) {
            row = sheet.createRow(index);
            row.setHeight((short) (25 * 20));

            Object[] fields = map.keySet().toArray();

            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(styleBody);
                String value = map.get(fields[i]);
                cell.setCellValue(value);
            }
            index++;
        }

        workBook.write(out);

    }

    public void excelExport(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) throws Exception {

        // 声明一个工作薄
        HSSFWorkbook workBook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = workBook.createSheet(title);

        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);

        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        // 生成一个样式
        HSSFCellStyle styleTitle = workBook.createCellStyle();

        // 设置这些样式
        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一个字体
        HSSFFont font = workBook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        // 把字体应用到当前的样式
        styleTitle.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle styleHead = workBook.createCellStyle();
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workBook.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        styleHead.setFont(font2);

        HSSFCellStyle styleBody = workBook.createCellStyle();

        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

        HSSFFont font3 = workBook.createFont();
        font3.setFontHeightInPoints((short) 10);
        styleBody.setFont(font3);// 选择需要用到的字体格式

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (40 * 20));
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(title);
        }

        row = sheet.createRow(1);
        row.setHeight((short) (30 * 20));
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleHead);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            row.setHeight((short) (25 * 20));
            T t = (T) it.next();

            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();

            // 从第二个字段开始过滤掉serialVersionUID字段
            for (short i = 1; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i - 1);
                cell.setCellStyle(styleBody);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});

                if (value != null) {
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            cell.setCellValue(textValue);
                        }
                    }
                }

            }
        }

        workBook.write(out);

    }

    public void excelExport(String title, String[] headers, String[] fieldNames, Collection<T> dataset, HttpServletResponse response, String pattern) throws Exception {

        // 声明一个工作薄
        HSSFWorkbook workBook = new HSSFWorkbook();

        // 生成一个表格
        HSSFSheet sheet = workBook.createSheet();

        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);

        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        // 生成一个样式
        HSSFCellStyle styleTitle = workBook.createCellStyle();

        // 设置这些样式
        styleTitle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一个字体
        HSSFFont font = workBook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 14);// 设置字体大小
        // 把字体应用到当前的样式
        styleTitle.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle styleHead = workBook.createCellStyle();
        styleHead.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleHead.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workBook.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        styleHead.setFont(font2);

        HSSFCellStyle styleBody = workBook.createCellStyle();

        styleBody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderRight(HSSFCellStyle.BORDER_THIN);
        styleBody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        styleBody.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleBody.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        styleBody.setWrapText(true);    //自动换行

        HSSFFont font3 = workBook.createFont();
        font3.setFontHeightInPoints((short) 10);
        styleBody.setFont(font3);// 选择需要用到的字体格式

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (40 * 20));
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleTitle);
            cell.setCellValue(title);
        }

        row = sheet.createRow(1);
        row.setHeight((short) (30 * 20));
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(styleHead);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 1;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            row.setHeight((short) (25 * 20));
            T t = (T) it.next();

            // 从第二个字段开始过滤掉serialVersionUID字段
            for (short i = 0; i < fieldNames.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(styleBody);
                String fieldName = fieldNames[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});

                if (value != null) {
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            cell.setCellValue(textValue);
                        }
                    }
                }

            }
        }

        workBook.write(response.getOutputStream());

    }


    /**
     * 多行表头
     * dataList：导出的数据；sheetName：表头名称； head0：表头第一行列名；headnum0：第一行合并单元格的参数
     * head1：表头第二行列名；headnum1：第二行合并单元格的参数；detail：导出的表体字段
     */
    public void reportMergeXls(Collection<T> dataList,
                               String sheetName, String[] head0, String[] headnum0,
                               String[] head1, String[] headnum1, String[] detail, String pattern, HttpServletResponse response)
            throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);// 创建一个表
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 表头标题样式
        HSSFFont headfont = workbook.createFont();
        headfont.setFontName("黑体");
        headfont.setFontHeightInPoints((short) 14);// 字体大小
        HSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        headstyle.setLocked(true);
        // 表头时间样式
        HSSFFont datefont = workbook.createFont();
        datefont.setFontName("黑体");
        datefont.setFontHeightInPoints((short) 12);// 字体大小
        HSSFCellStyle datestyle = workbook.createCellStyle();
        datestyle.setFont(datefont);
        datestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        datestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        datestyle.setLocked(true);
        // 列名样式
        HSSFFont font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 12);// 字体大小
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        style.setLocked(true);
        // 普通单元格样式（中文）
        HSSFFont font2 = workbook.createFont();
        font2.setFontName("黑体");
        font2.setFontHeightInPoints((short) 12);
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        style2.setFont(font2);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        style2.setWrapText(true); // 换行
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        // 设置列宽  （第几列，宽度）
/*        sheet.setColumnWidth(0, 1600);
        sheet.setColumnWidth(1, 3600);
        sheet.setColumnWidth(2, 2800);
        sheet.setColumnWidth(3, 2800);
        sheet.setColumnWidth(4, 2800);
        sheet.setColumnWidth(5, 2800);
        sheet.setColumnWidth(6, 4500);
        sheet.setColumnWidth(7, 3600);
        sheet.setDefaultRowHeight((short) 360);//设置行高*/
        // 第一行表头标题
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head0.length - 1));
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 0x349);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(headstyle);
        cell.setCellValue(sheetName);
        // 第二行表头列名
        row = sheet.createRow(1);
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(head0[i]);
            cell.setCellStyle(style);
        }
        //动态合并单元格
        for (int i = 0; i < headnum0.length; i++) {
            String[] temp = headnum0[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
        }
        //解析合并字段的其实位置
        int beginPos1 = 0;
        int endPos1 = 0;
        int beginPos2 = 0;
        int endPos2 = 0;
        if (headnum1.length > 4) {
            beginPos1 = Integer.parseInt(headnum1[0].split(",")[2]);
            endPos1 = Integer.parseInt(headnum1[3].split(",")[2]);
            beginPos2 = Integer.parseInt(headnum1[4].split(",")[2]);
            endPos2 = Integer.parseInt(headnum1[headnum1.length - 1].split(",")[2]);
        } else if (headnum1.length > 0) {
            beginPos1 = Integer.parseInt(headnum1[0].split(",")[2]);
            endPos1 = Integer.parseInt(headnum1[3].split(",")[2]);
        }
        //设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
        row = sheet.createRow(2);//因为下标从0开始，所以这里表示的是excel中的第三行
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);//设置excel中第四行的1、2、7、8列的边框
            if (i >= beginPos1 && i <= endPos1 && beginPos1 != endPos1) {
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j + beginPos1);
                    cell.setCellValue(head1[j]);//给excel中第四行的3、4、5、6列赋值（"温度℃", "湿度%", "温度℃", "湿度%"）
                    cell.setCellStyle(style);//设置excel中第四行的3、4、5、6列的边框
                }
            }
            if (i >= beginPos2 && i <= endPos2 && beginPos2 != endPos2) {
                for (int j = 0; j < 4; j++) {
                    cell = row.createCell(j + beginPos2);
                    cell.setCellValue(head1[j]);//给excel中第四行的3、4、5、6列赋值（"温度℃", "湿度%", "温度℃", "湿度%"）
                    cell.setCellStyle(style);//设置excel中第四行的3、4、5、6列的边框
                }
            }
        }
        //动态合并单元格
        for (int i = 0; i < headnum1.length; i++) {
            String[] temp = headnum1[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                    startcol, overcol));
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataList.iterator();
        int index = 2;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            row.setHeight((short) (25 * 20));
            T t = (T) it.next();

            // 从第二个字段开始过滤掉serialVersionUID字段
            for (short i = 0; i < detail.length; i++) {
                HSSFCell cellBody = row.createCell(i);
                cellBody.setCellStyle(style2);
                String fieldName = detail[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(t, new Object[]{});

                if (value != null) {
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cellBody.setCellValue(Double.parseDouble(textValue));
                        } else {
                            cellBody.setCellValue(textValue);
                        }
                    }
                }

            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] b = new byte[1024];
        while ((bais.read(b)) > 0) {
            response.getOutputStream().write(b);
        }
        bais.close();
        response.getOutputStream().flush();
    }

    /**
     * 工作簿的数据结构
     */
    public static class ExcelSheet {

        /**
         * 表格选项卡标题
         */
        public String title;


        /**
         * 表格数据(主表查询用的是这个字段信息)
         */
        public List<Map<String, String>> mapDataList;

    }
}


