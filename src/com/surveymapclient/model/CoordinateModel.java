package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;

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
//	private static int index=-1;
	private static String select="";
	private int extra=200;
	public void DrawCoordinate(Canvas canvas){		
		canvas.drawLine(center_x, center_y, xto_x, xto_y, mPaint);
		canvas.drawLine(center_x, center_y, yto_x, yto_y, mPaint);
		canvas.drawLine(center_x, center_y, zto_x, zto_y, mPaint);
	}
	public void DrawCoordinatesOnBitmap(List<CoordinateBean> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			ViewContans.DrawLine(canvas, list.get(i).getxLine());
			ViewContans.DrawLine(canvas, list.get(i).getyLine());
			ViewContans.DrawLine(canvas, list.get(i).getzLine());
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
			yto_y=y;
		}else if (select=="Z") {
			zto_y=y;
			zto_x=x;
		}
	}
	public void Coordinate_touch_up(float x,float y,Canvas canvas){
		DrawCoordinate(canvas);
		AddCoordinateParams(GetCoordlist,
				ViewContans.setLineBean(center_x, center_y, xto_x, xto_y),
				ViewContans.setLineBean(center_x, center_y, yto_x, yto_y),
				ViewContans.setLineBean(center_x, center_y, zto_x, zto_y));
	}
	
	public void MoveCoordinate_down(List<CoordinateBean> list,int i,float rx,float ry){
		center_mx=list.get(i).getxLine().getStartX();
		center_my=list.get(i).getxLine().getStartY();
		xto_mx=list.get(i).getxLine().getEndX();
		xto_my=list.get(i).getxLine().getEndY();
		yto_mx=list.get(i).getyLine().getEndX();
		yto_my=list.get(i).getyLine().getEndY();
		zto_mx=list.get(i).getzLine().getEndX();
		zto_my=list.get(i).getzLine().getEndY();
		coordinate.setName(list.get(i).getName());
		coordinate.setDescripte(list.get(i).getDescripte());
		coordinate.setVolum(list.get(i).getVolum());
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
		ViewContans.DrawLine(canvas,ViewContans.setLineBean(center_mx, center_my, xto_mx,xto_my));
		ViewContans.DrawLine(canvas,ViewContans.setLineBean(center_mx, center_my, yto_mx,yto_my));
		ViewContans.DrawLine(canvas,ViewContans.setLineBean(center_mx, center_my, zto_mx,zto_my));
		coordinate.setxLine(ViewContans.setLineBean(center_mx, center_my, xto_mx,xto_my));
		coordinate.setyLine(ViewContans.setLineBean(center_mx, center_my, yto_mx,yto_my));
		coordinate.setzLine(ViewContans.setLineBean(center_mx, center_my, zto_mx,zto_my));		
		AddChangeCoordinateParams(GetCoordlist, coordinate);
	}
	public int PitchOnCoordinate(List<CoordinateBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < lines.size(); i++) {
			int centerX=(int) lines.get(i).getxLine().getStartX();
			int centerY=(int) lines.get(i).getxLine().getStartY();
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
			int xx=(int) lines.get(i).getxLine().getEndX();
			int xy=(int) lines.get(i).getxLine().getEndY();
			int yx=(int) lines.get(i).getyLine().getEndX();
			int yy=(int) lines.get(i).getyLine().getEndY();
			int zx=(int) lines.get(i).getzLine().getEndX();
			int zy=(int) lines.get(i).getzLine().getEndY();
			boolean x_axis=((xx-30<dx)&&(xx+30>dx))&&((xy-30<dy)&&(xy+30>dy));
			boolean y_axis=((yx-30<dx)&&(yx+30>dx))&&((yy-30<dy)&&(yy+30>dy));
			boolean z_axis=((zx-30<dx)&&(zx+30>dx))&&((zy-30<dy)&&(zy+30>dy));
			if (x_axis) {
				Logger.i("选中坐标轴", i+"=X轴");
				center_x=lines.get(i).getxLine().getStartX();
				center_y=lines.get(i).getxLine().getStartY();
				zto_x=(float)zx;
				zto_y=(float)zy;
				yto_x=(float)yx;
				yto_y=(float)yy;
				xto_x=x;
				xto_y=y;
//				index=i;
				select="X";
				lines.remove(i);
				return true;
			}else if (y_axis) {
				Logger.i("选中坐标轴", i+"=Y轴");
				center_x=lines.get(i).getxLine().getStartX();
				center_y=lines.get(i).getxLine().getStartY();
				xto_x=(float)xx;
				xto_y=(float)xy;
				zto_x=(float)zx;
				zto_y=(float)zy;
				yto_x=x;
				yto_y=y;
//				index=i;
				select="Y";
				lines.remove(i);
				return true;
			}else if (z_axis) {
				Logger.i("选中坐标轴", i+"=Z轴");
				center_x=lines.get(i).getxLine().getStartX();
				center_y=lines.get(i).getxLine().getStartY();
				xto_x=(float)xx;
				xto_y=(float)xy;
				yto_x=(float)yx;
				yto_y=(float)yy;
				zto_x=x;
				zto_y=y;
//				index=i;
				select="Z";
				lines.remove(i);
				return true;
			}
		}
		return false;
	}
	public void AddCoordinateParams(List<CoordinateBean> list,LineBean xLine,LineBean yLine,LineBean zLine){
		CoordinateBean coor=new CoordinateBean();
		coor.setxLine(xLine);
		coor.setyLine(yLine);
		coor.setzLine(zLine);
		coor.setVolum(0.0);
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
		ViewContans.DrawLine(canvas, list.get(i).getxLine());
		ViewContans.DrawLine(canvas, list.get(i).getyLine());
		ViewContans.DrawLine(canvas, list.get(i).getzLine());
		list.remove(i);		
		AddChangeCoordinateParams(GetCoordlist, coor);
	}
	public void RemoveIndexCoordinate(List<CoordinateBean> list,Canvas canvas,int index){        
		list.remove(index);
		for (int i = 0; i < list.size(); i++) {
			ViewContans.DrawLine(canvas, list.get(i).getxLine());
			ViewContans.DrawLine(canvas, list.get(i).getyLine());
			ViewContans.DrawLine(canvas, list.get(i).getzLine());
		}
	}
}
