package com.surveymapclient.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class StardyView extends View{   
	

		private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        private Bitmap  mBitmap;
        private Paint mPaint;
         
        private ArrayList<DrawPath> savePath;
//        private ArrayList<DrawPath> deletePath;
        private DrawPath dp;
         
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
         
        private int bitmapWidth;
        private int bitmapHeight;
         
        public StardyView(Context c) {
            super(c);
            //�õ���Ļ�ķֱ���
             
            bitmapWidth = Contants.sreenWidth/3;
            bitmapHeight = Contants.screenHeight/3 - 2 * 45;
             
            initCanvas();
            savePath = new ArrayList<DrawPath>();
//            deletePath = new ArrayList<DrawPath>();
             
        }
        public StardyView(Context c, AttributeSet attrs) {
            super(c,attrs);
             
            bitmapWidth = Contants.sreenWidth;
            bitmapHeight = Contants.screenHeight - 2 * 45;
             
            initCanvas();
            savePath = new ArrayList<DrawPath>();
//            deletePath = new ArrayList<DrawPath>();
        }
        //��ʼ������
        public void initCanvas(){
             
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFF00FF00);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(1);  
             
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
             
            
             
            //������С 
            mBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, 
                Bitmap.Config.RGB_565);
            mCanvas = new Canvas(mBitmap);  //����mCanvas���Ķ���������������mBitmap��
             
            mCanvas.drawColor(Color.BLUE);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
             
        }
         
         
        @Override
        protected void onDraw(Canvas canvas) {   
             
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);     //��ʾ�ɵĻ���       
            if (mPath != null) {
                // ʵʱ����ʾ
                canvas.drawPath(mPath, mPaint);
            }
        }
        //·������ 
        class DrawPath{
            Path path;
            Paint paint;
        }
        void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){
            if(angle != 0){
                canvas.rotate(angle, x, y); 
            }
            canvas.drawText(text, x, y, paint);
            if(angle != 0){
                canvas.rotate(-angle, x, y); 
            }
        }
        /**
         * �����ĺ���˼����ǽ�������գ�
         * ������������Path·�����һ���Ƴ�����
         * ���½�·�����ڻ������档
         */
        public void undo(){
             
            System.out.println(savePath.size()+"--------------");
            if(savePath != null && savePath.size() > 0){
                //���ó�ʼ��������������ջ���
                initCanvas();
                 
                //��·�������б��е����һ��Ԫ��ɾ�� ,�����䱣����·��ɾ���б���
                DrawPath drawPath = savePath.get(savePath.size() - 1);
//                deletePath.add(drawPath);
                savePath.remove(savePath.size() - 1);
                 
                //��·�������б��е�·���ػ��ڻ�����
                Iterator<DrawPath> iter = savePath.iterator();      //�ظ�����
                while (iter.hasNext()) {
                    DrawPath dp = iter.next();
                    mCanvas.drawPath(dp.path, dp.paint);
                     
                }
                invalidate();// ˢ��
            }
        }
        /**
         * �ָ��ĺ���˼����ǽ�������·�����浽����һ���б�����(ջ)��
         * Ȼ���redo���б�����ȡ����˶���
         * ���ڻ������漴��
         */
//        public void redo(){
//            if(deletePath.size() > 0){
//                //��ɾ����·���б��е����һ����Ҳ�������·��ȡ����ջ��,������·�������б���
//                DrawPath dp = deletePath.get(deletePath.size() - 1);
//                savePath.add(dp);
//                //��ȡ����·���ػ��ڻ�����
//                mCanvas.drawPath(dp.path, dp.paint);
//                //����·����ɾ����·���б���ȥ��
//                deletePath.remove(deletePath.size() - 1);
//                invalidate();
//            }
//        }
        /*
         * ��յ���Ҫ˼����ǳ�ʼ������
         * ������·��������List���
         * */
        public void removeAllPaint(){
            //���ó�ʼ��������������ջ���
            initCanvas();
            invalidate();//ˢ��
            savePath.clear();
//            deletePath.clear();
        }
         
       /* 
        * ��������ͼ��
        * ���ػ�ͼ�ļ��Ĵ洢·��
        * */
        public String saveBitmap(){
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
                paintPath = "/My/" + str;
                 
         
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
             
            return paintPath;
        }
         
         
        private void touch_start(float x, float y) {
            mPath.reset();//���path
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                //mPath.quadTo(mX, mY, x, y);
                 mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);//Դ����������д�ģ�������û��Ū���ף�ΪʲôҪ������
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            savePath.add(dp);
            mPath = null;
             
        }
         
        public void drawlines(){
//        	float[] pts={200,400,200,200,  
//                    200,200,400,200}; 
//        	mCanvas.drawLines(pts, mPaint);
        	Paint paint = new Paint();                
            paint.setColor(Color.RED);
            paint.setTextSize(20);  
        	drawText(mCanvas, "line", 360, 640, paint, 45);
        }
        
        public void drawtext(){
//        	float[] pts={200,400,200,200,  
//                    200,200,400,200}; 
//        	mCanvas.drawLines(pts, mPaint);
        	Paint paint = new Paint();                
            paint.setColor(Color.RED);
            paint.setTextSize(40);  
        	drawText(mCanvas, "line", 360, 640, paint, 90);
        }
        
        public void drawpath(){
        	
        	mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(10); 
        	mPaint.setAlpha(0x40);
        	Path path =new Path();
        	
        	path.moveTo(200, 400);
        	path.lineTo(200, 200);
//        	path.lineTo(400, 200);
        	path.lineTo(500, 300);
        	path.lineTo(300, 600);
//        	path.lineTo(200, 400);
        	
        	mCanvas.drawPath(path, mPaint);
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
             
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                     
                    mPath = new Path();
                    dp = new DrawPath();
                    dp.path = mPath;
                    dp.paint = mPaint;
                     
                    touch_start(x, y);
                    invalidate(); //����
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
}
