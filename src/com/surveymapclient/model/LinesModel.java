package com.surveymapclient.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.Contants;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.LineBean;

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
	private Paint point=ViewContans.generatePaint(Color.RED, 18,true);
	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.BLACK, 6, false);
	//画图过程画笔
	private Paint painting=ViewContans.generatePaint(Color.RED, 4,true);
	//选中画笔
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10,true);
	//画布笔
//	private Paint sketchpaint=ViewContans.generatePaint(Color.GRAY, (float)0.5);
	
	private float start_x,start_y,end_x,end_y;
	private float start_mx,start_my,end_mx,end_my;
	private float sx1,sy1,sx2,sy2,ex12,ey12;
	
	private PointF movePoint=new PointF();
	//斜率
	public static int xl_k=10;
	public int LineType=0;
	
	public List<LineBean> Getlines = new ArrayList<LineBean>();
	LineBean getremoveline=new LineBean();	
	public void SketchXLines(Canvas canvas){
		
	}
	public void SketchYLines(Canvas canvas){
		
	}
	//画直线
	public void DrawLine(Canvas canvas){
		ViewContans.DrawLine(canvas, start_x, start_y, end_x,end_y, painting, point);
	}
	//直线移动
	public void MoveDrawLine(Canvas canvas){
		ViewContans.DrawLine(canvas,start_mx, start_my, end_mx,end_my, checkpaint, point);
	}
	//首次画在bitmap
	private void drawLineFirstOnBitmap(float sx,float sy,float ex,float ey,Canvas canvas){
		ViewContans.DrawLine(canvas, sx, sy, ex, ey, mPaint, point);
	}
	//重绘所以直线
	public void DrawLinesOnBitmap(List<LineBean> lines,Canvas canvas){
		for (int i = 0; i < lines.size(); i++) {
			ViewContans.DrawLine(canvas,
					lines.get(i).getStartX(), 
					lines.get(i).getStartY(), 
					lines.get(i).getEndX(), 
					lines.get(i).getEndY(), 
					ViewContans.generatePaint(lines.get(i).getPaintColor(),lines.get(i).getPaintWidth(), lines.get(i).isPaintIsFull()), 
					point);
			if (lines.get(i).getName().length()<8&&lines.get(i).getName().length()>0) {
        		ViewContans.AddTextOnLine(lines.get(i), canvas,lines.get(i).getName(),-25);
			}            	
        	if (lines.get(i).getLength()>0||lines.get(i).getAngle()>0) {
        		ViewContans.AddTextOnLine(lines.get(i), canvas,lines.get(i).getLength()+"m "+lines.get(i).getAngle()+"°",10);
			}
		}
	}
	//两个线交叉
	public void DrawLines(Canvas canvas){
		float[] pts={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		ViewContans.DrawLines(canvas, pts, painting, point);
	}
	//两个线交叉重绘
	private void drawLinesOnBitmap(float sx1,float sy1,float sx2,float sy2,float ex12,float ey12, Canvas canvas){
		float[] pts={sx1,sy1,ex12,ey12,sx2,sy2,ex12,ey12};
		ViewContans.DrawLines(canvas, pts, mPaint, point);
	}

	public void Line_touch_down(float x,float y){
		start_x=end_x=ViewContans.AdsorbPoint((int)Math.floor(x));
		start_y=end_y=ViewContans.AdsorbPoint((int)Math.floor(y));
	}
	public void Line_camera_touch_down(float x,float y){
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
				AddLinesParams(Getlines, sx2, sy2, ex12, ey12);	
				drawLineFirstOnBitmap(sx2, sy2, ex12, ey12, canvas);
			}else if (isOverlap2) {
				AddLinesParams(Getlines, sx1, sy1, ex12, ey12);
				drawLineFirstOnBitmap(sx1, sy1, ex12, ey12, canvas);
			}else {
				AddLinesParams(Getlines, sx1, sy1, ex12, ey12);	
				AddLinesParams(Getlines, sx2, sy2, ex12, ey12);	
				drawLinesOnBitmap(sx1, sy1, sx2, sy2, ex12, ey12, canvas);
			}
			LineType=Contants.IS_ONE_LINE;
		}else {
			end_x=ViewContans.AdsorbPoint((int)Math.floor(x));
			end_y=ViewContans.AdsorbPoint((int)Math.floor(y));	
			AddLinesParams(Getlines,start_x, start_y, end_x, end_y);
			drawLineFirstOnBitmap(start_x, start_y, end_x, end_y, canvas);
		}			
	}
	public void Line_camera_touch_up(float x,float y,Canvas canvas){
		if (LineType==Contants.IS_TWO_LINES) {
			ex12=x;
			ey12=y;
			boolean isOverlap1=(ex12==sx1)&&(ey12==sy1);
			boolean isOverlap2=(ex12==sx2)&&(ey12==sy2);			
			if (isOverlap1) {
				AddLinesParams(Getlines, sx2, sy2, ex12, ey12);	
				drawLineFirstOnBitmap(sx2, sy2, ex12, ey12, canvas);
			}else if (isOverlap2) {
				AddLinesParams(Getlines, sx1, sy1, ex12, ey12);
				drawLineFirstOnBitmap(sx1, sy1, ex12, ey12, canvas);
			}else {
				AddLinesParams(Getlines, sx1, sy1, ex12, ey12);	
				AddLinesParams(Getlines, sx2, sy2, ex12, ey12);	
				drawLinesOnBitmap(sx1, sy1, sx2, sy2, ex12, ey12, canvas);
			}
			LineType=Contants.IS_ONE_LINE;
		}else {
			end_x=x;
			end_y=y;	
			AddLinesParams(Getlines,start_x, start_y, end_x, end_y);
			drawLineFirstOnBitmap(start_x, start_y, end_x, end_y, canvas);
		}			
	}

	public void AddChangeLineParams(List<LineBean> lists,float sx,float sy,float ex,float ey,
			double angle,String name,String desc,double lengh, int color,float width,boolean isfull){
		LineBean line=new LineBean();
		line.setStartX(sx);
		line.setStartY(sy);
		line.setEndX(ex);
		line.setEndY(ey);
		line.setAngle(angle);
		line.setPaintColor(color);
		line.setLength(lengh);
		line.setName(name);
		line.setPaintIsFull(false);
		line.setPaintWidth(width);
		line.setDescripte(desc);
		line.setPaintIsFull(isfull);
		lists.add(line);
	}
	public void AddLinesParams(List<LineBean> lists, float sx,float sy,float ex,float ey){
		LineBean line=new LineBean();
		line.setStartX(sx);
		line.setStartY(sy);
		line.setEndX(ex);
		line.setEndY(ey);
		line.setAngle(0.00);
		line.setPaintColor(Color.BLACK);
		line.setLength(0);
		line.setName("lineName-"+lists.size());
		line.setPaintIsFull(false);
		line.setPaintWidth(6);
		line.setDescripte("描述"+lists.size());
		lists.add(line);
	}
	
	public boolean ExtendLine(List<LineBean> lines,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
<<<<<<< HEAD
=======
		for (int i = 0; i < lines.size(); i++) {
        	float sx=lines.get(i).getStartX();
        	float sy=lines.get(i).getStartY();
        	float ex=lines.get(i).getEndX();
        	float ey=lines.get(i).getEndY();
        	Logger.i("起点终点", "重绘前-->L"+i+": ("+(int)sx+","+(int)sy+")  ,  ("+(int)ex+","+(int)ey+")");
		}
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
		for (int i = 0; i < lines.size()-1; i++) {
			for (int j = i+1; j < lines.size(); j++) {
				int sxi=(int) lines.get(i).getStartX();
				int syi=(int) lines.get(i).getStartY();
				int exi=(int) lines.get(i).getEndX();
				int eyi=(int) lines.get(i).getEndY();  
				int sxj=(int) lines.get(j).getStartX();
				int syj=(int) lines.get(j).getStartY();
				int exj=(int) lines.get(j).getEndX();
				int eyj=(int) lines.get(j).getEndY();
				if ((sxi==sxj)&&(syi==syj)) {  
					int csx=Math.abs(dx-sxi);
					int csy=Math.abs(dy-syi);		
					Logger.i("共用端点", "第 i="+i+" 的头 , 第 j="+j+" 的头  :("+sxi+","+syi+")");				
					if (csx<40&&csy<40) {	
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getEndX();
						sy1=lines.get(i).getEndY();
						sx2=lines.get(j).getEndX();
						sy2=lines.get(j).getEndY();
						ex12=x;
						ey12=y;
<<<<<<< HEAD
//		            	Logger.i("起点终点", "---->Li"+i+": ("+sxi+","+syi+")  ,  ("+exi+","+eyi+")");
=======
//						Logger.i("线段总数", "重叠 Lines="+couplePointLines.size());
//		            	Logger.i("起点终点", "---->Li"+i+": ("+sxi+","+syi+")  ,  ("+exi+","+eyi+")");
//		            	Logger.i("起点终点", "---->Lj"+j+": ("+sxj+","+syj+")  ,  ("+exj+","+eyj+")");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((sxi==exj)&&(syi==eyj)) {     							
					int csx=Math.abs(dx-sxi);
					int csy=Math.abs(dy-syi);
<<<<<<< HEAD
=======
//					Logger.i("共用端点", "第 i="+i+" 的头 , 第 j="+j+" 的尾  :("+sxi+","+syi+")");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getEndX();
						sy1=lines.get(i).getEndY();
						sx2=lines.get(j).getStartX();
						sy2=lines.get(j).getStartY();
						ex12=x;
						ey12=y;
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((exi==sxj)&&(eyi==syj)) {     							
					int csx=Math.abs(dx-exi);
					int csy=Math.abs(dy-eyi);
<<<<<<< HEAD
=======
					Logger.i("共用端点", "第 i="+i+" 的尾 , 第 j="+j+" 的头  :("+exi+","+eyi+")");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStartX();
						sy1=lines.get(i).getStartY();
						sx2=lines.get(j).getEndX();
						sy2=lines.get(j).getEndY();
						ex12=x;
						ey12=y;
						lines.remove(j);
						lines.remove(i);
						return true;
					}
				}else if ((exi==exj)&&(eyi==eyj)) {     							
					int csx=Math.abs(dx-exi);
					int csy=Math.abs(dy-eyi);
<<<<<<< HEAD
=======
//					Logger.i("共用端点", "第 i="+i+" 的尾 , 第 j="+j+" 的尾  :("+exi+","+eyi+")");
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
					if (csx<40&&csy<40) {
						LineType=Contants.IS_TWO_LINES;
						sx1=lines.get(i).getStartX();
						sy1=lines.get(i).getStartY();
						sx2=lines.get(j).getStartX();
						sy2=lines.get(j).getStartY();
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
			int gspx=(int) lines.get(i).getStartX();
			int gspy=(int) lines.get(i).getStartY();
			int gepx=(int) lines.get(i).getEndX();
			int gepy=(int) lines.get(i).getEndY();
			int csx=Math.abs(dx-gspx);
			int csy=Math.abs(dy-gspy);
			int cex=Math.abs(dx-gepx);
			int cey=Math.abs(dy-gepy);
			if (csy<40&&csx<40) {
				LineType=Contants.IS_ONE_LINE;
				start_x=lines.get(i).getEndX();
				start_y=lines.get(i).getEndY();
				lines.remove(i);	
				return true;
			}else if (cex<40&&cey<40) {
				LineType=Contants.IS_ONE_LINE;
				start_x=lines.get(i).getStartX();
				start_y=lines.get(i).getStartY();
				lines.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public int PitchOnLine(List<LineBean> lines,float x,float y){
		Logger.i("线段总数", "Lines="+lines.size());
	    int dx=(int) x;
	    int dy=(int) y;			
		for (int i = 0; i < lines.size(); i++) {
			int gspx=(int) lines.get(i).getStartX();
			int gspy=(int) lines.get(i).getStartY();
			int gepx=(int) lines.get(i).getEndX();
			int gepy=(int) lines.get(i).getEndY();
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
	
	public void MoveLine_down(List<LineBean> lines,int i,float rx,float ry){
		start_mx=lines.get(i).getStartX();
		start_my=lines.get(i).getStartY();
		end_mx=lines.get(i).getEndX();
		end_my=lines.get(i).getEndY();
		getremoveline.setAngle(lines.get(i).getAngle());
		getremoveline.setLength(lines.get(i).getLength());
		getremoveline.setName(lines.get(i).getName());
		getremoveline.setDescripte(lines.get(i).getDescripte());
		getremoveline.setPaintColor(lines.get(i).getPaintColor());
		getremoveline.setPaintIsFull(lines.get(i).isPaintIsFull());
		getremoveline.setPaintWidth(lines.get(i).getPaintWidth());
		movePoint.set(rx, ry);
		lines.remove(i);
	}
	
	public void MoveLine_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	    	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	start_mx=start_mx+dx;
	    	start_my=start_my+dy;
	    	end_mx=end_mx+dx;
	    	end_my=end_my+dy; 			
	    }		
	}
	public void MoveLine_up(Canvas canvas,float rx,float ry){	    
		start_mx= ViewContans.AdsorbPoint((int)Math.floor(start_mx));
		start_my= ViewContans.AdsorbPoint((int)Math.floor(start_my));
		end_mx= ViewContans.AdsorbPoint((int)Math.floor(end_mx));
		end_my= ViewContans.AdsorbPoint((int)Math.floor(end_my)); 
		AddChangeLineParams(Getlines, start_mx, start_my, end_mx, end_my,
				getremoveline.getAngle(), getremoveline.getName(), 
				getremoveline.getDescripte(), getremoveline.getLength(),
				getremoveline.getPaintColor(), getremoveline.getPaintWidth(), getremoveline.isPaintIsFull());
		ViewContans.DrawLine(canvas, start_mx, start_my, end_mx, end_my, 
    			ViewContans.generatePaint(getremoveline.getPaintColor(), getremoveline.getPaintWidth(), getremoveline.isPaintIsFull()), point);
		getremoveline.setStartX(start_mx);
		getremoveline.setStartY(start_my);
		getremoveline.setEndX(end_mx);
		getremoveline.setEndY(end_my);
		if (getremoveline.getName().length()<8&&getremoveline.getName().length()>0) {
    		ViewContans.AddTextOnLine(getremoveline, canvas,getremoveline.getName(),-25);
		}            	
    	if (getremoveline.getLength()>0||getremoveline.getAngle()>0) {
    		ViewContans.AddTextOnLine(getremoveline, canvas,getremoveline.getLength()+"m "+getremoveline.getAngle()+"°",10);
		}
	}
	public void MoveLine_camera_up(Canvas canvas,float x,float y){    
		AddLinesParams(Getlines, start_mx,start_my,end_mx,end_my);
		drawLineFirstOnBitmap( start_mx,start_my,end_mx,end_my, canvas);
	}

	public int PitchOnLineToText(List<LineBean> lines,float x,float y){
		int i=PitchOnLine(lines, x, y);
		return i;
	}
	public void LineChangeToText(List<LineBean> lines,Canvas canvas,int i){
		Paint paint=ViewContans.generatePaint(Color.BLUE, 10,true);
		float sxi= lines.get(i).getStartX();
		float syi= lines.get(i).getStartY();
		float exi= lines.get(i).getEndX();
		float eyi= lines.get(i).getEndY(); 
		canvas.drawLine(sxi, syi, exi, eyi, paint);
	}
	
	public void AddTextOnLine(List<LineBean> list,Canvas canvas,int i,String text,int place){
		ViewContans.AddTextOnLine(list.get(i), canvas,text, place);
		list.get(i).setLength(Double.parseDouble(text));
	}
	public void DeleteLine(List<LineBean> lines,Canvas canvas){
	          
        Logger.i("线段总数", "before-->Lines="+lines.size());
        lines.remove(lines.size()-1);
        for (int i = 0; i < lines.size(); i++) {
		   	float sx=lines.get(i).getStartX();
		   	float sy=lines.get(i).getStartY();
		   	float ex=lines.get(i).getEndX();
		   	float ey=lines.get(i).getEndY();
		   
		   	Logger.i("起点终点", "undo-->L"+i+": ("+(int)sx+","+(int)sy+")  ,  ("+(int)ex+","+(int)ey+")");
		   	ViewContans.DrawLine(canvas, sx, sy, ex, ey, 
        			ViewContans.generatePaint(lines.get(i).getPaintColor(), lines.get(i).getPaintWidth(), lines.get(i).isPaintIsFull()), point);
		}
        Logger.i("线段总数", "after-->Lines="+lines.size());
        
	}
	public void ChangeLineAttributeAtIndex(List<LineBean> list
			,Canvas canvas,int index,LineBean line){
		float sx=list.get(index).getStartX();
    	float sy=list.get(index).getStartY();
    	float ex=list.get(index).getEndX();
    	float ey=list.get(index).getEndY();  
<<<<<<< HEAD
=======
//    	ViewContans.DrawLine(canvas, sx, sy, ex, ey, 
//    			ViewContans.generatePaint(line.getPaintColor(), line.getPaintWidth(), line.isPaintIsFull()), point);           	
//    	if (line.getName().length()<8&&line.getName().length()>0) {
//    		ViewContans.AddTextOnLine(list.get(index), canvas,line.getName(),-25);
//		}            	
//    	if (line.getLength()>0||line.getAngle()>0) {
//    		ViewContans.AddTextOnLine(list.get(index), canvas,line.getLength()+"m "+line.getAngle()+"°",10);
//		}
>>>>>>> fa4a202c7816980a0c716bb84e647853e5a4f4f4
    	list.remove(index);
    	AddChangeLineParams(list,
    			sx, sy, ex, ey, 
    			line.getAngle(), 
    			line.getName(),
    			line.getDescripte(), 
    			line.getLength(), 
    			line.getPaintColor(),
    			line.getPaintWidth(), 
    			line.isPaintIsFull());
	}
}
