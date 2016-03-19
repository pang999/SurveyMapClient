package com.surveymapclient.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CouplePointLineBean;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;

public class LinesModel {
	
	//直线两头端点画点
	private Paint point=ViewContans.generatePaint(Color.RED, 18);
	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.BLACK, 6, false);
	//画图过程画笔
	private Paint painting=ViewContans.generatePaint(Color.RED, 4);
	//选中画笔
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10);
	//画布笔
//	private Paint sketchpaint=ViewContans.generatePaint(Color.GRAY, (float)0.5);
	
	private float start_x,start_y,end_x,end_y;
	
	private float sx1,sy1,sx2,sy2,ex12,ey12;
	
	private PointF movePoint=new PointF();
	private float sx,sy,ex,ey;
	
	private int rx,ry;
	//斜率
	private static int xl_k=10;
	public int LineType=0;
	
	public List<CouplePointLineBean> list = new ArrayList<CouplePointLineBean>();
		
	public void SketchXLines(Canvas canvas){
		
	}
	public void SketchYLines(Canvas canvas){
		
	}
	
	public void DrawLine(Canvas canvas){
		canvas.drawLine(start_x, start_y, end_x, end_y, painting);
		canvas.drawPoint(start_x, start_y, point);
		canvas.drawPoint(end_x, end_y, point);
	}
	
	public void MoveDrawLine(Canvas canvas){
		canvas.drawLine(sx, sy, ex, ey, checkpaint);
		canvas.drawPoint(sx, sy, point);
		canvas.drawPoint(ex, ey, point);
	}
	private void drawLineOnBitmap(float sx,float sy,float ex,float ey,Canvas canvas){
		canvas.drawLine(sx, sy, ex, ey, mPaint);
		canvas.drawPoint(sx, sy, point);
		canvas.drawPoint(ex, ey, point);
	}
	public void DrawLinesOnBitmap(List<CouplePointLineBean> lines,Canvas canvas){
		for (int i = 0; i < lines.size(); i++) {
			float sx=lines.get(i).getStartPoint().x;
			float sy=lines.get(i).getStartPoint().y;
			float ex=lines.get(i).getStopPoint().x;
			float ey=lines.get(i).getStopPoint().y;
			canvas.drawLine(sx, sy, ex, ey, mPaint);
			canvas.drawPoint(sx, sy, point);
			canvas.drawPoint(ex, ey, point);
		}
	}
	private void drawLinesOnBitmap(float sx1,float sy1,float sx2,float sy2,float ex12,float ey12, Canvas canvas){
		float[] pts={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		canvas.drawLines(pts, mPaint);
		canvas.drawPoint(sx1, sy1, point);
		canvas.drawPoint(sx2, sy2, point);
		canvas.drawPoint(ex12, ey12, point);
	}
	
	public void DrawLines(Canvas canvas){
		float[] pts={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		canvas.drawLines(pts, painting);
		canvas.drawPoint(sx1, sy1, point);
		canvas.drawPoint(sx2, sy2, point);
		canvas.drawPoint(ex12, ey12, point);
	}
	
	public void Line_touch_down(float x,float y){
		list.clear();
		start_x=end_x=ViewContans.AdsorbPoint((int)Math.floor(x));
		start_y=end_y=ViewContans.AdsorbPoint((int)Math.floor(y));
	}
	public void Line_camera_touch_down(float x,float y){
		list.clear();
		start_x=end_x=x;
		start_y=end_y=y;
	}
	public void Line_touch_move(float x,float y){
		if (LineType==Contants.IS_TWO_LINES) {
			ex12=x;
			ey12=y;
		}else {
			end_x=x;
			end_y=y;
		}
		
	}

	public void Line_touch_up(float x,float y,Canvas canvas){
		if (LineType==Contants.IS_TWO_LINES) {
			ex12=ViewContans.AdsorbPoint((int)Math.floor(x));
			ey12=ViewContans.AdsorbPoint((int)Math.floor(y));
			boolean isOverlap1=(ex12==sx1)&&(ey12==sy1);
			boolean isOverlap2=(ex12==sx2)&&(ey12==sy2);
			
			if (isOverlap1) {
				AddLineParams(list, sx2, sy2, ex12, ey12);	
				drawLineOnBitmap(sx2, sy2, ex12, ey12, canvas);
			}else if (isOverlap2) {
				AddLineParams(list, sx1, sy1, ex12, ey12);
				drawLineOnBitmap(sx1, sy1, ex12, ey12, canvas);
			}else {
				CouplePointLineBean cpl=new CouplePointLineBean(new PointF(sx1, sy1), new PointF(ex12, ey12));
				list.add(cpl);
				CouplePointLineBean cp2=new CouplePointLineBean(new PointF(sx2, sy2), new PointF(ex12, ey12));
				list.add(cp2);
				drawLinesOnBitmap(sx1, sy1, sx2, sy2, ex12, ey12, canvas);
			}
			LineType=Contants.IS_ONE_LINE;
		}else {
			end_x=ViewContans.AdsorbPoint((int)Math.floor(x));
			end_y=ViewContans.AdsorbPoint((int)Math.floor(y));	
			AddLineParams(list,start_x, start_y, end_x, end_y);
			drawLineOnBitmap(start_x, start_y, end_x, end_y, canvas);
		}			
	}
	public void Line_camera_touch_up(float x,float y,Canvas canvas){
		if (LineType==Contants.IS_TWO_LINES) {
			ex12=x;
			ey12=y;
			boolean isOverlap1=(ex12==sx1)&&(ey12==sy1);
			boolean isOverlap2=(ex12==sx2)&&(ey12==sy2);
			
			if (isOverlap1) {
				AddLineParams(list, sx2, sy2, ex12, ey12);	
				drawLineOnBitmap(sx2, sy2, ex12, ey12, canvas);
			}else if (isOverlap2) {
				AddLineParams(list, sx1, sy1, ex12, ey12);
				drawLineOnBitmap(sx1, sy1, ex12, ey12, canvas);
			}else {
				CouplePointLineBean cpl=new CouplePointLineBean(new PointF(sx1, sy1), new PointF(ex12, ey12));
				list.add(cpl);
				CouplePointLineBean cp2=new CouplePointLineBean(new PointF(sx2, sy2), new PointF(ex12, ey12));
				list.add(cp2);
				drawLinesOnBitmap(sx1, sy1, sx2, sy2, ex12, ey12, canvas);
			}
			LineType=Contants.IS_ONE_LINE;
		}else {
			end_x=x;
			end_y=y;	
			AddLineParams(list,start_x, start_y, end_x, end_y);
			drawLineOnBitmap(start_x, start_y, end_x, end_y, canvas);
		}			
	}
	
	public void AddLineParams(List<CouplePointLineBean> lists,float sx,float sy,float ex,float ey){
		CouplePointLineBean cpl=new CouplePointLineBean(new PointF(sx, sy), new PointF(ex, ey));
		lists.add(cpl);
	}
	
	public void AddLinesParams(List<CouplePointLineBean> lists, PointF start,PointF stop){
		CouplePointLineBean cpl=new CouplePointLineBean(start, stop);
		cpl.setAngle(0.00);
		cpl.setColor(Color.BLACK);
		cpl.setLength(0);
		cpl.setName("lineName-"+lists.size());
		cpl.setFull(false);
		cpl.setWidth(6);
		cpl.setDescripte("描述"+lists.size());
		lists.add(cpl);
	}
	
	public boolean ExtendLine(List<CouplePointLineBean> lines,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < lines.size(); i++) {
        	float sx=lines.get(i).getStartPoint().x;
        	float sy=lines.get(i).getStartPoint().y;
        	float ex=lines.get(i).getStopPoint().x;
        	float ey=lines.get(i).getStopPoint().y;
        	Logger.i("起点终点", "重绘前-->L"+i+": ("+(int)sx+","+(int)sy+")  ,  ("+(int)ex+","+(int)ey+")");
		}
		for (int i = 0; i < lines.size()-1; i++) {
			for (int j = i+1; j < lines.size(); j++) {
				int sxi=(int) lines.get(i).getStartPoint().x;
				int syi=(int) lines.get(i).getStartPoint().y;
				int exi=(int) lines.get(i).getStopPoint().x;
				int eyi=(int) lines.get(i).getStopPoint().y;  
				int sxj=(int) lines.get(j).getStartPoint().x;
				int syj=(int) lines.get(j).getStartPoint().y;
				int exj=(int) lines.get(j).getStopPoint().x;
				int eyj=(int) lines.get(j).getStopPoint().y;
				if ((sxi==sxj)&&(syi==syj)) {  
					int csx=Math.abs(dx-sxi);
					int csy=Math.abs(dy-syi);		
					Logger.i("共用端点", "第 i="+i+" 的头 , 第 j="+j+" 的头  :("+sxi+","+syi+")");				
					if (csx<40&&csy<40) {	
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStopPoint().x;
						sy1=lines.get(i).getStopPoint().y;
						sx2=lines.get(j).getStopPoint().x;
						sy2=lines.get(j).getStopPoint().y;
						ex12=x;
						ey12=y;
//						Logger.i("线段总数", "重叠 Lines="+couplePointLines.size());
//		            	Logger.i("起点终点", "---->Li"+i+": ("+sxi+","+syi+")  ,  ("+exi+","+eyi+")");
//		            	Logger.i("起点终点", "---->Lj"+j+": ("+sxj+","+syj+")  ,  ("+exj+","+eyj+")");
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((sxi==exj)&&(syi==eyj)) {     							
					int csx=Math.abs(dx-sxi);
					int csy=Math.abs(dy-syi);
//					Logger.i("共用端点", "第 i="+i+" 的头 , 第 j="+j+" 的尾  :("+sxi+","+syi+")");
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStopPoint().x;
						sy1=lines.get(i).getStopPoint().y;
						sx2=lines.get(j).getStartPoint().x;
						sy2=lines.get(j).getStartPoint().y;
						ex12=x;
						ey12=y;
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((exi==sxj)&&(eyi==syj)) {     							
					int csx=Math.abs(dx-exi);
					int csy=Math.abs(dy-eyi);
					Logger.i("共用端点", "第 i="+i+" 的尾 , 第 j="+j+" 的头  :("+exi+","+eyi+")");
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStartPoint().x;
						sy1=lines.get(i).getStartPoint().y;
						sx2=lines.get(j).getStopPoint().x;
						sy2=lines.get(j).getStopPoint().y;
						ex12=x;
						ey12=y;
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((exi==exj)&&(eyi==eyj)) {     							
					int csx=Math.abs(dx-exi);
					int csy=Math.abs(dy-eyi);
//					Logger.i("共用端点", "第 i="+i+" 的尾 , 第 j="+j+" 的尾  :("+exi+","+eyi+")");
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStartPoint().x;
						sy1=lines.get(i).getStartPoint().y;
						sx2=lines.get(j).getStartPoint().x;
						sy2=lines.get(j).getStartPoint().y;
						ex12=x;
						ey12=y;
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}
			}
			
		}
		for (int i = 0; i < lines.size(); i++) {
			int gspx=(int) lines.get(i).getStartPoint().x;
			int gspy=(int) lines.get(i).getStartPoint().y;
			int gepx=(int) lines.get(i).getStopPoint().x;
			int gepy=(int) lines.get(i).getStopPoint().y;
			int csx=Math.abs(dx-gspx);
			int csy=Math.abs(dy-gspy);
			int cex=Math.abs(dx-gepx);
			int cey=Math.abs(dy-gepy);
			if (csy<40&&csx<40) {
				LineType=Contants.IS_ONE_LINE;
				start_x=lines.get(i).getStopPoint().x;
				start_y=lines.get(i).getStopPoint().y;
				lines.remove(i);	
				return true;
			}else if (cex<40&&cey<40) {
				LineType=Contants.IS_ONE_LINE;
				start_x=lines.get(i).getStartPoint().x;
				start_y=lines.get(i).getStartPoint().y;
				lines.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public int PitchOnLine(List<CouplePointLineBean> lines,float x,float y){
		Logger.i("线段总数", "Lines="+lines.size());
	    int dx=(int) x;
	    int dy=(int) y;			
		for (int i = 0; i < lines.size(); i++) {
			int gspx=(int) lines.get(i).getStartPoint().x;
			int gspy=(int) lines.get(i).getStartPoint().y;
			int gepx=(int) lines.get(i).getStopPoint().x;
			int gepy=(int) lines.get(i).getStopPoint().y;
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
						xl_k=0;
						return i;						
					}
				}
				else if (k_fm==0) {
					int cx=Math.abs(dx-gepx);
					if (cx<40&&yCan) {
						xl_k=2;
						return i;
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
		    			if (k>0) {
							xl_k=1;
						}else if (k<0) {
							xl_k=-1;
						}
		    			return i;
					}
		    		
				}
			}					
		 }	
		return -1;
	}	
	
	public void MoveLine_down(List<CouplePointLineBean> lines,int i,float rx,float ry){
		sx=lines.get(i).getStartPoint().x;
		sy=lines.get(i).getStartPoint().y;
		ex=lines.get(i).getStopPoint().x;
		ey=lines.get(i).getStopPoint().y;
		movePoint.set(rx, ry);
		lines.remove(i);
	}
	
	public void MoveLine_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;
	    	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	sx=sx+dx;
			sy= sy+dy;
			ex= ex+dx;
			ey= ey+dy; 			
	    }		
	}
	public void MoveLine_up(Canvas canvas,float rx,float ry){	    
		sx= ViewContans.AdsorbPoint((int)Math.floor(sx));
		sy= ViewContans.AdsorbPoint((int)Math.floor(sy));
		ex= ViewContans.AdsorbPoint((int)Math.floor(ex));
		ey= ViewContans.AdsorbPoint((int)Math.floor(ey)); 
		AddLineParams(list, sx, sy, ex, ey);
		drawLineOnBitmap(sx, sy, ex, ey, canvas);
	}
	public void MoveLine_camera_up(Canvas canvas,float x,float y){    
		AddLineParams(list, sx, sy, ex, ey);
		drawLineOnBitmap(sx, sy, ex, ey, canvas);
	}

	public int PitchOnLineToText(List<CouplePointLineBean> lines,float x,float y){
		int i=PitchOnLine(lines, x, y);
		return i;
	}
	public void LineChangeToText(List<CouplePointLineBean> lines,Canvas canvas,int i){
		Paint paint=ViewContans.generatePaint(Color.BLUE, 10);
		float sxi= lines.get(i).getStartPoint().x;
		float syi= lines.get(i).getStartPoint().y;
		float exi= lines.get(i).getStopPoint().x;
		float eyi= lines.get(i).getStopPoint().y; 
		canvas.drawLine(sxi, syi, exi, eyi, paint);
	}
	
	public void AddTextOnLine(List<CouplePointLineBean> lines,Canvas canvas,int i,String text,int place){
		Paint textpaint = new Paint();                
    	textpaint.setColor(Color.BLUE);
    	textpaint.setTextSize(30); 
    	List<PointF> list=new ArrayList<PointF>();
		
        list.add(new PointF(lines.get(i).getStopPoint().x, lines.get(i).getStartPoint().y));
        list.add(new PointF(lines.get(i).getStartPoint().x, lines.get(i).getStartPoint().y));
        list.add(new PointF(lines.get(i).getStopPoint().x, lines.get(i).getStopPoint().y));
		float A=(float) PolygonModel.Angle(list);
		float len=(float)getTextWidth(textpaint, text);
		float tx=lines.get(i).getStartPoint().x/2+lines.get(i).getStopPoint().x/2;
		float ty=lines.get(i).getStartPoint().y/2+lines.get(i).getStopPoint().y/2;	
		Logger.i("文字的角度", ""+A);
		if (xl_k==0) {
			ViewContans.drawText(canvas, text, tx-place , ty-place, textpaint, -A,len);
		}else if (xl_k==2) {
			ViewContans.drawText(canvas, text, tx+place , ty-place, textpaint, 90,len);
		}else if (xl_k==1) {
			ViewContans.drawText(canvas, text, tx+place , ty-place, textpaint, A,len);
		}else if (xl_k==-1) {
			ViewContans.drawText(canvas, text, tx-place , ty-place, textpaint, -A,len);
		}
       	list.clear();	
	}
	public void AddLineNameAtIndex(List<CouplePointLineBean> lines ,String text,int index){
		lines.get(index).setName(text);
	}
	 public static int getTextWidth(Paint paint, String str) {  
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
	
	public void DeleteLine(List<CouplePointLineBean> lines,Canvas canvas){
	          
        Logger.i("线段总数", "before-->Lines="+lines.size());
        lines.remove(lines.size()-1);
        for (int i = 0; i < lines.size(); i++) {
		   	float sx=lines.get(i).getStartPoint().x;
		   	float sy=lines.get(i).getStartPoint().y;
		   	float ex=lines.get(i).getStopPoint().x;
		   	float ey=lines.get(i).getStopPoint().y;
		   
		   	Logger.i("起点终点", "undo-->L"+i+": ("+(int)sx+","+(int)sy+")  ,  ("+(int)ex+","+(int)ey+")");
		   	ViewContans.DrawLine(canvas, sx, sy, ex, ey, 
        			ViewContans.generatePaint(lines.get(i).getColor(), lines.get(i).getWidth(), lines.get(i).isFull()), point);
		}
        Logger.i("线段总数", "after-->Lines="+lines.size());
        
	}
	public void RemoveIndexLine(List<CouplePointLineBean> lines,Canvas canvas,int index){
        
		Logger.i("线段总数", "before-->Lines="+lines.size());
		lines.remove(index);
        for (int i = 0; i < lines.size(); i++) {
        	float sx=lines.get(i).getStartPoint().x;
        	float sy=lines.get(i).getStartPoint().y;
        	float ex=lines.get(i).getStopPoint().x;
        	float ey=lines.get(i).getStopPoint().y;
        	Logger.i("起点终点", "undo-->L"+i+": ("+(int)sx+","+(int)sy+")  ,  ("+(int)ex+","+(int)ey+")");
//			ViewContans.DrawLine(canvas, sx, sy, ex, ey, mPaint, point);
        	ViewContans.DrawLine(canvas, sx, sy, ex, ey, 
        			ViewContans.generatePaint(lines.get(i).getColor(), lines.get(i).getWidth(), lines.get(i).isFull()), point);
		}
        Logger.i("线段总数", "after-->Lines="+lines.size());
        
	}
	public void ChangeLineAttributeAtIndex(List<CouplePointLineBean> lines
			,Canvas canvas,int index,String name,double angle,int color
			,double lenght,boolean isfull,float w,String desc){	
		for (int i = 0; i < lines.size(); i++) {
        	if (i==index) {
        		float sx=lines.get(index).getStartPoint().x;
            	float sy=lines.get(index).getStartPoint().y;
            	float ex=lines.get(index).getStopPoint().x;
            	float ey=lines.get(index).getStopPoint().y;
            	lines.get(index).setAngle(angle);
            	lines.get(index).setColor(color);
            	lines.get(index).setLength(lenght);
            	lines.get(index).setName(name);
            	lines.get(index).setFull(isfull);
            	lines.get(index).setWidth(w);
            	lines.get(index).setDescripte(desc);
            	Logger.i("画笔宽度", "w="+w);   
            	ViewContans.DrawLine(canvas, sx, sy, ex, ey, 
            			ViewContans.generatePaint(color, w, isfull), point);
			}else {
				float sx=lines.get(i).getStartPoint().x;
            	float sy=lines.get(i).getStartPoint().y;
            	float ex=lines.get(i).getStopPoint().x;
            	float ey=lines.get(i).getStopPoint().y;
    			ViewContans.DrawLine(canvas, sx, sy, ex, ey, mPaint, point);
			}
		}
		
	}
	public void ReStartDrawLine(List<CouplePointLineBean> lines,Canvas canvas){    
        for (int i = 0; i < lines.size(); i++) {
        	float sx=lines.get(i).getStartPoint().x;
        	float sy=lines.get(i).getStartPoint().y;
        	float ex=lines.get(i).getStopPoint().x;
        	float ey=lines.get(i).getStopPoint().y;
			ViewContans.DrawLine(canvas, sx, sy, ex, ey, mPaint, point);
		}
        
	}
	
}
