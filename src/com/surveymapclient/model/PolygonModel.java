package com.surveymapclient.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Looper;
import android.provider.MediaStore.Video;

public class PolygonModel {
	
	//连续线段获取坐标
    private List<PointF> continuousPoints=new ArrayList<PointF>();
    //连续线段获得终止画点
    private List<PointF> linePoint=new ArrayList<PointF>();
    //连续线段获得条数
    private List<LineBean> continuouslist=new ArrayList<LineBean>();
    private float mX, mY;// 临时点坐标
    private static final float TOUCH_TOLERANCE = 4;
    public List<PolygonBean> GetpolyList=new ArrayList<PolygonBean>();
    private PolygonBean drawpolygon=new PolygonBean();
    //每隔10像素取点
  	private static int space=10;
  	private PointF movePoint=new PointF();
	private Path mPath;
	
	//直线两头端点画点
	private Paint point=ViewContans.generatePaint(Color.RED, 18,true);
	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.BLACK, 6, false);
	//画图过程画笔
	private Paint painting=ViewContans.generatePaint(Color.RED, 4,true);
	//选中画笔
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10,true);
	//画阴影
    Path mshadowpath; 
    
    private Paint generatePaint(int color){
		Paint p = new Paint();
        p.setAntiAlias(true);
        p.setDither(true);
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(10); 
    	p.setAlpha(0x40); 
    	return p;
	}	
	public void DrawPath(Canvas canvas){
		if (mPath!=null) {
			canvas.drawPath(mPath, painting);
		}
	}
	public void DrawPolygonOnBitmap(Canvas canvas){
		mshadowpath=new Path();
   		mshadowpath.reset();
   		List<LineBean> lineBeans=GetpolyList.get(GetpolyList.size()-1).getPolyLine();
   		for (int j = 0;j < lineBeans.size(); j++) {
			float sx=lineBeans.get(j).getStartX();
			float sy=lineBeans.get(j).getStartY();
			float ex=lineBeans.get(j).getEndX();
			float ey=lineBeans.get(j).getEndY();
			canvas.drawLine(sx, sy, ex, ey, mPaint);
			canvas.drawPoint(sx, sy, point);
			canvas.drawPoint(ex, ey, point);
			if (j==0) {
				mshadowpath.moveTo(lineBeans.get(0).getStartX(), lineBeans.get(0).getStartY());
			}else {
				mshadowpath.quadTo(lineBeans.get(j-1).getEndX(), lineBeans.get(j-1).getEndY(),
						lineBeans.get(j).getStartX(), lineBeans.get(j).getStartY());
			}
		}
		canvas.drawPath(mshadowpath, generatePaint(Color.RED));
		mshadowpath=null;
	}
	public void DrawPolygonsOnBitmap(List<PolygonBean> polylist,Canvas canvas){
	   	for (int i = 0; i < polylist.size(); i++) {
	   		mshadowpath=new Path();
	   		mshadowpath.reset();
	   		Logger.i("多边形次数", "i="+i);
	   		for (int j = 0;j < polylist.get(i).getPolyLine().size(); j++) {
				float sx=polylist.get(i).getPolyLine().get(j).getStartX();
				float sy=polylist.get(i).getPolyLine().get(j).getStartY();
				float ex=polylist.get(i).getPolyLine().get(j).getEndX();
				float ey=polylist.get(i).getPolyLine().get(j).getEndY();
				canvas.drawLine(sx, sy, ex, ey, mPaint);
				canvas.drawPoint(sx, sy, point);
				canvas.drawPoint(ex, ey, point);
				if (j==0) {
					mshadowpath.moveTo(polylist.get(i).getPolyLine().get(0).getStartX(), polylist.get(i).getPolyLine().get(0).getStartY());
				}else {
					mshadowpath.quadTo(polylist.get(i).getPolyLine().get(j-1).getEndX(), polylist.get(i).getPolyLine().get(j-1).getEndY(),
							polylist.get(i).getPolyLine().get(j).getStartX(), polylist.get(i).getPolyLine().get(j).getStartY());
				}
			}
			Logger.i("多边形次数", "i="+i);
			canvas.drawPath(mshadowpath, generatePaint(Color.RED));
			mshadowpath=null;
		}
	}
	
	public void Continuous_touch_down(float x,float y){
		// 每次down下去重新new一个Path 
		mPath=new Path();
		continuousPoints.clear();
		linePoint.clear();
		continuouslist.clear();
		continuousPoints.add(new PointF(x, y));
		mPath.reset();
		mPath.moveTo(x, y);
		mX=x;
		mY=y;	
	}
	
	public void Continuous_touch_move(float x,float y){
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
	public void Continuous_touch_up(float x,float y,Canvas canvas){
		drawpolygon(0, 0);
		int count =linePoint.size();
		for (int i = 0; i < count; i++) {
			if (i==count-1) {
				GetLines(continuouslist,ViewContans.AdsorbPoint((int)linePoint.get(i).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i).y), ViewContans.AdsorbPoint((int)linePoint.get(0).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(0).y));
			}else {
				GetLines(continuouslist,ViewContans.AdsorbPoint((int)linePoint.get(i).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i).y), ViewContans.AdsorbPoint((int)linePoint.get(i+1).x), 
						ViewContans.AdsorbPoint((int)linePoint.get(i+1).y));
			}	
			
		}
		for (int i = 0; i < continuouslist.size(); i++) {
        	float sx=continuouslist.get(i).getStartX();
        	float sy=continuouslist.get(i).getStartY();
        	float ex=continuouslist.get(i).getEndX();
        	float ey=continuouslist.get(i).getEndY();
        	if (sx==ex&&sy==ey) {
        		continuouslist.remove(i);
			}	
		}	
		if (continuouslist.size()>=3) {
			AddPolygonParams(GetpolyList, continuouslist);
			DrawPolygonOnBitmap(canvas);
		}		
		Logger.i("多边形总数", "GetpolyList="+GetpolyList.size());
		for (int i = 0; i < GetpolyList.size(); i++) {
			Logger.i("多边形总数", "lines("+i+")="+GetpolyList.get(i).getPolyLine().size());
			Logger.i("多边形总数", "描述("+i+")="+GetpolyList.get(i).getDescripe());
		}	
        mPath = null;// 重新置空	
		
	}
	List<LineBean> lineBeans=new ArrayList<LineBean>();
	public void MovePolygon_down(List<PolygonBean> polylist,int i,float x,float y){		
		Logger.i("多边形移动", "down--按下--polylist="+polylist.size());
		lineBeans.clear();
		for (int j = 0; j < polylist.get(i).getPolyLine().size(); j++) {
			LineBean lineBean=new LineBean();
			float sx=polylist.get(i).getPolyLine().get(j).getStartX();
			float sy=polylist.get(i).getPolyLine().get(j).getStartY();
			float ex=polylist.get(i).getPolyLine().get(j).getEndX();
			float ey=polylist.get(i).getPolyLine().get(j).getEndY();			
			lineBean.setStartX(sx);
			lineBean.setStartY(sy);
			lineBean.setEndX(ex);
			lineBean.setEndY(ey);
			lineBeans.add(lineBean);
		}
		drawpolygon.setPolyLine(lineBeans);
		movePoint.set(x, y);
		Logger.i("多边形移动", "down--按下--lines="+lineBeans.size());		
		polylist.remove(i);
	}
	List<LineBean> list=new ArrayList<LineBean>();
	LineBean lineBean=new LineBean();
	public void MovePolygon_move(float rx,float ry){
		
		list.clear();
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	
	    Logger.i("多边形移动", "move---->"+drawpolygon.getPolyLine().size());
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {	    	
	    	movePoint.set(rx, ry);
	    	for (int i = 0; i < drawpolygon.getPolyLine().size(); i++) {
	    		float sx=drawpolygon.getPolyLine().get(i).getStartX()+dx;
				float sy=drawpolygon.getPolyLine().get(i).getStartY()+dy;
				float ex=drawpolygon.getPolyLine().get(i).getEndX()+dx;
				float ey=drawpolygon.getPolyLine().get(i).getEndY()+dy;			
				lineBean.setStartX(sx);
				lineBean.setStartY(sy);
				lineBean.setEndX(ex);
				lineBean.setEndY(ey);
				list.add(lineBean);
			}	
	    	drawpolygon.setPolyLine(list);	    	
	    }
	    Logger.i("多边形移动", "move---->"+drawpolygon.getPolyLine().size());
	}
	public void MovePolygon_up(Canvas canvas){
	    Logger.i("多边形移动", "up---->"+drawpolygon.getPolyLine().size());

		list.clear();
		for (int i = 0; i < drawpolygon.getPolyLine().size(); i++) {
    		float sx=drawpolygon.getPolyLine().get(i).getStartX();
			float sy=drawpolygon.getPolyLine().get(i).getStartY();
			float ex=drawpolygon.getPolyLine().get(i).getEndX();
			float ey=drawpolygon.getPolyLine().get(i).getEndY();
			sx= ViewContans.AdsorbPoint((int)Math.floor(sx));
			sy= ViewContans.AdsorbPoint((int)Math.floor(sy));
			ex= ViewContans.AdsorbPoint((int)Math.floor(ex));
			ey= ViewContans.AdsorbPoint((int)Math.floor(ey)); 
			lineBean.setStartX(sx);
			lineBean.setStartY(sy);
			lineBean.setEndX(ex);
			lineBean.setEndY(ey);
			list.add(lineBean);
		}
    	drawpolygon.setPolyLine(list);
    	GetpolyList.add(drawpolygon);
    	DrawPolygonOnBitmap(canvas);
	}
	public void MoveDrawPolygon(Canvas canvas){	
		mshadowpath=new Path();
		Logger.i("多边形移动", "down---->lines="+drawpolygon.getPolyLine().size());		
		for (int i = 0; i < drawpolygon.getPolyLine().size(); i++) {
			Logger.i("多边形移动", "i="+i);
			float sx=drawpolygon.getPolyLine().get(i).getStartX();
			float sy=drawpolygon.getPolyLine().get(i).getStartY();
			float ex=drawpolygon.getPolyLine().get(i).getEndX();
			float ey=drawpolygon.getPolyLine().get(i).getEndY();
			canvas.drawLine(sx, sy, ex, ey, checkpaint);
			canvas.drawPoint(sx, sy, point);
			canvas.drawPoint(ex, ey, point);
			if (i==0) {
				mshadowpath.moveTo(drawpolygon.getPolyLine().get(0).getStartX(), drawpolygon.getPolyLine().get(0).getStartY());
			}else {
				mshadowpath.quadTo(drawpolygon.getPolyLine().get(i-1).getEndX(), drawpolygon.getPolyLine().get(i-1).getEndY(),
						drawpolygon.getPolyLine().get(i).getStartX(), drawpolygon.getPolyLine().get(i).getStartY());
			}
		}
		canvas.drawPath(mshadowpath, generatePaint(Color.BLUE));
		mshadowpath=null;
	}
	public void Continuous_camera_touch_up(float x,float y,Canvas canvas){
		
		drawpolygon(0, 0);
		int count =linePoint.size();
		for (int i = 0; i < count; i++) {
			if (i==count-1) {
				GetLines(continuouslist,linePoint.get(i).x, 
						linePoint.get(i).y, linePoint.get(0).x, 
						linePoint.get(0).y);
			}else {
				GetLines(continuouslist,linePoint.get(i).x, 
						linePoint.get(i).y, linePoint.get(i+1).x, 
						linePoint.get(i+1).y);
			}		
		}
		for (int i = 0; i < continuouslist.size(); i++) {
        	float sx=continuouslist.get(i).getStartX();
        	float sy=continuouslist.get(i).getStartY();
        	float ex=continuouslist.get(i).getEndX();
        	float ey=continuouslist.get(i).getEndY();
        	if (sx==ex&&sy==ey) {
        		continuouslist.remove(i);
			}	
		}
		
		AddPolygonParams(GetpolyList,continuouslist);
		DrawPolygonOnBitmap(canvas);
        mPath = null;// 重新置空	
		
	}
	private List<PointF> points=new ArrayList<PointF>();
	
	public int PitchOnPolygon(List<PolygonBean> list,float x,float y){		
	    int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < list.size(); i++) {	    	
			double area=0;
			points.clear();
	   		for (int j = 0;j < list.get(i).getPolyLine().size(); j++) {
	   			Logger.i("For循环", "i="+i+",j="+j);
				int sx=(int) list.get(i).getPolyLine().get(j).getStartX();
				int sy=(int) list.get(i).getPolyLine().get(j).getStartY();
				int ex=(int) list.get(i).getPolyLine().get(j).getEndX();
				int ey=(int) list.get(i).getPolyLine().get(j).getEndY();
				points.add(new PointF(sx, sy));
				area+=ViewContans.triangleArea(new PointF(dx, dy),
						new PointF(sx, sy), new PointF(ex, ey));				
			}
	   		Logger.i("选中多边形", "点面积="+area);
	   		Logger.i("选中多边形", "原面积="+Math.abs(ViewContans.getArea(points)));
	   		if (Math.abs(Math.abs(ViewContans.getArea(points))-area)<2) {
				return i;
			}
		}
	    return -1;
	}
	private void drawpolygon(int i,int j){		
		List<PointF> currePoint=new ArrayList<PointF>();
		try {
			currePoint.add(continuousPoints.get(i));
			currePoint.add(continuousPoints.get(space*(j+1) + i));
			currePoint.add(continuousPoints.get(space*(j+1) + space + i));
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			return;
		}
		if (Angle(currePoint)<150) {
			linePoint.add(continuousPoints.get(i));
			linePoint.add(continuousPoints.get(space*(j+1) + i));
			linePoint.add(continuousPoints.get(space*(j+1) + space + i));
			drawpolygon((space*(j+1) + space + i), 0);
		}else {
			drawpolygon(i, j+1);
		}			
	}	
	public void GetLines(List<LineBean> lists,float sx,float sy,float ex,float ey){
		LineBean line=new LineBean();
		line.setStartX(sx);
		line.setStartY(sy);
		line.setEndX(ex);
		line.setEndY(ey);
		line.setAngle(0.00);
		line.setPaintColor(Color.BLACK);
		line.setLength(0);
		line.setName("polylineName-"+lists.size());
		line.setPaintIsFull(false);
		line.setPaintWidth(4);
		line.setDescripte("描述"+lists.size());
		lists.add(line);
	}
	public void AddPolygonParams(List<PolygonBean> polylist,List<LineBean> linelist){
		PolygonBean poly=new PolygonBean();
		List<LineBean> lineslist=new ArrayList<LineBean>();
		for (int i = 0; i <linelist.size(); i++) {
			LineBean lineBean=new LineBean();
			lineBean.setStartX(linelist.get(i).getStartX());
			lineBean.setStartY(linelist.get(i).getStartY());
			lineBean.setEndX(linelist.get(i).getEndX());
			lineBean.setEndY(linelist.get(i).getEndY());
			lineslist.add(lineBean);			
		}
		poly.setPolyName("polyName-"+polylist.size());
		poly.setPolyArea(0);
		poly.setPolyColor(Color.RED);
		poly.setPolyLine(lineslist);
		Logger.i("多边形总数", "linelist="+lineslist.size());
		poly.setDescripe("描述"+lineslist.size());
		polylist.add(poly);
	}
	public static double Angle(List<PointF> points){		
		double a=distance(points.get(0), points.get(1));
		double b=distance(points.get(1), points.get(2));
		double c=distance(points.get(2), points.get(0));
		// 计算弧度表示的角
		double A=Math.acos((b * b + a * a - c * c) / (2.0 * b * a));
		A=Math.toDegrees(A);
		// 格式化数据，保留两位小数
		return A;
	}
	
	private static double distance(PointF p1,PointF p2){
		float x = p1.x - p2.x;  
	    float y = p1.y - p2.y;  
	    return Math.sqrt(x * x + y * y);  		
	}
	
	
}
