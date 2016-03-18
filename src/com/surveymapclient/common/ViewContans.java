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

	//����
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
	//��ʾЧ��
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
        //���ϵͳ��ǰʱ�䣬���Ը�ʱ����Ϊ�ļ���
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");  
        Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ�� 
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
            //�����ͼ�ļ�·��
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
	/**��������֮��ľ�������**/
	public static float distance(MotionEvent event) {
		// TODO Auto-generated method stub
		float eX = event.getX(1) - event.getX(0);  //����ĵ����� - ǰ�������� 
		float eY = event.getY(1) - event.getY(0);
		return (float) Math.sqrt(eX * eX + eY * eY);
	}
	/** 
	 * �ж��Ƿ��г����������� 
	 * @param lastX ����ʱX���� 
	 * @param lastY ����ʱY���� 
	 * @param thisX �ƶ�ʱX���� 
	 * @param thisY �ƶ�ʱY���� 
	 * @param lastDownTime ����ʱ�� 
	 * @param thisEventTime �ƶ�ʱ�� 
	 * @param longPressTime �жϳ���ʱ��ķ�ֵ 
	 */ 
	 public static boolean isLongPressed(float lastX,float lastY, float thisX,float thisY,
			 long lastDownTime,long thisEventTime,long longPressTime){ 
		 
		 float offsetX = Math.abs(thisX - lastX); 
		 float offsetY = Math.abs(thisY - lastY); 
		 long intervalTime = thisEventTime - lastDownTime; 
		 Logger.i("����ֵ", "x��ֵ="+offsetX+"->"+lastX+"->"+thisX+"��y��ֵ="+offsetY+"��ʱ���ֵ="+intervalTime);
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
		 Logger.i("�̰�ֵ", "x��ֵ="+offsetX+"->"+lastX+"->"+thisX+"��y��ֵ="+offsetY+"��ʱ���ֵ="+intervalTime);
		 if(offsetX <=10 && offsetY<=10 && intervalTime <= longPressTime){ 
			 return true; 
		 } 
		 return false; 
	 }
	 //��һ����
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
         paint.setColor(Color.RED);                      //���û�����ɫ  
         paint.setAlpha(50);
         canvas.drawText(text, x, y, paint);
         canvas.drawRect(x-5, y+5, x+lenght+5, y-25, paint);
         if(angle != 0){
             canvas.rotate(-angle, x, y); 
         }
     }
	 //�����������
	 public static float AdsorbPoint(int x){
			int n=x/20;
			int dx=x%20;
			Logger.i("������", "����="+n+"������="+dx);
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
