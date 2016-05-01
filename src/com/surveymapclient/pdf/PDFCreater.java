package com.surveymapclient.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.surveymapclient.common.FileUtils;
import com.surveymapclient.common.SurveyUtils;
import com.surveymapclient.common.TimeUtils;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;

public class PDFCreater {

	private String PDFfile = "";

	public PDFCreater(String filePath, String fileName) {
		// TODO Auto-generated constructor stub
		File path = new File(filePath);
		try {
			if (!path.exists())
				path.mkdirs();
			if (path.isFile()) {
				path.delete();
				path.mkdirs();
			}
			PDFfile = filePath + fileName;
		} catch (Exception e) {
			// TODO: handle exception

		}
	}

	// 产生PDF字体
	private Font getChineseFont() {
		BaseFont bf = null;
		Font fontChinese = null;
		try {
			bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			fontChinese = new Font(bf, 12, Font.NORMAL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fontChinese;
	}

	private Paragraph setTilie(String info) {
		Paragraph paragraph = new Paragraph(info, getChineseFont());
		paragraph.setAlignment(Paragraph.ALIGN_MIDDLE);
		return paragraph;
	}

	public boolean createPDF(String picPath, List<LineBean> linelist, List<PolygonBean> polylist,
			/*List<RectangleBean> rectlist, List<CoordinateBean> coorlist, List<AngleBean> anglelist,*/
			List<TextBean> textlist, List<AudioBean> audiolist) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(PDFfile));
			// 文档属性
			document.addTitle("Title@sample");
			document.addAuthor("Author@aa");
			document.addSubject("Subject@iText sample");
			document.addKeywords("Keywords@iText");
			document.addCreator("Creator@iText");
			// Step 3—Open the Document.
			document.open();
			// Step 4—Add content.
			Image image = Image.getInstance(picPath);
			// float width = document.getPageSize().getWidth();
			// width = width / 2 - image.getWidth() / 2;
//			image.setAbsolutePosition(0, 0);
//			image.setAlignment(Image.ALIGN_RIGHT);
			image.scalePercent(20);//依照比例缩放
			document.add(image);
			document.newPage();

			// 直线
			Paragraph p1 = new Paragraph();
			p1.add(new Chunk(new LineSeparator()));
			document.add(p1);
			document.add(new Paragraph("数据清单"));
			document.add(setTilie("直线"));
			document.add(createLineTable(linelist));
			document.add(setTilie("多边形"));
			document.add(createPolygonTable(polylist));
			/*document.add(setTilie("矩形"));
			document.add(createRectangleTable(rectlist));
			document.add(setTilie("坐标"));
			document.add(createCoordinateTable(coorlist));
			document.add(setTilie("角度"));
			document.add(createAngleTable(anglelist));*/
			document.add(setTilie("文本注释"));
			document.add(createTextTable(textlist));
			document.add(setTilie("语音注释"));
			document.add(createAudioTable(audiolist));
			document.close();
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private PdfPCell settabCell(String str) {
		// 设置字体
		BaseFont bfChinese;
		PdfPCell cell = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color
			cell = new PdfPCell(new Paragraph(str, fontChinese));//
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cell;

	}

	private PdfPCell setCell(String str) {
		// 设置字体
		BaseFont bfChinese;
		PdfPCell cell = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// 创建字体，设置family，size，style,还可以设置color
			cell = new PdfPCell(new Paragraph(str, fontChinese));//
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cell;

	}

	private PdfPTable createLineTable(List<LineBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "名称", "长度", "角度", "颜色", "宽度", "虚实", "描述" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}

		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
			table.addCell(list.get(i).getLength() + "m");
			table.addCell(list.get(i).getAngle() + "°");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getPaintWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createPolygonTable(List<PolygonBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 1, 1, 1, 9, 1 });
		String[] tttile = { "名称", "面积", "颜色", "直线", "描述" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getPolyName()));
			table.addCell(list.get(i).getPolyArea() + "㎡");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPolyColor())));
			PdfPTable table2 = new PdfPTable(7);
			table2.setWidthPercentage(100);// 设置表格宽度为100%
			table2.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
			String[] tttile1 = { "名称", "长度", "角度", "颜色", "宽度", "虚实", "描述" };
			for (int j = 0; j < tttile1.length; j++) {
				table2.addCell(settabCell(tttile1[j]));
			}
			for (int j = 0; j < list.get(i).getPolyLine().size(); j++) {
				table2.addCell(setCell(list.get(i).getPolyLine().get(j).getName()));
				table2.addCell(list.get(i).getPolyLine().get(j).getLength() + "m");
				table2.addCell(list.get(i).getPolyLine().get(j).getAngle() + "°");
				table2.addCell(setCell(SurveyUtils.getColor(list.get(i).getPolyLine().get(j).getPaintColor())));
				table2.addCell(list.get(i).getPolyLine().get(j).getPaintWidth() + "");
				table2.addCell(setCell(SurveyUtils.getStyle(list.get(i).getPolyLine().get(j).isPaintIsFull())));
				table2.addCell(setCell(list.get(i).getPolyLine().get(j).getDescripte()));
			}
			table.addCell(table2);
			table.addCell(setCell(list.get(i).getDescripe()));
		}
		return table;
	}

	private PdfPTable createRectangleTable(List<RectangleBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(8);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "名称", "面积", "长度", "宽度", "颜色", "线大小", "虚实", "描述" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getRectName()));
			table.addCell(list.get(i).getRectArea() + "m2");
			table.addCell(list.get(i).getRectLenght() + "m");
			table.addCell(list.get(i).getRectWidth() + "m");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getRectWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createCoordinateTable(List<CoordinateBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(9);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "名称", "体积", "长度", "宽度", "高度", "颜色", "线大小", "虚实", "描述" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
			table.addCell(list.get(i).getVolum() + "m3");
//			table.addCell(list.get(i).getLenght() + "m");
//			table.addCell(list.get(i).getWidth() + "m");
//			table.addCell(list.get(i).getHeight() + "m");
//			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
//			table.addCell(list.get(i).getPaintWidth() + "");
//			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createAngleTable(List<AngleBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 3 });
		String[] tttile = { "名称", "角度", "颜色", "宽度", "虚实", "描述" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
			table.addCell(list.get(i).getAngle() + "°");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getPaintWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createTextTable(List<TextBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 1, 9 });
		String[] tttile = { "序号", "内容" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(settabCell(i +1+ ""));
			table.addCell(setCell(list.get(i).getText()));
		}
		return table;
	}

	private PdfPTable createAudioTable(List<AudioBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(3);
		table.setWidthPercentage(100);// 设置表格宽度为100%
		table.setWidths(new int[] { 1, 2, 7 });
		String[] tttile = { "序号", "时长", "路径" };
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(settabCell(i +1+ ""));
			table.addCell(setCell(TimeUtils.convertMilliSecondToMinute2(list.get(i).getLenght())));
			table.addCell(setCell(FileUtils.getM4aFilePath(list.get(i).getUrl())));
		}
		return table;
	}
}
