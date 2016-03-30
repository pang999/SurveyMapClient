package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

public class AngleModel {
	
	//连续线段获取坐标
    private List<PointF> continuousPoints;

    
	Path mPath;
	private Paint mPaint=ViewContans.generatePaint(Color.RED, 4,true);
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10,true);
	private float mX, mY;// 临时点坐标
	private static final float TOUCH_TOLERANCE = 4;
	float start_x,start_y,end_x,end_y,angle_x,angle_y;
	float start_mx,start_my,end_mx,end_my,angle_mx,angle_my;
	public List<AngleBean> getAnglelist=new ArrayList<AngleBean>();
	private AngleBean angleBean=new AngleBean();
	private PointF movePoint=new PointF();
	private int BorderType=-1;
	private static final int startborder=0;
	private static final int endborder=1;
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

	public void DrawAngleOnBitmap(List<AngleBean> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			float sx=list.get(i).getStartX();
			float sy=list.get(i).getStartY();
			float ax=list.get(i).getAngleX();
			float ay=list.get(i).getAngleY();
			float ex=list.get(i).getEndX();
			float ey=list.get(i).getEndY();
			Paint paint=ViewContans.generatePaint(list.get(i).getPaintColor(), list.get(i).getPaintWidth(), list.get(i).isPaintIsFull());
			canvas.drawLine(sx, sy, ax, ay, paint);
			canvas.drawLine(ax, ay, ex, ey, paint);
			if (list.get(i).getAngle()>0) {
				ViewContans.AddTextOnAngle(list.get(i), canvas, list.get(i).getAngle()+"°");
			}
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
		boolean isOne=Math.abs(start_x-angle_x)>20;
		boolean isTwo=Math.abs(end_x-angle_x)>20;
		if (isOne&&isTwo) {			
			end_x=x;
			end_y=y;			
			DrawAngle(canvas);	
			AddAngleParams(getAnglelist, start_x, start_y, angle_x, angle_y, end_x, end_y);
			Logger.i("Angle总数", "angle="+getAnglelist.size());
		}
		mPath = null;// 重新置空	
	}
	
	public void MoveAngle_down(List<AngleBean> list,int i,float rx,float ry){
		start_mx=list.get(i).getStartX();
		start_my=list.get(i).getStartY();
		end_mx=list.get(i).getEndX();
		end_my=list.get(i).getEndY();
		angle_mx=list.get(i).getAngleX();
		angle_my=list.get(i).getAngleY();
		angleBean.setName(list.get(i).getName());
		angleBean.setDescripte(list.get(i).getDescripte());
		angleBean.setPaintColor(list.get(i).getPaintColor());
		angleBean.setPaintWidth(list.get(i).getPaintWidth());
		angleBean.setPaintIsFull(list.get(i).isPaintIsFull());
		angleBean.setAngle(list.get(i).getAngle());
		movePoint.set(rx, ry);
		list.remove(i);
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
		angleBean.setStartX(start_mx);
		angleBean.setStartY(start_my);
		angleBean.setEndX(end_mx);
		angleBean.setEndY(end_my);
		angleBean.setAngleX(angle_mx);
		angleBean.setAngleY(angle_my);
		Paint paint=ViewContans.generatePaint(angleBean.getPaintColor(), angleBean.getPaintWidth(), angleBean.isPaintIsFull());
		canvas.drawLine(start_mx, start_my, angle_mx, angle_my, paint);
		canvas.drawLine(angle_mx, angle_my, end_mx, end_my, paint);
		if (angleBean.getAngle()>0) {
			ViewContans.AddTextOnAngle(angleBean, canvas, angleBean.getAngle()+"°");
		}
		AddChangeAngleAttributeParams(getAnglelist, start_mx, start_my, end_mx, end_my, 
				angle_mx,angle_my, angleBean.getName(),angleBean.getDescripte(), angleBean.getAngle(), angleBean.getPaintColor(), 
				angleBean.getPaintWidth(), angleBean.isPaintIsFull());
	}
	public void MoveAngle_camera_up(Canvas canvas){	 
		angleBean.setStartX(start_mx);
		angleBean.setStartY(start_my);
		angleBean.setEndX(end_mx);
		angleBean.setEndY(end_my);
		angleBean.setAngleX(angle_mx);
		angleBean.setAngleY(angle_my);
		Paint paint=ViewContans.generatePaint(angleBean.getPaintColor(), angleBean.getPaintWidth(), angleBean.isPaintIsFull());
		canvas.drawLine(start_mx, start_my, angle_mx, angle_my, paint);
		canvas.drawLine(angle_mx, angle_my, end_mx, end_my, paint);
		if (angleBean.getAngle()>0) {
			ViewContans.AddTextOnAngle(angleBean, canvas, angleBean.getAngle()+"°");
		}
		AddChangeAngleAttributeParams(getAnglelist, start_mx, start_my, end_mx, end_my, 
				angle_mx,angle_my, angleBean.getName(),angleBean.getDescripte(), angleBean.getAngle(), angleBean.getPaintColor(), 
				angleBean.getPaintWidth(), angleBean.isPaintIsFull());
	}
	public void AddAngleParams(List<AngleBean> list,float sx,
			float sy,float ax,float ay,float ex,float ey){
		AngleBean angle=new AngleBean();
		angle.setStartX(sx);
		angle.setStartY(sy);
		angle.setAngleX(ax);
		angle.setAngleY(ay);
		angle.setEndX(ex);
		angle.setEndY(ey);
		angle.setPaintIsFull(true);
		angle.setPaintWidth(4);
		angle.setPaintColor(Color.RED);
		angle.setName("angle-"+list.size());
		angle.setAngle(0.0);
		angle.setDescripte("描述"+list.size());
		list.add(angle);
	}
	public void AddChangeAngleAttributeParams(List<AngleBean> list,
			float sx,float sy,float ex,float ey,float ax,float ay,String name,String desc,double a,
			int paintcolor,float paintwidth,boolean isfull){
		AngleBean angle=new AngleBean();
		angle.setStartX(sx);
		angle.setStartY(sy);
		angle.setAngleX(ax);
		angle.setAngleY(ay);
		angle.setEndX(ex);
		angle.setEndY(ey);
		angle.setPaintIsFull(isfull);
		angle.setPaintWidth(paintwidth);
		angle.setPaintColor(paintcolor);
		angle.setName(name);
		angle.setAngle(a);
		angle.setDescripte(desc);
		list.add(angle);
	}
	public void ChangeAngleBeanAttributeAtIndex(List<AngleBean> list,int index,Canvas canvas,AngleBean cangle){
		float sx=list.get(index).getStartX();
		float sy=list.get(index).getStartY();
		float ax=list.get(index).getAngleX();
		float ay=list.get(index).getAngleY();
		float ex=list.get(index).getEndX();
		float ey=list.get(index).getEndY();
		Paint paint=ViewContans.generatePaint(cangle.getPaintColor(), cangle.getPaintWidth(), cangle.isPaintIsFull());
		canvas.drawLine(sx, sy, ax, ay, paint);
		canvas.drawLine(ax, ay, ex, ey, paint);
		if (cangle.getAngle()>0) {
			ViewContans.AddTextOnAngle(list.get(index), canvas, cangle.getAngle()+"°");
		}
		list.remove(index);
		AddChangeAngleAttributeParams(list, sx, sy, ex, ey, ax, ay, cangle.getName(),
				cangle.getDescripte(), cangle.getAngle(), cangle.getPaintColor(), 
				cangle.getPaintWidth(), cangle.isPaintIsFull());
	}
	public void ExtandAngleBorder_touch_move(float x,float y){
		if (BorderType==startborder) {
			start_x=x;
			start_y=y;
		}else if (BorderType==endborder) {
			end_x=x;
			end_y=y;
		}
	}
	public void ExtandAngle_touch_up(Canvas canvas){
		boolean isOne=Math.abs(start_x-angle_x)>20;
		boolean isTwo=Math.abs(end_x-angle_x)>20;
		if (isOne&&isTwo) {						
			DrawAngle(canvas);	
			AddAngleParams(getAnglelist, start_x, start_y, angle_x, angle_y, end_x, end_y);
			Logger.i("Angle总数", "angle="+getAnglelist.size());
		}
	}
	public int PitchOnAngle(List<AngleBean> list,float x,float y){
		PointF pos=new PointF(x, y);
		for (int i = 0; i < list.size(); i++) {
			if (ViewContans.inTriangle(pos, new PointF(list.get(i).getStartX(), list.get(i).getStartY()),
					new PointF(list.get(i).getAngleX(),list.get(i).getAngleY()),
					new PointF(list.get(i).getEndX(), list.get(i).getEndY()))) {
				return i;
			}
		}		
		return -1;
	}
	public boolean ExtandAngle(List<AngleBean> lines,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < lines.size(); i++) {
			angle_x=lines.get(i).getAngleX();
			angle_y=lines.get(i).getAngleY();
			int sx=(int) lines.get(i).getStartX();
			int sy=(int) lines.get(i).getStartY();
			int ex=(int) lines.get(i).getEndX();
			int ey=(int) lines.get(i).getEndY();
			boolean isStart=((sx-40<dx)&&(sx+40>dx))&&((sy-40<dy)&&(sy+40>dy));
			boolean isStop=((ex-40<dx)&&(ex+40>dx))&&((ey-40<dy)&&(ey+40>dy));
			if (isStart) {
				end_x=(float)ex;
				end_y=(float)ey;
				start_x=x;
				start_y=y;
				BorderType=startborder;
				lines.remove(i);
				return true;
			}else if (isStop) {
				start_x=(float)sx;
				start_y=(float)sy;
				end_x=x;
				end_y=y;
				BorderType=endborder;
				lines.remove(i);
				return true;
			}
		}
		return false;
	}


	
}
