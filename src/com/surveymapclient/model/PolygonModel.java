package com.surveymapclient.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

<<<<<<< HEAD
import com.surveymapclient.common.IToast;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
//    private PolygonBean drawpolygon=new PolygonBean();
=======
    private PolygonBean drawpolygon=new PolygonBean();
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
    List<LineBean> movelineslist;
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
    
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
<<<<<<< HEAD
	public void MovePolygon_down(List<PolygonBean> polylist,int i,float rx,float ry){		
		Logger.i("多边形移动", "down--按下--polylist="+polylist.size());
		movelineslist=polylist.get(i).getPolyLine();
		movePoint.set(rx, ry);
		polylist.remove(i);
	}
	
	public void MovePolygon_move(float rx,float ry){		
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {	    	
	    	movePoint.set(rx, ry);
	    	for (int i = 0; i < movelineslist.size(); i++) {
	    		LineBean lineBean=new LineBean();
	    		float sx=movelineslist.get(i).getStartX()+dx;
				float sy=movelineslist.get(i).getStartY()+dy;
				float ex=movelineslist.get(i).getEndX()+dx;
				float ey=movelineslist.get(i).getEndY()+dy;						
=======
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
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
				lineBean.setStartX(sx);
				lineBean.setStartY(sy);
				lineBean.setEndX(ex);
				lineBean.setEndY(ey);
<<<<<<< HEAD
				movelineslist.set(i, lineBean);
			}	
	    }
	}
	public void MovePolygon_up(Canvas canvas){
		for (int i = 0; i < movelineslist.size(); i++) {
			LineBean lineBean=new LineBean();
    		float sx=movelineslist.get(i).getStartX();
			float sy=movelineslist.get(i).getStartY();
			float ex=movelineslist.get(i).getEndX();
			float ey=movelineslist.get(i).getEndY();
=======
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
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			sx= ViewContans.AdsorbPoint((int)Math.floor(sx));
			sy= ViewContans.AdsorbPoint((int)Math.floor(sy));
			ex= ViewContans.AdsorbPoint((int)Math.floor(ex));
			ey= ViewContans.AdsorbPoint((int)Math.floor(ey)); 
			lineBean.setStartX(sx);
			lineBean.setStartY(sy);
			lineBean.setEndX(ex);
			lineBean.setEndY(ey);
<<<<<<< HEAD
			movelineslist.set(i, lineBean);
		}
		AddPolygonParams(GetpolyList, movelineslist);
    	DrawPolygonOnBitmap(canvas);
	}
	
	public void MoveDrawPolygon(Canvas canvas){	
		mshadowpath=new Path();
//		Logger.i("多边形移动", "down---->lines="+drawpolygon.getPolyLine().size());		
		for (int i = 0; i < movelineslist.size(); i++) {
			Logger.i("多边形移动", "i="+i);
			float sx=movelineslist.get(i).getStartX();
			float sy=movelineslist.get(i).getStartY();
			float ex=movelineslist.get(i).getEndX();
			float ey=movelineslist.get(i).getEndY();
=======
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
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			canvas.drawLine(sx, sy, ex, ey, checkpaint);
			canvas.drawPoint(sx, sy, point);
			canvas.drawPoint(ex, ey, point);
			if (i==0) {
<<<<<<< HEAD
				mshadowpath.moveTo(movelineslist.get(0).getStartX(),movelineslist.get(0).getStartY());
			}else {
				mshadowpath.quadTo(movelineslist.get(i-1).getEndX(), movelineslist.get(i-1).getEndY(),
						movelineslist.get(i).getStartX(), movelineslist.get(i).getStartY());
			}
		}
		canvas.drawPath(mshadowpath, generatePaint(Color.BLUE));
		mshadowpath=null;
	}
	public void ExtendDrawPolygon(Canvas canvas){	
		mshadowpath=new Path();
//		Logger.i("多边形移动", "down---->lines="+drawpolygon.getPolyLine().size());		
		for (int i = 0; i < movelineslist.size(); i++) {
			Logger.i("多边形移动", "i="+i);
			float sx=movelineslist.get(i).getStartX();
			float sy=movelineslist.get(i).getStartY();
			float ex=movelineslist.get(i).getEndX();
			float ey=movelineslist.get(i).getEndY();
			canvas.drawLine(sx, sy, ex, ey, painting);
			canvas.drawPoint(sx, sy, point);
			canvas.drawPoint(ex, ey, point);
			if (i==0) {
				mshadowpath.moveTo(movelineslist.get(0).getStartX(),movelineslist.get(0).getStartY());
			}else {
				mshadowpath.quadTo(movelineslist.get(i-1).getEndX(), movelineslist.get(i-1).getEndY(),
						movelineslist.get(i).getStartX(), movelineslist.get(i).getStartY());
=======
				mshadowpath.moveTo(drawpolygon.getPolyLine().get(0).getStartX(), drawpolygon.getPolyLine().get(0).getStartY());
			}else {
				mshadowpath.quadTo(drawpolygon.getPolyLine().get(i-1).getEndX(), drawpolygon.getPolyLine().get(i-1).getEndY(),
						drawpolygon.getPolyLine().get(i).getStartX(), drawpolygon.getPolyLine().get(i).getStartY());
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
	private List<PointF> points=new ArrayList<PointF>();	
=======
	private List<PointF> points=new ArrayList<PointF>();
	
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
	public int PitchOnPolygonLine(List<PolygonBean> list,float x,float y){
		 int dx=(int) x;
		    int dy=(int) y;
		    for (int i = 0; i < list.size(); i++) {	    	
		   		for (int j = 0;j < list.get(i).getPolyLine().size(); j++) {
		   			Logger.i("For循环", "i="+i+",j="+j);
					int gspx=(int) list.get(i).getPolyLine().get(j).getStartX();
					int gspy=(int) list.get(i).getPolyLine().get(j).getStartY();
					int gepx=(int) list.get(i).getPolyLine().get(j).getEndX();
					int gepy=(int) list.get(i).getPolyLine().get(j).getEndY();
					Logger.i("起点终点", "---->L"+i+": ("+gspx+","+gspy+")  ,  ("+gepx+","+gepy+")");
					boolean xCan=(gspx<dx&&dx<gepx)||(gspx>dx&&dx>gepx);
					boolean xx=(gspx==gepx);
					boolean yCan=(gspy<dy&&dy<gepy)||(gspy>dy&&dy>gepy);
					boolean yy=(gspy==gepy);
					if ((xCan&&yCan)||xx||yy) {
						int k_fz=gepy-gspy;
						int k_fm=gepx-gspx;
						if (k_fz==0) {
							int cy=Math.abs(dy-gepy);
							if (cy<40&&xCan) {
								Logger.i("选中多边形的边", "j="+j);
								return j;						
							}
						}
						else if (k_fm==0) {
							int cx=Math.abs(dx-gepx);
							if (cx<40&&yCan) {
								Logger.i("选中多边形的边", "j="+j);
								return j;
							}
						}else {
							double val=((double)k_fz)/k_fm;
				    		BigDecimal bd =new BigDecimal(val);
				    		//保留2位小数
				    		double k = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 		    		
				    		int b_fz1=gspy*gepx;
				    		int b_fz2=gepy*gspx;
				    		int b_fz=b_fz1-b_fz2;
				    		int b_fm=gepx-gspx;
				    		double d=((double)b_fz)/b_fm;
				    		BigDecimal bdb =new BigDecimal(d);
				    		//保留2位小数
				    		double b = bdb.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 
				    		double gety=k*dx+b;
				    		double getx=(dy-b)/k;
				    		int cx=dx-(int)getx;
				    		int cy=dy-(int)gety;
				    		if (Math.abs(cx)<40&&Math.abs(cy)<40) {
				    			Logger.i("选中多边形的边", "j="+j);
				    			return j;
							}
				    		
						}
					}			
				}
			}
		return -1;
	}
	public void ExtendPolygonLine(float x,float y){
		LineBean line1=new LineBean();
		LineBean line2=new LineBean();
		line1.setStartX(movelineslist.get(m).getStartX());
		line1.setStartY(movelineslist.get(m).getStartY());
		line1.setEndX(x);
		line1.setEndY(y);
		line2.setStartX(x);
		line2.setStartY(y);
		if (m==movelineslist.size()-1) {
			line2.setEndX(movelineslist.get(0).getEndX());
			line2.setEndY(movelineslist.get(0).getEndY());
			movelineslist.set(m, line1);
			movelineslist.set(0, line2);
		}else {
			line2.setEndX(movelineslist.get(m+1).getEndX());
			line2.setEndY(movelineslist.get(m+1).getEndY());
			movelineslist.set(m, line1);
			movelineslist.set(m+1, line2);
		}
	}
	int m;
	public boolean ExtendPolygon(List<PolygonBean> list,float x,float y){	
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < list.size(); i++) {
			List<LineBean> lineBeans=list.get(i).getPolyLine();
			for (int j = 0; j < lineBeans.size(); j++) {
				int sx=(int) lineBeans.get(j).getStartX();
	        	int sy=(int) lineBeans.get(j).getStartY();
	        	int ex=(int) lineBeans.get(j).getEndX();
	        	int ey=(int) lineBeans.get(j).getEndY();
	        	boolean xCan=((dx>ex-40)&&(dx<ex+40));
	        	boolean yCan=((dy>ey-40)&&(dy<ey+40));
	        	if (xCan&&yCan) {
					movelineslist=list.get(i).getPolyLine();
					LineBean line1=new LineBean();
					LineBean line2=new LineBean();
					line1.setStartX((float)sx);
					line1.setStartY((float)sy);
					line1.setEndX(x);
					line1.setEndY(y);
					line2.setStartX(x);
					line2.setStartY(y);
					m=j;
					if (j==lineBeans.size()-1) {
						line2.setEndX(lineBeans.get(0).getEndX());
						line2.setEndY(lineBeans.get(0).getEndY());
						movelineslist.set(j, line1);
						movelineslist.set(0, line2);
						list.remove(i);
					}else {
						line2.setEndX(lineBeans.get(j+1).getEndX());
						line2.setEndY(lineBeans.get(j+1).getEndY());
						movelineslist.set(j, line1);
						movelineslist.set(j+1, line2);
						list.remove(i);
					}
					return true;
				}
			}
		}
		return false;
	}
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
<<<<<<< HEAD
		line.setName("polylineName");
		line.setPaintIsFull(false);
		line.setPaintWidth(4);
		line.setDescripte("描述");
=======
		line.setName("polylineName-"+lists.size());
		line.setPaintIsFull(false);
		line.setPaintWidth(4);
		line.setDescripte("描述"+lists.size());
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		lists.add(line);
	}
	public void AddPolygonParams(List<PolygonBean> polylist,List<LineBean> linelist){
		PolygonBean poly=new PolygonBean();
		List<LineBean> lineslist=new ArrayList<LineBean>();
		for (int i = 0; i <linelist.size(); i++) {
			LineBean lineBean=new LineBean();
<<<<<<< HEAD
			lineBean.setName("lines-"+i);
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
			lineBean.setStartX(linelist.get(i).getStartX());
			lineBean.setStartY(linelist.get(i).getStartY());
			lineBean.setEndX(linelist.get(i).getEndX());
			lineBean.setEndY(linelist.get(i).getEndY());
<<<<<<< HEAD
			lineBean.setDescripte("描述-"+i);
=======
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
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
