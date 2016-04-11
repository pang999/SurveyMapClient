package com.surveymapclient.db;

<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
import java.util.List;

import com.surveymapclient.common.Logger;
import com.surveymapclient.db.greendao.Angle;
import com.surveymapclient.db.greendao.AudioNote;
import com.surveymapclient.db.greendao.Coordinate;
import com.surveymapclient.db.greendao.Line;
import com.surveymapclient.db.greendao.Lines;
import com.surveymapclient.db.greendao.Module;
import com.surveymapclient.db.greendao.Polygon;
import com.surveymapclient.db.greendao.Rectangle;
import com.surveymapclient.db.greendao.TextNote;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.entity.TextBean;
import com.tencent.a.a.a.a.c;

import android.R.integer;

public class OperateData {
	
	public static void insertLine(long key,List<LineBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			Line line=new Line();
			line.setName(list.get(i).getName());
			line.setDesc(list.get(i).getDescripte());
			line.setAngle(list.get(i).getAngle());
			line.setLenght(list.get(i).getLength());
			line.setPcolor(list.get(i).getPaintColor());
			line.setPwidth(list.get(i).getPaintWidth());
			line.setIsfull(list.get(i).isPaintIsFull());
			line.setStartx(list.get(i).getStartX());
			line.setStarty(list.get(i).getStartY());
			line.setEndx(list.get(i).getEndX());
			line.setEndy(list.get(i).getEndY());
			line.setKey(key);
			helper.insertDateLine(line);
		}			
	}
<<<<<<< HEAD
	public static List<LineBean> searchLine(long key,DBHelper helper){
		List<Line> list=helper.searchDataLine(key);
		List<LineBean> lineBeans=new ArrayList<LineBean>();
		for (int i = 0; i < list.size(); i++) {
			LineBean lineBean=new LineBean();
			lineBean.setName(list.get(i).getName());
			lineBean.setDescripte(list.get(i).getDesc());
			lineBean.setAngle(list.get(i).getAngle());
			lineBean.setLength(list.get(i).getLenght());
			lineBean.setPaintColor(list.get(i).getPcolor());
			lineBean.setPaintWidth(list.get(i).getPwidth());
			lineBean.setPaintIsFull(list.get(i).getIsfull());
			lineBean.setStartX(list.get(i).getStartx());
			lineBean.setStartY(list.get(i).getStarty());
			lineBean.setEndX(list.get(i).getEndx());
			lineBean.setEndY(list.get(i).getEndy());
			lineBeans.add(lineBean);
		}
		return lineBeans;		
	}
	public static void dedeleLine(List<Line> lines,DBHelper helper){
		for (int i = 0; i < lines.size(); i++) {
			helper.deleteDataLine(lines.get(i));
		}		
	}
=======
	
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public static void insertPolygon(long key,List<PolygonBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			Polygon polygon=new Polygon();
			polygon.setName(list.get(i).getPolyName());
			polygon.setArea(list.get(i).getPolyArea());
			polygon.setDesc(list.get(i).getDescripe());
			polygon.setColor(list.get(i).getPolyColor());
<<<<<<< HEAD
			polygon.setPolygon_id(key+i);
			polygon.setKey(key);
			List<LineBean> linelist=list.get(i).getPolyLine();				
			for (int j = 0; j < linelist.size(); j++) {
//				Logger.i("数据库数据", "多边形直线insert="+linelist.get(j).getName());
				Lines lines=new Lines();
				lines.setKey(key);
=======
			polygon.setKey(key);
			List<LineBean> linelist=list.get(i).getPolyLine();
			for (int j = 0; j < list.get(i).getPolyLine().size(); j++) {
				Lines lines=new Lines();				
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
				lines.setName(linelist.get(j).getName());
				lines.setDesc(linelist.get(j).getDescripte());
				lines.setAngle(linelist.get(j).getAngle());
				lines.setLenght(linelist.get(j).getLength());
				lines.setPcolor(linelist.get(j).getPaintColor());
				lines.setPwidth(linelist.get(j).getPaintWidth());
				lines.setIsfull(linelist.get(j).isPaintIsFull());
				lines.setStartx(linelist.get(j).getStartX());
				lines.setStarty(linelist.get(j).getStartY());
				lines.setEndx(linelist.get(j).getEndX());
<<<<<<< HEAD
				lines.setEndy(linelist.get(j).getEndY());	
				lines.setPolygon_id(Long.parseLong(String.valueOf(i)));
				lines.setPolygon(polygon);			
=======
				lines.setEndy(linelist.get(j).getEndY());
				lines.setKey(key);
				lines.setPolygon(polygon);
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
				helper.insertDataLines(lines);
			}
			helper.insertDataPolygon(polygon);
		}
	}
	
<<<<<<< HEAD
	public static List<PolygonBean> searchPolygon(long key,DBHelper helper){
		List<Polygon> list=helper.searchDataPolygon(key);
		Logger.i("数据库数据", "多边形总数前="+list.size());

		List<PolygonBean> polygonlist=new ArrayList<PolygonBean>();
		for (int i = 0; i < list.size(); i++) {
			PolygonBean polygon=new PolygonBean();
			polygon.setPolyName(list.get(i).getName());
			polygon.setDescripe(list.get(i).getDesc());
			polygon.setPolyColor(list.get(i).getColor());
			polygon.setPolyArea(list.get(i).getArea());
			List<LineBean> linelist=new ArrayList<LineBean>();
			for (int j = 0; j < list.get(i).getLines().size(); j++) {
				LineBean line=new LineBean();
				line.setName(list.get(i).getLines().get(j).getName());
				line.setDescripte(list.get(i).getLines().get(j).getDesc());
				line.setAngle(list.get(i).getLines().get(j).getAngle());
				line.setLength(list.get(i).getLines().get(j).getLenght());
				line.setStartX(list.get(i).getLines().get(j).getStartx());
				line.setStartY(list.get(i).getLines().get(j).getStarty());
				line.setEndX(list.get(i).getLines().get(j).getEndx());
				line.setEndY(list.get(i).getLines().get(j).getEndy());
				line.setPaintColor(list.get(i).getLines().get(j).getPcolor());
				line.setPaintWidth(list.get(i).getLines().get(j).getPwidth());
				line.setPaintIsFull(list.get(i).getLines().get(j).getIsfull());
				linelist.add(line);
			}
			polygon.setPolyLine(linelist);
			polygonlist.add(polygon);			
		}
		Logger.i("数据库数据", "多边形总数="+polygonlist.size());
		return polygonlist;
	}
	
	public static void deletePolygon(List<Polygon> list,DBHelper helper){
		for (int i = 0; i <list.size(); i++) {
			helper.deleteDataPolygon(list.get(i));
		}
	}
	
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public static void insertRectangle(long key,List<RectangleBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			Rectangle rectangle=new Rectangle();
			rectangle.setName(list.get(i).getRectName());
			rectangle.setArea(list.get(i).getRectArea());
			rectangle.setLenght(list.get(i).getRectLenght());
			rectangle.setWidth(list.get(i).getRectWidth());
			rectangle.setPcolor(list.get(i).getPaintColor());
			rectangle.setPwidth(list.get(i).getPaintWidth());
			rectangle.setIsfull(list.get(i).isFull());
			rectangle.setDesc(list.get(i).getDescripte());
			rectangle.setStartx(list.get(i).getStartX());
			rectangle.setStarty(list.get(i).getStartY());
			rectangle.setEndx(list.get(i).getEndX());
			rectangle.setEndy(list.get(i).getEndY());
			rectangle.setKey(key);
			helper.insertDataRect(rectangle);
		}
	}
<<<<<<< HEAD
	public static List<RectangleBean> searchRectangle(long key,DBHelper helper){
		List<Rectangle> list=helper.searchDataRectangle(key);
		List<RectangleBean> rectlist=new ArrayList<RectangleBean>();
		for (int i = 0; i < list.size(); i++) {
			RectangleBean rect=new RectangleBean();
			rect.setRectName(list.get(i).getName());
			rect.setRectArea(list.get(i).getArea());
			rect.setRectLenght(list.get(i).getLenght());
			rect.setRectWidth(list.get(i).getPwidth());
			rect.setPaintColor(list.get(i).getPcolor());
			rect.setPaintWidth(list.get(i).getPwidth());
			rect.setFull(list.get(i).getIsfull());
			rect.setDescripte(list.get(i).getDesc());
			rect.setStartX(list.get(i).getStartx());
			rect.setStartY(list.get(i).getStarty());
			rect.setEndX(list.get(i).getEndx());
			rect.setEndY(list.get(i).getEndy());
			rectlist.add(rect);
		}
		return rectlist;
		
	}
	public static void deleteRectangle(List<Rectangle> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			helper.deleteDataRectangle(list.get(i));
		}
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public static void insertCoordinate(long key,List<CoordinateBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			Coordinate coor=new Coordinate();
			coor.setName(list.get(i).getName());
			coor.setDesc(list.get(i).getDescripte());
			coor.setVolum(list.get(i).getVolum());
			coor.setLenght(list.get(i).getLenght());
			coor.setWidth(list.get(i).getWidth());
			coor.setHeight(list.get(i).getHeight());
			coor.setPcolor(list.get(i).getPaintColor());
			coor.setPwidth(list.get(i).getPaintWidth());
			coor.setIsfull(list.get(i).isPaintIsFull());
			coor.setCenterx(list.get(i).getCenterX());
			coor.setCentery(list.get(i).getCenterY());
			coor.setXaxisx(list.get(i).getXaxisX());
			coor.setXaxisy(list.get(i).getXaxisY());
			coor.setYaxisx(list.get(i).getYaxisX());
			coor.setYaxisy(list.get(i).getYaxisY());
			coor.setZaxisx(list.get(i).getZaxisX());
			coor.setZaxisy(list.get(i).getZaxisY());
			coor.setKey(key);
			helper.insertDataCoor(coor);
		}
	}
<<<<<<< HEAD
	public static List<CoordinateBean> searchCoordinate(long key,DBHelper helper){
		List<Coordinate> list=helper.searchDataCoordinate(key);
		List<CoordinateBean> coorlist=new ArrayList<CoordinateBean>();
		for (int i = 0; i < list.size(); i++) {
			CoordinateBean coor=new CoordinateBean();
			coor.setName(list.get(i).getName());
			coor.setDescripte(list.get(i).getDesc());
			coor.setVolum(list.get(i).getVolum());
			coor.setLenght(list.get(i).getLenght());
			coor.setWidth(list.get(i).getWidth());
			coor.setHeight(list.get(i).getHeight());
			coor.setPaintColor(list.get(i).getPcolor());
			coor.setPaintWidth(list.get(i).getPwidth());
			coor.setPaintIsFull(list.get(i).getIsfull());
			coor.setCenterX(list.get(i).getCenterx());
			coor.setCenterY(list.get(i).getCentery());
			coor.setXaxisX(list.get(i).getXaxisx());
			coor.setXaxisY(list.get(i).getXaxisy());
			coor.setYaxisX(list.get(i).getYaxisx());
			coor.setYaxisY(list.get(i).getYaxisy());
			coor.setZaxisX(list.get(i).getZaxisx());
			coor.setZaxisY(list.get(i).getZaxisy());
			coorlist.add(coor);
		}
		return coorlist;
	}
	public static void deleteCoordinate(List<Coordinate> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			helper.deleteDataCoor(list.get(i));
		}
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public static void insertAngle(long key,List<AngleBean> list,DBHelper helper){
		for (int i = 0; i <list.size(); i++) {
			Angle angle=new Angle();
			angle.setName(list.get(i).getName());
			angle.setDesc(list.get(i).getDescripte());
			angle.setAngle(list.get(i).getAngle());
			angle.setPcolor(list.get(i).getPaintColor());
			angle.setPwidth(list.get(i).getPaintWidth());
			angle.setIsfull(list.get(i).isPaintIsFull());
			angle.setStartx(list.get(i).getStartX());
			angle.setStarty(list.get(i).getStartY());
			angle.setAnglex(list.get(i).getAngleX());
			angle.setAngley(list.get(i).getAngleY());
			angle.setEndx(list.get(i).getEndX());
			angle.setEndy(list.get(i).getEndY());
			angle.setKey(key);
			helper.insertDataAngle(angle);
		}
	}
<<<<<<< HEAD
	public static void deleteAngle(List<Angle> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			helper.deleteDataAngle(list.get(i));
		}
	}
	public static List<AngleBean> searchAngle(long key,DBHelper helper){
		List<Angle> list=helper.searchDataAngle(key);
		List<AngleBean> anglelist=new ArrayList<AngleBean>();
		for (int i = 0; i < list.size(); i++) {
			AngleBean angle=new AngleBean();
			angle.setName(list.get(i).getName());
			angle.setDescripte(list.get(i).getDesc());
			angle.setAngle(list.get(i).getAngle());
			angle.setPaintColor(list.get(i).getPcolor());
			angle.setPaintWidth(list.get(i).getPwidth());
			angle.setPaintIsFull(list.get(i).getIsfull());
			angle.setStartX(list.get(i).getStartx());
			angle.setStartY(list.get(i).getStarty());
			angle.setAngleX(list.get(i).getAnglex());
			angle.setAngleY(list.get(i).getAngley());
			angle.setEndX(list.get(i).getEndx());
			angle.setEndY(list.get(i).getEndy());
			anglelist.add(angle);
		}
		return anglelist;
	}
	public static void insertText(long key,List<TextBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			TextNote text=new TextNote();		
=======
	public static void insertText(long key,List<TextBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			TextNote text=new TextNote();
			
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			text.setContent(list.get(i).getText());
			text.setTx(list.get(i).getTx());
			text.setTy(list.get(i).getTy());	
			text.setKey(key);
			helper.insertDataText(text);
		}
	}
	
<<<<<<< HEAD
	public static List<TextBean> searchText(long key,DBHelper helper){
		List<TextNote> list=helper.searchDataText(key);
		List<TextBean> textlist=new ArrayList<TextBean>();
		for (int i = 0; i < list.size(); i++) {
			TextBean text=new TextBean();
			text.setText(list.get(i).getContent());
			text.setTx(list.get(i).getTx());
			text.setTy(list.get(i).getTy());
			textlist.add(text);
		}
		return textlist;
	}
	public static void deleteText(List<TextNote> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			helper.deleteDataText(list.get(i));
		}
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
	public static void insertAudio(long key,List<AudioBean> list,DBHelper helper){
		for (int i = 0; i <list.size(); i++) {
			AudioNote audio=new AudioNote();
			audio.setUrl(list.get(i).getUrl());
			audio.setLenght(list.get(i).getLenght());
			audio.setAx(list.get(i).getAx());
			audio.setAy(list.get(i).getAy());
			audio.setKey(key);
			helper.insertDataAudio(audio);
		}
	}
<<<<<<< HEAD
	
	public static List<AudioBean> searchAudio(long key,DBHelper helper){
		
		List<AudioNote> list=helper.searchDataAudio(key);
		List<AudioBean> audiolist=new ArrayList<AudioBean>();
		for (int i = 0; i < list.size(); i++) {
			AudioBean audio=new AudioBean();
			audio.setUrl(list.get(i).getUrl());
			audio.setLenght(list.get(i).getLenght());
			audio.setAx(list.get(i).getAx());
			audio.setAy(list.get(i).getAy());
			audiolist.add(audio);
		}		
		return audiolist;		
	}
	public static void deleteAudio(List<AudioNote> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			helper.deleteDataAudio(list.get(i));
		}
	}
	public static void insertModule(long key,String name,String imgurl,int type, DBHelper helper){
		Module module=new Module();
		module.setName(name);
		module.setImgUrl(imgurl);
=======
	public static void insertModule(long key,String name,int type, DBHelper helper){
		Module module=new Module();
		module.setName(name);
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		module.setType(type);
		module.setKey(key);
		helper.insertDataModule(module);
	}
<<<<<<< HEAD
	public static List<Module> searchModule(DBHelper helper){
		return helper.searchDataModule();
	}
	public static void deleteModule(List<Module> list,int i,DBHelper helper){
		helper.deleteDataModule(list.get(i));
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
//	long key=121212;
//	OperateData.insertLine(key, defineview.BackLinelist(), helper);
//	OperateData.insertRectangle(key, defineview.BackRectlist(), helper);
//	OperateData.insertCoordinate(key, defineview.BackCoorlist(), helper);
//	OperateData.insertAngle(key, defineview.BackAnglelist(), helper);
//	OperateData.insertText(key, defineview.BackTextlist(), helper);
//	OperateData.insertAudio(key, defineview.BackAudiolist(), helper);
//	for (int i = 0; i < helper.searchDataLine(key).size(); i++) {
//		Logger.i("数据库数据", "直线="+helper.searchDataLine(key).get(i).getName());				
//	}
//	for (int i = 0; i < helper.searchDataRectangle(key).size(); i++) {
//		Logger.i("数据库数据", "矩形="+helper.searchDataRectangle(key).get(i).getName());				
//	}
//	for (int i = 0; i < helper.searchDataCoordinate(key).size(); i++) {
//		Logger.i("数据库数据", "坐标="+helper.searchDataCoordinate(key).get(i).getName());				
//	}
//	for (int i = 0; i < helper.searchDataAngle(key).size(); i++) {
//		Logger.i("数据库数据", "角度="+helper.searchDataAngle(key).get(i).getName());				
//	}
//	for (int i = 0; i < helper.searchDataText(key).size(); i++) {
//		Logger.i("数据库数据", "文本="+helper.searchDataText(key).get(i).getContent());				
//	}
//	for (int i = 0; i < helper.searchDataAudio(key).size(); i++) {
//		Logger.i("数据库数据", "录音="+helper.searchDataAudio(key).get(i).getUrl());				
//	}

}
