package com.ssr.base.util.composite.jexcel.excelmaker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ssr.base.util.CommonUtils;

/**
 * 将html table 转成 excel
 * 
 * 记录下来所占的行和列，然后填充合并
 */
public class ConvertHtml2Excel {
    public static void main(String[] args) {
    	//测试html
    	StringBuilder c = new StringBuilder("<table class=\"table table-bordered\">");
    	c.append("<tbody align=\"center\">");
    	c.append("<tr><td colspan=\"2\" rowspan=\"2\"></td><td colspan=\"2\" rowspan=\"1\">A</td><td colspan=\"1\" rowspan=\"2\">B</td></tr>");
    	c.append("<tr><td colspan=\"1\" rowspan=\"1\">A1</td><td colspan=\"1\" rowspan=\"1\">A2</td></tr>");
    	c.append("<tr align=\"center\" valign=\"value\"><td align=\"center\" valign=\"middle\" colspan=\"2\" rowspan=\"1\">r</td><td>1</td><td>2</td><td>3</td></tr>");
    	c.append("<tr align=\"center\" valign=\"value\"><td align=\"center\" valign=\"middle\" colspan=\"2\" rowspan=\"1\">s</td><td>4</td><td>5</td><td>6</td></tr>");
    	c.append("</tbody></table>");
    	
        HSSFWorkbook wb = table2Excel(c.toString(), "测试标签");
        try {
        	//如果需要作http下载,直接获取servlet out流写入wb.write(out);
            FileOutputStream fos = new FileOutputStream(new File("F:\\1.xls"));
            wb.write(fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * html表格转excel
     * 
     * @param tableHtml 如
     *            <table>
     *            ..
     *            </table>
     * @param sheetName sheet名称
     * 
     * @return
     */
    @SuppressWarnings({ "deprecation" })
	public static HSSFWorkbook table2Excel(String tableHtml, String sheetName) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<CrossRangeCellMeta>();
        int rowIndex = 0;
        try {
            Document data = DocumentHelper.parseText(tableHtml);
            // 生成表头
            Element thead = data.getRootElement().element("thead");
            HSSFCellStyle titleStyle = getTitleStyle(wb);
            if (thead != null) {
                List<Element> trLs = thead.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("th");
                    makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
            }
            // 生成表体
            Element tbody = data.getRootElement().element("tbody");
            if (tbody != null) {
                HSSFCellStyle contentStyle = getContentStyle(wb);
                List<Element> trLs = tbody.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("th");
                    int cellIndex = makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    List<Element> tdLs = trEle.elements("td");
                    makeRowCell(tdLs, rowIndex, row, cellIndex, contentStyle, crossRowEleMetaLs);
                    rowIndex++;
                }
            }
            // 合并表头
            for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
            	CellRangeAddress cellRangeAddress = new CellRangeAddress(crcm.getFirstRow(), crcm.getLastRow(), crcm.getFirstCol(), crcm.getLastCol());
                sheet.addMergedRegion(cellRangeAddress);
                //设置合并单元格样式
                RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return wb;
    }

    /**
     * 生产行内容
     * 
     * @return 最后一列的cell index
     */
    /**
     * @param tdLs th或者td集合
     * @param rowIndex 行号
     * @param row POI行对象
     * @param startCellIndex
     * @param cellStyle 样式
     * @param crossRowEleMetaLs 跨行元数据集合
     * @return
     */
    @SuppressWarnings("deprecation")
	private static int makeRowCell(List<Element> tdLs, int rowIndex, HSSFRow row, int startCellIndex, HSSFCellStyle cellStyle,
            List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int i = startCellIndex;
        for (int eleIndex = 0; eleIndex < tdLs.size(); i++, eleIndex++) {
            int captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            while (captureCellSize > 0) {
                for (int j = 0; j < captureCellSize; j++) {// 当前行跨列处理（补单元格）
                    row.createCell(i);
                    i++;
                }
                captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            }
            Element thEle = tdLs.get(eleIndex);
            String val = thEle.getTextTrim();
            if (CommonUtils.isBlank(val)) {
                Element e = thEle.element("a");
                if (e != null) {
                    val = e.getTextTrim();
                }
            }
            HSSFCell c = row.createCell(i);
            if (CommonUtils.isNumber(val)) {
                c.setCellValue(Double.parseDouble(val));
                c.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            } else {
                c.setCellValue(val);
            }
            
            String align = thEle.attributeValue("align");
            if("center".equals(align)){
            	cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            }
            else if("right".equals(align)){
            	cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            }
            else if("left".equals(align)){
            	cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            }
            else{
            	cellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
            }
            
            c.setCellStyle(cellStyle);
            int rowSpan = CommonUtils.toInt(thEle.attributeValue("rowspan"), 1);
            int colSpan = CommonUtils.toInt(thEle.attributeValue("colspan"), 1);
            if (rowSpan > 1 || colSpan > 1) { // 存在跨行或跨列
                crossRowEleMetaLs.add(new CrossRangeCellMeta(rowIndex, i, rowSpan, colSpan));
            }
            if (colSpan > 1) {// 当前行跨列处理（补单元格）
                for (int j = 1; j < colSpan; j++) {
                    i++;
                    row.createCell(i);
                }
            }
        }
        return i;
    }

    /**
     * 获得因rowSpan占据的单元格
     * 
     * @param rowIndex 行号
     * @param colIndex 列号
     * @param crossRowEleMetaLs 跨行列元数据
     * @return 当前行在某列需要占据单元格
     */
    private static int getCaptureCellSize(int rowIndex, int colIndex, List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int captureCellSize = 0;
        for (CrossRangeCellMeta crossRangeCellMeta : crossRowEleMetaLs) {
            if (crossRangeCellMeta.getFirstRow() < rowIndex && crossRangeCellMeta.getLastRow() >= rowIndex) {
                if (crossRangeCellMeta.getFirstCol() <= colIndex && crossRangeCellMeta.getLastCol() >= colIndex) {
                    captureCellSize = crossRangeCellMeta.getLastCol() - colIndex + 1;
                }
            }
        }
        return captureCellSize;
    }

    /**
     * 获得标题样式
     * 
     * @param workbook
     * @return
     */
    @SuppressWarnings("deprecation")
	private static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        short titlebackgroundcolor = HSSFColor.GREY_25_PERCENT.index;
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(titlebackgroundcolor);// 背景色

        HSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    /**
     * 获得内容样式
     * 
     * @param wb
     * @return
     */
    @SuppressWarnings("deprecation")
	private static HSSFCellStyle getContentStyle(HSSFWorkbook wb) {
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = wb.createCellStyle();
        
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        
        HSSFFont font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        return style;
    }
}
