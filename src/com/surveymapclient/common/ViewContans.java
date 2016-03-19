package com.surveymapclient.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.view.MotionEvent;

public class ViewContans {

	//画笔
	private static Paint paint;
	
	public static Paint generatePaint(int color,float width){
		paint=new Paint();
		paint.setColor(color);
		paint.setStrokeJoin(Paint.Join.ROUND);      
		paint.setStrokeCap(Paint.Cap.ROUND); 
		paint.setStrokeWidth(width); 
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
	    return paint;
	}
	public static Paint generatePaint(float width){
		paint=new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeJoin(Paint.Join.ROUND);      
		paint.setStrokeCap(Paint.Cap.ROUND); 
		paint.setStrokeWidth(width); 
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
	    return paint;
	}
	public static Paint generateTextPaint(){
		paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeJoin(Paint.Join.ROUND);      
		paint.setStrokeCap(Paint.Cap.ROUND); 
		paint.setStrokeWidth(3); 
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
        File dir = new File("/sdcard/notes/");
        File file = new File("/sdcard/notes/",str);
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
            paintPath = "/sdcard/notes/" + str;
             
     
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
	 public static void DrawLines(Canvas canvas,float sx1,float
			 sy1,float sx2,float sy2,float ex12,float ey12,Paint line,Paint point){
		 float[] spt={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		 canvas.drawLines(spt, paint);
		 canvas.drawPoint(sx1, sy1, point);
		 canvas.drawPoint(sx2, sy2, point);
		 canvas.drawPoint(ex12 , ey12, point);
		 Paint paint = new Paint();                
         paint.setColor(Color.RED);
         paint.setTextSize(20);  

	 }
	 public static void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle,float lenght){
         if(angle != 0){
             canvas.rotate(angle, x, y); 
         }
         paint.setColor(Color.RED);                      //设置画笔颜色  
         paint.setAlpha(50);
         canvas.drawText(text, x, y, paint);
         canvas.drawRect(x-5, y+5, x+lenght+5, y-25, paint);
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
}
