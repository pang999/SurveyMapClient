package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.AngleIndexBean;
import com.surveymapclient.entity.AngleLineBean;
import com.surveymapclient.entity.CoordinateLineBean;
import com.surveymapclient.entity.CouplePointLineBean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class AngleModel {
	
	//连续线段获取坐标
    private List<PointF> continuousPoints;

    
	Path mPath;
	private Paint mPaint=ViewContans.generatePaint(Color.RED, 4);
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10);
	private float mX, mY;// 临时点坐标
	private static final float TOUCH_TOLERANCE = 4;
	float start_x,start_y,end_x,end_y,angle_x,angle_y;
	float start_mx,start_my,end_mx,end_my,angle_mx,angle_my;
	public List<AngleLineBean> getAnglelist=new ArrayList<AngleLineBean>();
	private PointF movePoint=new PointF();
	public void DrawPath(Canvas canvas){
		if (mPath!=null) {
			canvas.drawPath(mPath, mPaint);
		}
	}

	public void DrawAngle(Canvas canvas){
		canvas.drawLine(start_x, start_y, angle_x, angle_y, mPaint);
		canvas.drawLine(angle_x, angle_y, end_x, end_y, mPaint);
	}
	public void MoveDrawAngle(Canvas canvas){
		canvas.drawLine(start_mx, start_my, angle_mx, angle_my, checkpaint);
		canvas.drawLine(angle_mx, angle_my, end_mx, end_my, checkpaint);
	}
	public void MoveDrawAngleOnBitmap(Canvas canvas){
		canvas.drawLine(start_mx, start_my, angle_mx, angle_my, mPaint);
		canvas.drawLine(angle_mx, angle_my, end_mx, end_my, mPaint);
	}
	
	public void DrawAngleOnBitmap(List<AngleLineBean> lines,Canvas canvas){
		for (int i = 0; i < lines.size(); i++) {
			float sx=lines.get(i).getStartPoint().x;
			float sy=lines.get(i).getStartPoint().y;
			float ax=lines.get(i).getAnglePoint().x;
			float ay=lines.get(i).getAnglePoint().y;
			float ex=lines.get(i).getEndPoint().x;
			float ey=lines.get(i).getEndPoint().y;		
			canvas.drawLine(sx, sy, ax, ay, mPaint);
			canvas.drawLine(ax, ay, ex, ey, mPaint);
		}
	}
	
	public void Angle_touch_down(float x,float y){
		continuousPoints=new ArrayList<PointF>();
		start_x=x;
		start_y=y;
		// 每次down下去重新new一个Path 
		mPath=new Path();
		mPath.reset();
		mPath.moveTo(x, y);
		mX=x;
		mY=y;		
		
	}
	public void Angle_touch_move(float x,float y){
		continuousPoints.add(new PointF(x, y));
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        //触摸间隔大于阈值才绘制路径
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	// 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }    
	}
	public void Angle_touch_up(float x,float y,Canvas canvas){

		angle_x=continuousPoints.get(continuousPoints.size()/2).x;
		angle_y=continuousPoints.get(continuousPoints.size()/2).y;
		end_x=x;
		end_y=y;			
		DrawAngle(canvas);	
		AddAngleParams(getAnglelist, start_x, start_y, angle_x, angle_y, end_x, end_y);
		mPath = null;// 重新置空	
	}
	public void MoveAngle_down(List<AngleLineBean> lines,int i,float rx,float ry){
		start_mx=lines.get(i).getStartPoint().x;
		start_my=lines.get(i).getStartPoint().y;
		end_mx=lines.get(i).getEndPoint().x;
		end_my=lines.get(i).getEndPoint().y;
		angle_mx=lines.get(i).getAnglePoint().x;
		angle_my=lines.get(i).getAnglePoint().y;
		movePoint.set(rx, ry);
		lines.remove(i);
	}
	
	public void MoveAngle_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	    	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	start_mx=start_mx+dx;
	    	start_my=start_my+dy;
	    	end_mx=end_mx+dx;
	    	end_my=end_my+dy; 	
	    	angle_mx=angle_mx+dx;
	    	angle_my=angle_my+dy;
	    }
		
	}
	public void MoveAngle_up(Canvas canvas){	    
		start_mx= ViewContans.AdsorbPoint((int)Math.floor(start_mx));
		start_my= ViewContans.AdsorbPoint((int)Math.floor(start_my));
		end_mx= ViewContans.AdsorbPoint((int)Math.floor(end_mx));
		end_my= ViewContans.AdsorbPoint((int)Math.floor(end_my)); 
		angle_mx= ViewContans.AdsorbPoint((int)Math.floor(angle_mx));
		angle_my= ViewContans.AdsorbPoint((int)Math.floor(angle_my)); 
		MoveDrawAngleOnBitmap(canvas);
		AddAngleParams(getAnglelist, start_mx, start_my, angle_mx,
				angle_my, end_mx, end_my);
	}
	public void MoveAngle_camera_up(Canvas canvas){	    
		MoveDrawAngleOnBitmap(canvas);
		AddAngleParams(getAnglelist, start_mx, start_my, angle_mx,
				angle_my, end_mx, end_my);
	}
	public void AddAngleParams(List<AngleLineBean> lines,float sx,
			float sy,float ax,float ay,float ex,float ey){
		AngleLineBean angleLineBean=new AngleLineBean(new PointF(sx, sy),
				new PointF(ex, ey), new PointF(ax, ay));
		angleLineBean.setFull(true);
		angleLineBean.setWidth(4);
		angleLineBean.setName("angle");
		angleLineBean.setDescripte("描述");
		lines.add(angleLineBean);
	}
	public int PitchOnAngle(List<AngleLineBean> lines,float x,float y){
		PointF pos=new PointF(x, y);
		for (int i = 0; i < lines.size(); i++) {
			if (inTriangle(pos, lines.get(i).getStartPoint(),
					lines.get(i).getAnglePoint(), lines.get(i).getEndPoint())) {
				return i;
			}
		}		
		return -1;
	}
	public boolean ExtandAngle(List<AngleLineBean> lines,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < lines.size(); i++) {
			angle_x=lines.get(i).getAnglePoint().x;
			angle_y=lines.get(i).getAnglePoint().y;
			int sx=(int) lines.get(i).getStartPoint().x;
			int sy=(int) lines.get(i).getStartPoint().y;
			int ex=(int) lines.get(i).getEndPoint().x;
			int ey=(int) lines.get(i).getEndPoint().y;
			boolean isStart=((sx-40<dx)&&(sx+40<dx))&&((sy-40<dy)&&(sy+40>dy));
			boolean isStop=((ex-40<dx)&&(ex+40<dx))&&((ey-40<dy)&&(ey+40>dy));
			if (isStart) {
				end_x=(float)ex;
				end_y=(float)ey;
				start_x=x;
				start_y=y;
				lines.remove(i);
				return true;
			}else if (isStop) {
				start_x=(float)sx;
				start_y=(float)sy;
				end_x=x;
				end_y=y;
				lines.remove(i);
				return true;
			}
		}
		return false;
	}
	// 判断点pos是否在指定的三角形内。
	private boolean inTriangle(PointF pos, PointF posA, PointF posB,
			PointF posC) {
	    double triangleArea = triangleArea(posA, posB, posC);
	    double area = triangleArea(pos, posA, posB);
	    area += triangleArea(pos, posA, posC);
	    area += triangleArea(pos, posB, posC);
	    double epsilon = 2;  // 由于浮点数的计算存在着误差，故指定一个足够小的数，用于判定两个面积是否(近似)相等。
	    if (Math.abs(triangleArea - area) < epsilon) {
	        return true;
	    }
	    return false;
	}


	// 由给定的三个顶点的坐标，计算三角形面积。
	// Point(java.awt.Point)代表点的坐标。
	private double triangleArea(PointF pos1, PointF pos2, PointF pos3) {
	    double result = Math.abs((pos1.x * pos2.y + pos2.x * pos3.y + pos3.x * pos1.y
	            - pos2.x * pos1.y - pos3.x * pos2.y - pos1.x * pos3.y) / 2.0D);
	    return result;
	}
	 

	
}
