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

<<<<<<< HEAD
	// äº§ç”ŸPDFå­—ä½“
=======
	// ²úÉúPDF×ÖÌå
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
			List<RectangleBean> rectlist, List<CoordinateBean> coorlist, List<AngleBean> anglelist,
			List<TextBean> textlist, List<AudioBean> audiolist) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(PDFfile));
<<<<<<< HEAD
			// æ–‡æ¡£å±žæ€§
=======
			// ÎÄµµÊôÐÔ
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			document.addTitle("Title@sample");
			document.addAuthor("Author@aa");
			document.addSubject("Subject@iText sample");
			document.addKeywords("Keywords@iText");
			document.addCreator("Creator@iText");
<<<<<<< HEAD
			// Step 3â€”Open the Document.
			document.open();
			// Step 4â€”Add content.
=======
			// Step 3¡ªOpen the Document.
			document.open();
			// Step 4¡ªAdd content.
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			Image image = Image.getInstance(picPath);
			// float width = document.getPageSize().getWidth();
			// width = width / 2 - image.getWidth() / 2;
//			image.setAbsolutePosition(0, 0);
//			image.setAlignment(Image.ALIGN_RIGHT);
<<<<<<< HEAD
			image.scalePercent(20);//ä¾ç…§æ¯”ä¾‹ç¼©æ”¾
			document.add(image);
			document.newPage();

			// ç›´çº¿
			Paragraph p1 = new Paragraph();
			p1.add(new Chunk(new LineSeparator()));
			document.add(p1);
			document.add(new Paragraph("æ•°æ®æ¸…å•"));
			document.add(setTilie("ç›´çº¿"));
			document.add(createLineTable(linelist));
			document.add(setTilie("å¤šè¾¹å½¢"));
			document.add(createPolygonTable(polylist));
			document.add(setTilie("çŸ©å½¢"));
			document.add(createRectangleTable(rectlist));
			document.add(setTilie("åæ ‡"));
			document.add(createCoordinateTable(coorlist));
			document.add(setTilie("è§’åº¦"));
			document.add(createAngleTable(anglelist));
			document.add(setTilie("æ–‡æœ¬æ³¨é‡Š"));
			document.add(createTextTable(textlist));
			document.add(setTilie("è¯­éŸ³æ³¨é‡Š"));
=======
			image.scalePercent(20);//ÒÀÕÕ±ÈÀýËõ·Å
			document.add(image);
			document.newPage();

			// Ö±Ïß
			Paragraph p1 = new Paragraph();
			p1.add(new Chunk(new LineSeparator()));
			document.add(p1);
			document.add(new Paragraph("Êý¾ÝÇåµ¥"));
			document.add(setTilie("Ö±Ïß"));
			document.add(createLineTable(linelist));
			document.add(setTilie("¶à±ßÐÎ"));
			document.add(createPolygonTable(polylist));
			document.add(setTilie("¾ØÐÎ"));
			document.add(createRectangleTable(rectlist));
			document.add(setTilie("×ø±ê"));
			document.add(createCoordinateTable(coorlist));
			document.add(setTilie("½Ç¶È"));
			document.add(createAngleTable(anglelist));
			document.add(setTilie("ÎÄ±¾×¢ÊÍ"));
			document.add(createTextTable(textlist));
			document.add(setTilie("ÓïÒô×¢ÊÍ"));
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		// è®¾ç½®å­—ä½“
=======
		// ÉèÖÃ×ÖÌå
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		BaseFont bfChinese;
		PdfPCell cell = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
<<<<<<< HEAD
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// åˆ›å»ºå­—ä½“ï¼Œè®¾ç½®familyï¼Œsizeï¼Œstyle,è¿˜å¯ä»¥è®¾ç½®color
=======
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// ´´½¨×ÖÌå£¬ÉèÖÃfamily£¬size£¬style,»¹¿ÉÒÔÉèÖÃcolor
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		// è®¾ç½®å­—ä½“
=======
		// ÉèÖÃ×ÖÌå
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		BaseFont bfChinese;
		PdfPCell cell = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
<<<<<<< HEAD
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// åˆ›å»ºå­—ä½“ï¼Œè®¾ç½®familyï¼Œsizeï¼Œstyle,è¿˜å¯ä»¥è®¾ç½®color
=======
			Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);// ´´½¨×ÖÌå£¬ÉèÖÃfamily£¬size£¬style,»¹¿ÉÒÔÉèÖÃcolor
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "åç§°", "é•¿åº¦", "è§’åº¦", "é¢œè‰²", "å®½åº¦", "è™šå®ž", "æè¿°" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "Ãû³Æ", "³¤¶È", "½Ç¶È", "ÑÕÉ«", "¿í¶È", "ÐéÊµ", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}

		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
			table.addCell(list.get(i).getLength() + "m");
<<<<<<< HEAD
			table.addCell(list.get(i).getAngle() + "Â°");
=======
			table.addCell(list.get(i).getAngle() + "¡ã");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getPaintWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createPolygonTable(List<PolygonBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(5);
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 1, 1, 1, 9, 1 });
		String[] tttile = { "åç§°", "é¢ç§¯", "é¢œè‰²", "ç›´çº¿", "æè¿°" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 1, 1, 1, 9, 1 });
		String[] tttile = { "Ãû³Æ", "Ãæ»ý", "ÑÕÉ«", "Ö±Ïß", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getPolyName()));
<<<<<<< HEAD
			table.addCell(list.get(i).getPolyArea() + "ãŽ¡");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPolyColor())));
			PdfPTable table2 = new PdfPTable(7);
			table2.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
			table2.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
			String[] tttile1 = { "åç§°", "é•¿åº¦", "è§’åº¦", "é¢œè‰²", "å®½åº¦", "è™šå®ž", "æè¿°" };
=======
			table.addCell(list.get(i).getPolyArea() + "©O");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPolyColor())));
			PdfPTable table2 = new PdfPTable(7);
			table2.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
			table2.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 3 });
			String[] tttile1 = { "Ãû³Æ", "³¤¶È", "½Ç¶È", "ÑÕÉ«", "¿í¶È", "ÐéÊµ", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			for (int j = 0; j < tttile1.length; j++) {
				table2.addCell(settabCell(tttile1[j]));
			}
			for (int j = 0; j < list.get(i).getPolyLine().size(); j++) {
				table2.addCell(setCell(list.get(i).getPolyLine().get(j).getName()));
				table2.addCell(list.get(i).getPolyLine().get(j).getLength() + "m");
<<<<<<< HEAD
				table2.addCell(list.get(i).getPolyLine().get(j).getAngle() + "Â°");
=======
				table2.addCell(list.get(i).getPolyLine().get(j).getAngle() + "¡ã");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "åç§°", "é¢ç§¯", "é•¿åº¦", "å®½åº¦", "é¢œè‰²", "çº¿å¤§å°", "è™šå®ž", "æè¿°" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "Ãû³Æ", "Ãæ»ý", "³¤¶È", "¿í¶È", "ÑÕÉ«", "Ïß´óÐ¡", "ÐéÊµ", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getRectName()));
<<<<<<< HEAD
			table.addCell(list.get(i).getRectArea() + "mÂ²");
=======
			table.addCell(list.get(i).getRectArea() + "m2");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "åç§°", "ä½“ç§¯", "é•¿åº¦", "å®½åº¦", "é«˜åº¦", "é¢œè‰²", "çº¿å¤§å°", "è™šå®ž", "æè¿°" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 1, 1, 1, 3 });
		String[] tttile = { "Ãû³Æ", "Ìå»ý", "³¤¶È", "¿í¶È", "¸ß¶È", "ÑÕÉ«", "Ïß´óÐ¡", "ÐéÊµ", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
<<<<<<< HEAD
			table.addCell(list.get(i).getVolum() + "mÂ³");
=======
			table.addCell(list.get(i).getVolum() + "m3");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			table.addCell(list.get(i).getLenght() + "m");
			table.addCell(list.get(i).getWidth() + "m");
			table.addCell(list.get(i).getHeight() + "m");
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getPaintWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createAngleTable(List<AngleBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(6);
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 3 });
		String[] tttile = { "åç§°", "è§’åº¦", "é¢œè‰²", "å®½åº¦", "è™šå®ž", "æè¿°" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 2, 1, 1, 1, 1, 3 });
		String[] tttile = { "Ãû³Æ", "½Ç¶È", "ÑÕÉ«", "¿í¶È", "ÐéÊµ", "ÃèÊö" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < tttile.length; i++) {
			table.addCell(settabCell(tttile[i]));
		}
		for (int i = 0; i < list.size(); i++) {
			table.addCell(setCell(list.get(i).getName()));
<<<<<<< HEAD
			table.addCell(list.get(i).getAngle() + "Â°");
=======
			table.addCell(list.get(i).getAngle() + "¡ã");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			table.addCell(setCell(SurveyUtils.getColor(list.get(i).getPaintColor())));
			table.addCell(list.get(i).getPaintWidth() + "");
			table.addCell(setCell(SurveyUtils.getStyle(list.get(i).isPaintIsFull())));
			table.addCell(setCell(list.get(i).getDescripte()));
		}
		return table;
	}

	private PdfPTable createTextTable(List<TextBean> list) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 1, 9 });
		String[] tttile = { "åºå·", "å†…å®¹" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 1, 9 });
		String[] tttile = { "ÐòºÅ", "ÄÚÈÝ" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		table.setWidthPercentage(100);// è®¾ç½®è¡¨æ ¼å®½åº¦ä¸º100%
		table.setWidths(new int[] { 1, 2, 7 });
		String[] tttile = { "åºå·", "æ—¶é•¿", "è·¯å¾„" };
=======
		table.setWidthPercentage(100);// ÉèÖÃ±í¸ñ¿í¶ÈÎª100%
		table.setWidths(new int[] { 1, 2, 7 });
		String[] tttile = { "ÐòºÅ", "Ê±³¤", "Â·¾¶" };
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
