package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.RectangleBean;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class CoordinateModel {
	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.RED, 4, true);
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10, true);
	
	float center_x,center_y,xto_x,xto_y,yto_x,yto_y,zto_x,zto_y;
	private PointF movePoint=new PointF();
	float center_mx,center_my,xto_mx,xto_my,yto_mx,yto_my,zto_mx,zto_my;
	
	public List<CoordinateBean> GetCoordlist=new ArrayList<CoordinateBean>();
	private CoordinateBean coordinate=new CoordinateBean();
	private static int index=-1;
	private static String select="";
	private int extra=200;
	public void DrawCoordinate(Canvas canvas){		
		canvas.drawLine(center_x, center_y, xto_x, xto_y, mPaint);
		canvas.drawLine(center_x, center_y, yto_x, yto_y, mPaint);
		canvas.drawLine(center_x, center_y, zto_x, zto_y, mPaint);
	}
	public void DrawCoordinatesOnBitmap(List<CoordinateBean> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			float cx=list.get(i).getCenterX();
			float cy=list.get(i).getCenterY();
			float xx=list.get(i).getXaxisX();
			float xy=list.get(i).getXaxisY();
			float yx=list.get(i).getYaxisX();
			float yy=list.get(i).getYaxisY();
			float zx=list.get(i).getZaxisX();
			float zy=list.get(i).getZaxisY();
			Paint paint=ViewContans.generatePaint(list.get(i).getPaintColor(), list.get(i).getPaintWidth(), list.get(i).isPaintIsFull());
			canvas.drawLine(cx, cy, xx, xy, paint);
			canvas.drawLine(cx, cy, yx, yy, paint);
			canvas.drawLine(cx, cy, zx, zy, paint);
			if (list.get(i).getLenght()>0&&list.get(i).getWidth()>0&&list.get(i).getHeight()>0) {
				ViewContans.AddTextOnCoordinate(list.get(i), canvas,
						list.get(i).getVolum()+"m³", list.get(i).getLenght()+"m",
						list.get(i).getWidth()+"m", list.get(i).getHeight()+"m");
			}
		}
	}
	public void MoveDrawCoordinate(Canvas canvas){
		canvas.drawLine(center_mx, center_my, xto_mx, xto_my, checkpaint);
		canvas.drawLine(center_mx, center_my, yto_mx, yto_my, checkpaint);
		canvas.drawLine(center_mx, center_my, zto_mx, zto_my, checkpaint);
	}

	public void Coordinate_touch_down(float x,float y){
		center_x = xto_x = yto_x = zto_x = x;
		center_y = xto_y = yto_y = zto_y = y;	
	}
	public void Coordinate_touch_move(float x,float y){
		center_x=x;
		center_y=y;	
		xto_x=center_x-extra+50;
		xto_y=center_y+extra-50;
		
		yto_x=center_x+extra;
		yto_y=center_y;
		
		zto_x=center_x;
		zto_y=center_y-extra;
	}
	public void ExtandCoordinateOneAxis_touch_move(float x,float y){
		if (select=="X") {
			xto_x=x;
			xto_y=y;
		}else if (select=="Y") {
			yto_x=x;
		}else if (select=="Z") {
			zto_y=y;
		}
	}
	public void Coordinate_touch_up(float x,float y,Canvas canvas){
		DrawCoordinate(canvas);
		AddCoordinateParams(GetCoordlist,
				center_x, center_y,
				xto_x, xto_y, 
				yto_x, yto_y,
				zto_x, zto_y);
	}
	
	public void MoveCoordinate_down(List<CoordinateBean> list,int i,float rx,float ry){
		center_mx=list.get(i).getCenterX();
		center_my=list.get(i).getCenterY();
		xto_mx=list.get(i).getXaxisX();
		xto_my=list.get(i).getXaxisY();
		yto_mx=list.get(i).getYaxisX();
		yto_my=list.get(i).getYaxisY();
		zto_mx=list.get(i).getZaxisX();
		zto_my=list.get(i).getZaxisY();
		coordinate.setName(list.get(i).getName());
		coordinate.setDescripte(list.get(i).getDescripte());
		coordinate.setLenght(list.get(i).getLenght());
		coordinate.setWidth(list.get(i).getWidth());
		coordinate.setHeight(list.get(i).getHeight());
		coordinate.setVolum(list.get(i).getVolum());
		coordinate.setPaintColor(list.get(i).getPaintColor());
		coordinate.setPaintWidth(list.get(i).getPaintWidth());
		coordinate.setPaintIsFull(list.get(i).isPaintIsFull());	
		movePoint.set(rx, ry);
		list.remove(i);
	}
	public void MoveCoordinate_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	center_mx=center_mx+dx;
	    	center_my=center_my+dy;	
	    	xto_mx=xto_mx+dx;
			xto_my=xto_my+dy;
			yto_mx=yto_mx+dx;
			yto_my=yto_my+dy;
			zto_mx=zto_mx+dx;
			zto_my=zto_my+dy;
	    }
	}
	public void MoveCoordinate_up(Canvas canvas){
		Paint paint=ViewContans.generatePaint(coordinate.getPaintColor(), coordinate.getPaintWidth(), coordinate.isPaintIsFull());	
		canvas.drawLine(center_mx, center_my, xto_mx, xto_my, paint);
		canvas.drawLine(center_mx, center_my, yto_mx, yto_my, paint);
		canvas.drawLine(center_mx, center_my, zto_mx, zto_my, paint);
		coordinate.setCenterX(center_mx);
		coordinate.setCenterY(center_my);
		coordinate.setXaxisX(xto_mx);
		coordinate.setXaxisY(xto_my);
		coordinate.setYaxisX(yto_mx);
		coordinate.setYaxisY(yto_my);
		coordinate.setZaxisX(zto_mx);
		coordinate.setZaxisY(zto_my);
		if (coordinate.getLenght()>0&&coordinate.getWidth()>0&&coordinate.getHeight()>0) {
			ViewContans.AddTextOnCoordinate(coordinate, canvas,
					coordinate.getVolum()+"m³", coordinate.getLenght()+"m",
					coordinate.getWidth()+"m", coordinate.getHeight()+"m");
		}
		AddChangeCoordinateParams(GetCoordlist, coordinate);
	}
	public int PitchOnCoordinate(List<CoordinateBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < lines.size(); i++) {
			int centerX=(int) lines.get(i).getCenterX();
			int centerY=(int) lines.get(i).getCenterY();
			boolean xCan=(centerX-50<dx)&&(centerX+50>dx);
			boolean yCan=(centerY-50<dy)&&(centerY+50>dy);	
			if (xCan&&yCan) {
				return i;
			}
		}
		return -1;
	}
	public boolean ExtandCoordinate(List<CoordinateBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
		for (int i = 0; i < lines.size(); i++) {			
			int xx=(int) lines.get(i).getXaxisX();
			int xy=(int) lines.get(i).getXaxisY();
			int yx=(int) lines.get(i).getYaxisX();
			int yy=(int) lines.get(i).getYaxisY();
			int zx=(int) lines.get(i).getZaxisX();
			int zy=(int) lines.get(i).getZaxisY();
			boolean x_axis=((xx-30<dx)&&(xx+30>dx))&&((xy-30<dy)&&(xy+30>dy));
			boolean y_axis=((yx-30<dx)&&(yx+30>dx))&&((yy-30<dy)&&(yy+30>dy));
			boolean z_axis=((zx-30<dx)&&(zx+30>dx))&&((zy-30<dy)&&(zy+30>dy));
			if (x_axis) {
				Logger.i("选中坐标轴", i+"=X轴");
				center_x=lines.get(i).getCenterX();
				center_y=lines.get(i).getCenterY();
				zto_x=(float)zx;
				zto_y=(float)zy;
				yto_x=(float)yx;
				yto_y=(float)yy;
				xto_x=x;
				xto_y=y;
				index=i;
				select="X";
				lines.remove(i);
				return true;
			}else if (y_axis) {
				Logger.i("选中坐标轴", i+"=Y轴");
				center_x=lines.get(i).getCenterX();
				center_y=lines.get(i).getCenterY();
				xto_x=(float)xx;
				xto_y=(float)xy;
				zto_x=(float)zx;
				zto_y=(float)zy;
				yto_x=x;
				yto_y=(float)yy;
				index=i;
				select="Y";
				lines.remove(i);
				return true;
			}else if (z_axis) {
				Logger.i("选中坐标轴", i+"=Z轴");
				center_x=lines.get(i).getCenterX();
				center_y=lines.get(i).getCenterY();
				xto_x=(float)xx;
				xto_y=(float)xy;
				yto_x=(float)yx;
				yto_y=(float)yy;
				zto_x=(float)zx;
				zto_y=y;
				index=i;
				select="Z";
				lines.remove(i);
				return true;
			}
		}
		return false;
	}
	public void AddCoordinateParams(List<CoordinateBean> list,
			float center_x,float center_y,float xto_x,float xto_y,
			float yto_x,float yto_y,float zto_x,float zto_y){
		CoordinateBean coor=new CoordinateBean();
		coor.setCenterX(center_x);
		coor.setCenterY(center_y);
		coor.setXaxisX(xto_x);
		coor.setXaxisY(xto_y);
		coor.setYaxisX(yto_x);
		coor.setYaxisY(yto_y);
		coor.setZaxisX(zto_x);
		coor.setZaxisY(zto_y);
		coor.setPaintWidth(4);
		coor.setPaintColor(Color.RED);
		coor.setPaintIsFull(true);
		coor.setVolum(0);
		coor.setName("Coordinate-"+list.size());
		coor.setDescripte("描述");
		list.add(coor);
	}
	public void AddChangeCoordinateParams(List<CoordinateBean> list,CoordinateBean coordinate){
//		CoordinateBean coor=new CoordinateBean();
//		coor.setCenterX(coordinate.getCenterX());
//		coor.setCenterY(coordinate.getCenterY());
//		coor.setXaxisX(coordinate.getXaxisX());
//		coor.setXaxisY(coordinate.getXaxisY());
//		coor.setYaxisX(coordinate.getYaxisX());
//		coor.setYaxisY(coordinate);
//		coor.setZaxisX(zto_x);
//		coor.setZaxisY(zto_y);
//		coor.setPaintWidth(4);
//		coor.setPaintColor(Color.RED);
//		coor.setPaintIsFull(true);
//		coor.setVolum(0);
//		coor.setName("Coordinate");
//		coor.setDescripte("描述");
		list.add(coordinate);
	}
	public void ChangeCoorAttributeAtIndex(List<CoordinateBean> list,
			int i,Canvas canvas,CoordinateBean coor){
		float cx=list.get(i).getCenterX();
		float cy=list.get(i).getCenterY();
		float xx=list.get(i).getXaxisX();
		float xy=list.get(i).getXaxisY();
		float yx=list.get(i).getYaxisX();
		float yy=list.get(i).getYaxisY();
		float zx=list.get(i).getZaxisX();
		float zy=list.get(i).getZaxisY();
		coor.setCenterX(cx);
		coor.setCenterY(cy);
		coor.setXaxisX(xx);
		coor.setXaxisY(xy);
		coor.setYaxisX(yx);
		coor.setYaxisY(yy);
		coor.setZaxisX(zx);
		coor.setZaxisY(zy);
		Paint paint=ViewContans.generatePaint(coor.getPaintColor(), coor.getPaintWidth(), coor.isPaintIsFull());		
		canvas.drawLine(cx, cy, xx, xy,paint);
		canvas.drawLine(cx, cy, yx, yy,paint);
		canvas.drawLine(cx, cy, zx, zy,paint);
		if (coor.getLenght()>0&&coor.getWidth()>0&&coor.getHeight()>0) {
			ViewContans.AddTextOnCoordinate(list.get(i), canvas,
					coor.getVolum()+"m³", coor.getLenght()+"m",
					coor.getWidth()+"m", coor.getHeight()+"m");
		}
		list.remove(i);		
		AddChangeCoordinateParams(GetCoordlist, coor);
	}
	public void RemoveIndexCoordinate(List<CoordinateBean> list,Canvas canvas,int index){        
		list.remove(index);
		for (int i = 0; i < list.size(); i++) {
			float cx=list.get(i).getCenterX();
			float cy=list.get(i).getCenterY();
			float xx=list.get(i).getXaxisX();
			float xy=list.get(i).getXaxisY();
			float yx=list.get(i).getYaxisX();
			float yy=list.get(i).getYaxisY();
			float zx=list.get(i).getZaxisX();
			float zy=list.get(i).getZaxisY();
			Paint paint=ViewContans.generatePaint(list.get(i).getPaintColor(), list.get(i).getPaintWidth(), list.get(i).isPaintIsFull());
			canvas.drawLine(cx, cy, xx, xy, paint);
			canvas.drawLine(cx, cy, yx, yy, paint);
			canvas.drawLine(cx, cy, zx, zy, paint);
			if (list.get(i).getLenght()>0&&list.get(i).getWidth()>0&&list.get(i).getHeight()>0) {
				ViewContans.AddTextOnCoordinate(list.get(i), canvas,
						list.get(i).getVolum()+"m³", list.get(i).getLenght()+"m",
						list.get(i).getWidth()+"m", list.get(i).getHeight()+"m");
			}
		}
	}
}
