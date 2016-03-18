package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CoordinateLineBean;
import com.surveymapclient.entity.RectangleLineBean;

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
	
	public List<CoordinateLineBean> getCoordlist=new ArrayList<CoordinateLineBean>();
	private static int index=-1;
	private static String select="";
	public void DrawCoordinate(Canvas canvas){
		
		canvas.drawLine(center_x, center_y, xto_x, xto_y, mPaint);
		canvas.drawLine(center_x, center_y, yto_x, yto_y, mPaint);
		canvas.drawLine(center_x, center_y, zto_x, zto_y, mPaint);
	}
	public void DrawCoordinateOnBitmap(List<CoordinateLineBean> lines,Canvas canvas){
		for (int i = 0; i < lines.size(); i++) {
			float cx=lines.get(i).getCenterPoint().x;
			float cy=lines.get(i).getCenterPoint().y;
			float xx=lines.get(i).getxPoint().x;
			float xy=lines.get(i).getxPoint().y;
			float yx=lines.get(i).getyPoint().x;
			float yy=lines.get(i).getyPoint().y;
			float zx=lines.get(i).getzPoint().x;
			float zy=lines.get(i).getzPoint().y;
			canvas.drawLine(cx, cy, xx, xy, mPaint);
			canvas.drawLine(cx, cy, yx, yy, mPaint);
			canvas.drawLine(cx, cy, zx, zy, mPaint);
		}
	}
	public void MoveDrawCoordinate(Canvas canvas){
		canvas.drawLine(center_mx, center_my, xto_mx, xto_my, checkpaint);
		canvas.drawLine(center_mx, center_my, yto_mx, yto_my, checkpaint);
		canvas.drawLine(center_mx, center_my, zto_mx, zto_my, checkpaint);
	}
	public void MoveCoordinateOnBitmap(Canvas canvas){
		canvas.drawLine(center_mx, center_my, xto_mx, xto_my, mPaint);
		canvas.drawLine(center_mx, center_my, yto_mx, yto_my, mPaint);
		canvas.drawLine(center_mx, center_my, zto_mx, zto_my, mPaint);		
	}
	public void Coordinate_touch_down(float x,float y){
		center_x = xto_x = yto_x = zto_x = x;
		center_y = xto_y = yto_y = zto_y = y;	
	}
	public void Coordinate_touch_move(float x,float y){
		center_x=x;
		center_y=y;	
		xto_x=center_x-70;
		xto_y=center_y+70;
		yto_x=center_x+100;
		yto_y=center_y;
		zto_x=center_x;
		zto_y=center_y-100;
	}
	public void ExtandCoordinateOneAxis_touch_move(List<CoordinateLineBean> lines,float x,float y){
		center_x=lines.get(index).getCenterPoint().x;
		center_y=lines.get(index).getCenterPoint().y;
		if (select=="X") {
			yto_x=lines.get(index).getyPoint().x;
			yto_y=lines.get(index).getyPoint().y;
			zto_x=lines.get(index).getzPoint().x;
			zto_y=lines.get(index).getzPoint().y;
			xto_x=x;
			xto_y=y;
		}else if (select=="Y") {
			xto_x=lines.get(index).getxPoint().x;
			xto_y=lines.get(index).getxPoint().y;
			yto_x=x;
			yto_y=lines.get(index).getyPoint().y;
			zto_x=lines.get(index).getzPoint().x;
			zto_y=lines.get(index).getzPoint().y;
		}else if (select=="Z") {
			xto_x=lines.get(index).getxPoint().x;
			xto_y=lines.get(index).getxPoint().y;
			yto_x=lines.get(index).getyPoint().x;
			yto_y=lines.get(index).getyPoint().y;
			zto_x=lines.get(index).getzPoint().x;
			zto_y=y;
		}
	}
	public void Coordinate_touch_up(float x,float y,Canvas canvas){
//		center_x= ViewContans.AdsorbPoint((int)Math.floor(center_x));
//		center_y= ViewContans.AdsorbPoint((int)Math.floor(center_y));
		DrawCoordinate(canvas);
		AddCoordinateParams(getCoordlist,
				center_x, center_y,
				xto_x, xto_y, 
				yto_x, yto_y,
				zto_x, zto_y);
	}
	
	public void MoveCoordinate_down(List<CoordinateLineBean> lines,int i,float rx,float ry){
		center_mx=lines.get(i).getCenterPoint().x;
		center_my=lines.get(i).getCenterPoint().y;
		xto_mx=lines.get(i).getxPoint().x;
		xto_my=lines.get(i).getxPoint().y;
		yto_mx=lines.get(i).getyPoint().x;
		yto_my=lines.get(i).getyPoint().y;
		zto_mx=lines.get(i).getzPoint().x;
		zto_my=lines.get(i).getzPoint().y;
		movePoint.set(rx, ry);
		lines.remove(i);
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
//		center_mx= ViewContans.AdsorbPoint((int)Math.floor(center_mx));
//		center_my= ViewContans.AdsorbPoint((int)Math.floor(center_mx));
		AddCoordinateParams(getCoordlist, center_mx, center_my,
				xto_mx, xto_my, 
				yto_mx, yto_my, 
				zto_mx, zto_my);
		MoveCoordinateOnBitmap(canvas);
	}
	public int PitchOnCoordinate(List<CoordinateLineBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < lines.size(); i++) {
			int centerX=(int) lines.get(i).getCenterPoint().x;
			int centerY=(int) lines.get(i).getCenterPoint().y;
			boolean xCan=(centerX-50<dx)&&(centerX+50>dx);
			boolean yCan=(centerY-50<dy)&&(centerY+50>dy);	
			if (xCan&&yCan) {
				return i;
			}
		}
		return -1;
	}
	public boolean ExtandCoordinate(List<CoordinateLineBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
		for (int i = 0; i < lines.size(); i++) {			
			int xx=(int) lines.get(i).getxPoint().x;
			int xy=(int) lines.get(i).getxPoint().y;
			int yx=(int) lines.get(i).getyPoint().x;
			int yy=(int) lines.get(i).getyPoint().y;
			int zx=(int) lines.get(i).getzPoint().x;
			int zy=(int) lines.get(i).getzPoint().y;
			boolean x_axis=((xx-30<dx)&&(xx+30>dx))&&((xy-30<dy)&&(xy+30>dy));
			boolean y_axis=((yx-30<dx)&&(yx+30>dx))&&((yy-30<dy)&&(yy+30>dy));
			boolean z_axis=((zx-30<dx)&&(zx+30>dx))&&((zy-30<dy)&&(zy+30>dy));
			if (x_axis) {
				Logger.i("选中坐标轴", i+"=X轴");
				center_x=lines.get(i).getCenterPoint().x;
				center_y=lines.get(i).getCenterPoint().y;
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
				center_x=lines.get(i).getCenterPoint().x;
				center_y=lines.get(i).getCenterPoint().y;
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
				center_x=lines.get(i).getCenterPoint().x;
				center_y=lines.get(i).getCenterPoint().y;
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
	public void AddCoordinateParams(List<CoordinateLineBean> lines,
			float center_x,float center_y,float xto_x,float xto_y,
			float yto_x,float yto_y,float zto_x,float zto_y){
		CoordinateLineBean coor=new CoordinateLineBean(new PointF(center_x, center_y),
				new PointF(xto_x, xto_y), new PointF(yto_x, yto_y), new PointF(zto_x, zto_y));
		coor.setDescripte("描述");
		coor.setWidth(4);
		coor.setName("coordinate");
		coor.setFull(true);
		lines.add(coor);
	}
}
