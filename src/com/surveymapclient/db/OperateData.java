package com.surveymapclient.db;

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
	
	public static void insertPolygon(long key,List<PolygonBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			Polygon polygon=new Polygon();
			polygon.setName(list.get(i).getPolyName());
			polygon.setArea(list.get(i).getPolyArea());
			polygon.setDesc(list.get(i).getDescripe());
			polygon.setColor(list.get(i).getPolyColor());
			polygon.setKey(key);
			List<LineBean> linelist=list.get(i).getPolyLine();
			for (int j = 0; j < list.get(i).getPolyLine().size(); j++) {
				Lines lines=new Lines();				
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
				lines.setEndy(linelist.get(j).getEndY());
				lines.setKey(key);
				lines.setPolygon(polygon);
				helper.insertDataLines(lines);
			}
			helper.insertDataPolygon(polygon);
		}
	}
	
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
	public static void insertText(long key,List<TextBean> list,DBHelper helper){
		for (int i = 0; i < list.size(); i++) {
			TextNote text=new TextNote();
			
			text.setContent(list.get(i).getText());
			text.setTx(list.get(i).getTx());
			text.setTy(list.get(i).getTy());	
			text.setKey(key);
			helper.insertDataText(text);
		}
	}
	
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
	public static void insertModule(long key,String name,int type, DBHelper helper){
		Module module=new Module();
		module.setName(name);
		module.setType(type);
		module.setKey(key);
		helper.insertDataModule(module);
	}
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
