package com.surveymapclient.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.RectangleBean;
import com.surveymapclient.model.PolygonModel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.view.MotionEvent;

public class ViewContans {

	//画笔
	private static Paint paint;
	//画点
	private static Paint point=generatePaint(Color.RED, 20,true);
	private static Paint huabu=generatePaint(Color.GRAY, (float)0.5,true);
	
	final static int width = Contants.sreenWidth*3;
	final static int height = Contants.screenHeight*3;

	public static Paint generateTextPaint(){
		paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeJoin(Paint.Join.ROUND);      
		paint.setStrokeCap(Paint.Cap.ROUND); 
		paint.setStrokeWidth(2); 
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setTextSize(30);
	    return paint;
	}
	//显示效果
	public static Paint generatePaint(int color,float width,boolean isFull){
		paint=new Paint();
		paint.setColor(color);
		paint.setStrokeJoin(Paint.Join.ROUND);      
		paint.setStrokeCap(Paint.Cap.ROUND); 
		paint.setStrokeWidth(width); 
		paint.setAntiAlias(true);
		if (!isFull) {
	        PathEffect effects = new DashPathEffect(new float[]{5,13,5,13},1);  
	        paint.setPathEffect(effects);
		}
		paint.setStyle(Style.STROKE);
	    return paint;
	}
	public static String saveBitmap(Bitmap mBitmap){
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");  
        Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间 
        String   str   =   formatter.format(curDate);  
        String paintPath = "";
        str = str + "paint.png";
        File dir = new File("/sdcard/surveymap/png/");
        File file = new File("/sdcard/surveymap/png/",str);
        if (!dir.exists()) { 
            dir.mkdir(); 
        } 
        else{
            if(file.exists()){
                file.delete();
            }
        }
         
        try {
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out); 
            out.flush(); 
            out.close(); 
            //保存绘图文件路径
            paintPath = "/sdcard/surveymap/png/" + str;
             
     
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
         
        return paintPath;
    }
	/**计算两点之间的距离像素**/
	public static float distance(MotionEvent event) {
		// TODO Auto-generated method stub
		float eX = event.getX(1) - event.getX(0);  //后面的点坐标 - 前面点的坐标 
		float eY = event.getY(1) - event.getY(0);
		return (float) Math.sqrt(eX * eX + eY * eY);
	}
	/** 
	 * 判断是否有长按动作发生 
	 * @param lastX 按下时X坐标 
	 * @param lastY 按下时Y坐标 
	 * @param thisX 移动时X坐标 
	 * @param thisY 移动时Y坐标 
	 * @param lastDownTime 按下时间 
	 * @param thisEventTime 移动时间 
	 * @param longPressTime 判断长按时间的阀值 
	 */ 
	 public static boolean isLongPressed(float lastX,float lastY, float thisX,float thisY,
			 long lastDownTime,long thisEventTime,long longPressTime){ 
		 
		 float offsetX = Math.abs(thisX - lastX); 
		 float offsetY = Math.abs(thisY - lastY); 
		 long intervalTime = thisEventTime - lastDownTime; 
		 Logger.i("长按值", "x差值="+offsetX+"->"+lastX+"->"+thisX+"，y差值="+offsetY+"，时间差值="+intervalTime);
		 if(offsetX <=10 && offsetY<=10 && intervalTime >= longPressTime){ 
			 return true; 
		 } 
		 return false; 
	 }
	 public static boolean isShortPressed(float lastX,float lastY, float thisX,float thisY,
			 long lastDownTime,long thisEventTime,long longPressTime){ 
		 
		 float offsetX = Math.abs(thisX - lastX); 
		 float offsetY = Math.abs(thisY - lastY); 
		 long intervalTime = thisEventTime - lastDownTime; 
		 Logger.i("短按值", "x差值="+offsetX+"->"+lastX+"->"+thisX+"，y差值="+offsetY+"，时间差值="+intervalTime);
		 if(offsetX <=10 && offsetY<=10 && intervalTime <= longPressTime){ 
			 return true; 
		 } 
		 return false; 
	 }
	 //画一条线
	 public static void DrawLine(Canvas canvas,float startX,float startY,float stopX,float stopY,Paint line,Paint point){
		 canvas.drawLine(startX, startY, stopX, stopY, line);
		 canvas.drawPoint(startX, startY, point);
		 canvas.drawPoint(stopX, stopY, point);
	 }
	 public static void DrawLines(Canvas canvas,float[] spt,Paint line,Paint point){
//		 float[] spt={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		 canvas.drawLines(spt, line);
		 canvas.drawPoint(spt[0], spt[1], point);
		 canvas.drawPoint(spt[4], spt[5], point);
		 canvas.drawPoint(spt[6], spt[7], point);
	 }
	 public static  void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle,float lenght){
         if(angle != 0){
             canvas.rotate(angle, x, y); 
         }
 		Logger.i("长度角度", "长度=drawText");

         paint.setColor(Color.RED);                      //设置画笔颜色  
         paint.setAlpha(50);
         canvas.drawText(text, x, y, paint);
         canvas.drawRect(x-5, y+8, x+lenght+5, y-28, paint);
         if(angle != 0){
             canvas.rotate(-angle, x, y); 
         }
     }
	 //整除获得吸附
	 public static float AdsorbPoint(int x){
			int n=x/20;
			int dx=x%20;
			Logger.i("求整数", "除整="+n+"，除余="+dx);
			int m=0;
			if (dx==0) {
				m=n;
			}else if (dx<=10) {
				m=n;
			}else {
				m=n+1;
			}
			return (float)20*m;
	}
	public static void initCanvasHuabu(Canvas canvas,Bitmap mBitmap,float scale){
		canvas.drawColor(Color.WHITE);
		final int space = 20;   //长宽间隔  
	    int vertz = 0;  
	    int hortz = 0;  
	    int xcount=height/10;
	    int ycount=width/10;
		canvas.scale(scale, scale,width/2,height/2);
		// 将前面已经画过得显示出来
	    canvas.drawBitmap(mBitmap, 0, 0, null);     //显示旧的画布       
	    Logger.i("mBitmap大小", mBitmap.toString());
	    
	    for(int i=0;i<xcount/2+1;i++){  
	       canvas.drawLine(0,  vertz,  width, vertz, huabu);  
	       vertz+=space;  
	    }   
	    for (int i = 0; i < ycount/2+1; i++) {
	       canvas.drawLine(hortz, 0, hortz, height, huabu);  
	       hortz+=space;  
		}
        canvas.drawPoint(0, 0, point);
	    canvas.drawPoint( width, 0, point);
	    canvas.drawPoint(0,  height, point);
	    canvas.drawPoint( width,  height, point);
	    canvas.drawPoint(width/2, height/2, point);
	}
	public static double getArea(List<PointF> list) {
		// S = 0.5 * ( (x0*y1-x1*y0) + (x1*y2-x2*y1) + ... + (xn*y0-x0*yn) )
		double area = 0.00;
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				area += list.get(i).x *list.get(i + 1).y - list.get(i + 1).x * list.get(i).y;
			}else {
				area += list.get(i).x * list.get(0).y- list.get(0).x * list.get(i).y;
			}
		}
		area = area / 2.00;
		return area;
	}
	// 判断点pos是否在指定的三角形内。
	public static boolean inTriangle(PointF pos, PointF posA, PointF posB,
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
	public static double triangleArea(PointF pos1, PointF pos2, PointF pos3) {
	    double area = Math.abs((pos1.x * pos2.y + pos2.x * pos3.y + pos3.x * pos1.y
	            - pos2.x * pos1.y - pos3.x * pos2.y - pos1.x * pos3.y) / 2.0D);
	    return area;
	}
	
	public static void AddTextOnLine(LineBean line,Canvas canvas,String text,int place){
		Paint textpaint = new Paint();                
    	textpaint.setColor(Color.BLUE);
    	textpaint.setTextSize(30); 
    	List<PointF> pointFs=new ArrayList<PointF>();	
    	float sx=line.getStartX();
    	float sy=line.getStartY();
    	float ex=line.getEndX();
    	float ey=line.getEndY();
    	pointFs.add(new PointF(ex,sy));
    	pointFs.add(new PointF(sx,sy));
    	pointFs.add(new PointF(ex,ey));
    	float k_fz=ey-sy;
    	float k_fm=ex-sx;
    	float xl_k=10;
		if (k_fz==0) {
				xl_k=0;
		}
		else if (k_fm==0) {
				xl_k=2;
		}else {
			double val=((double)k_fz)/k_fm;
    		BigDecimal bd =new BigDecimal(val);
    		//保留2位小数
    		double k = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); 		    		
    			if (k>0) {
					xl_k=1;
				}else if (k<0) {
					xl_k=-1;
				}    		
		}
    	Logger.i("长度角度", "---->L"+": ("+line.getStartX()+","+line.getStartY()+")  ,  ("+line.getEndX()+","+line.getEndY()+")");
		float A=(float) PolygonModel.Angle(pointFs);
		float len=getTextWidth(textpaint, text+"m");
		float tx=sx/2+ex/2;
		float ty=sy/2+ey/2;	
		Logger.i("文字的角度", ""+A);
		if (xl_k==0) {
			drawText(canvas, text, tx-place , ty-place, textpaint, -A,len);
		}else if (xl_k==2) {
			drawText(canvas, text, tx+place , ty-place, textpaint, 90,len);
		}else if (xl_k==1) {
			drawText(canvas, text, tx+place , ty-place, textpaint, A,len);
		}else if (xl_k==-1) {
			drawText(canvas, text, tx-place , ty-place, textpaint, -A,len);
		}
		pointFs.clear();	
	}	 
	public static void AddTextOnRectangle(RectangleBean rect,Canvas canvas,String area,String lengh,String width){
		Paint textpaint = new Paint();                
    	textpaint.setColor(Color.BLUE);
    	textpaint.setTextSize(30);
		float sx=rect.getStartX();
		float sy=rect.getStartY();
		float ex=rect.getEndX();
		float ey=rect.getEndY();
		int place=20;
		float tx_x=sx;
		float tx_y=sy/2+ey/2;		
		float ty_y=sy;
		float ty_x=ex/2+sx/2;
		float lenL=getTextWidth(textpaint, lengh);
		float lenW=getTextWidth(textpaint, width);
		float lenA=getTextWidth(textpaint, area);
		Logger.i("BackRectangle", "lengh="+rect.getRectLenght());
		drawText(canvas, lengh, ty_x-place, ty_y-place, textpaint, 0, lenL);
		drawText(canvas, width, tx_x-place, tx_y+place, textpaint, -90, lenW);
		drawText(canvas, area, ty_x, tx_y, textpaint, 0, lenA);		
	}
	public static void AddTextOnCoordinate(CoordinateBean coor,Canvas canvas,
			String volum,String lengh,String width,String height){
		Paint textpaint = new Paint();                
    	textpaint.setColor(Color.BLUE);
    	textpaint.setTextSize(30);
		float cx=coor.getCenterX();
		float cy=coor.getCenterY();
		float xx=coor.getXaxisX();
		float xy=coor.getXaxisY();
		float yx=coor.getYaxisX();
		float yy=coor.getYaxisY();
		float zx=coor.getZaxisX();
		float zy=coor.getZaxisY();
		List<PointF> pointFs=new ArrayList<PointF>();	
		pointFs.add(new PointF(xx,cy));
    	pointFs.add(new PointF(cx,cy));
    	pointFs.add(new PointF(xx,xy));
		int place=20;
		float zx_x=cx;
		float zx_y=zy/2+cy/2;				
		float yy_x=cx/2+yx/2;
		float yy_y=cy;		
		float xx_x=xx/2+cx/2;
		float xx_y=xy/2+cy/2;
		float A=(float) PolygonModel.Angle(pointFs);
		float lenL=getTextWidth(textpaint, lengh);
		float lenW=getTextWidth(textpaint, width);
		float lenH=getTextWidth(textpaint, height);
		float lenV=getTextWidth(textpaint, volum);
		drawText(canvas, lengh, yy_x-place, yy_y-place, textpaint, 0, lenL);
		drawText(canvas, width, xx_x-40, xx_y+40, textpaint,-A, lenW);
		drawText(canvas, height, zx_x-place, zx_y+lenH/2, textpaint,  -90, lenH);		
		drawText(canvas, volum, xx_x-lenV/2, yy_y, textpaint, 0, lenV);
	}
	public static void AddTextOnAngle(AngleBean angleBean,Canvas canvas,String angle){
		Paint textpaint = new Paint();                
    	textpaint.setColor(Color.BLUE);
    	textpaint.setTextSize(30);
    	float ax=angleBean.getAngleX();
    	float ay=angleBean.getAngleY();
    	float lena=getTextWidth(textpaint, angle);
    	drawText(canvas, angle, ax, ay, textpaint, 0, lena);
	}
	public static float getTextWidth(Paint paint, String str) {  
        int iRet = 0;  
        if (str != null && str.length() > 0) {  
            int len = str.length();  
            float[] widths = new float[len];  
            paint.getTextWidths(str, widths);  
            for (int j = 0; j < len; j++) {  
                iRet += (int) Math.ceil(widths[j]);  
            }              
        }  
        return iRet;  
	}
}
