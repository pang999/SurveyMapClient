package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.entity.RectangleLineBean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class RectangleModel {

	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.RED,4);
	//选中画笔
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10);
	
	private float start_x,start_y,end_x,end_y;
	private PointF movePoint=new PointF();
	private float smx,smy,emx,emy;
	
    public List<RectangleLineBean> getRectlist=new ArrayList<RectangleLineBean>();

		
	public void DrawRectangle(Canvas canvas){
		
		canvas.drawRect(start_x, start_y, end_x, end_y, mPaint);
	}
	public void MoveDrawRectangle(Canvas canvas){	
		canvas.drawRect(smx, smy, emx, emy, checkpaint);		
	}
	public void MoveDrawRectangleOnBitmap(Canvas canvas){	
		canvas.drawRect(smx, smy, emx, emy, mPaint);		
	}
	public void drawRectangleOnBitmap(Canvas canvas){
		
		DrawRectangle(canvas);
		
	}
	public void Rectangle_touch_down(float x,float y){		
		start_x=end_x= ViewContans.AdsorbPoint((int)Math.floor(x));
		start_y=end_y= ViewContans.AdsorbPoint((int)Math.floor(y));	
	}
	public void Rectangle_touch_move(float x,float y){		
		end_x=x;
		end_y=y;		
	}
	public void Rectangle_touch_up(float x,float y,Canvas canvas){		
		end_x= ViewContans.AdsorbPoint((int)Math.floor(x));
		end_y= ViewContans.AdsorbPoint((int)Math.floor(y));	
		AddRectangleParams(getRectlist, start_x, start_y, end_x, end_y);
		DrawRectangle(canvas);
	}
	public void MoveRectangle_down(List<RectangleLineBean> lines,int i,float rx,float ry){
		smx=lines.get(i).getStartpoint().x;
		smy=lines.get(i).getStartpoint().y;
		emx=lines.get(i).getEndpoint().x;
		emy=lines.get(i).getEndpoint().y;
		movePoint.set(rx, ry);
		lines.remove(i);
	}
	public void MoveRectangle_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	smx=smx+dx;
	    	smy=smy+dy;
	    	emx=emx+dx;
	    	emy=emy+dy; 			
	    }
	}
	public void MoveRectangle_up(Canvas canvas){
		smx= ViewContans.AdsorbPoint((int)Math.floor(smx));
		smy= ViewContans.AdsorbPoint((int)Math.floor(smy));
		emx= ViewContans.AdsorbPoint((int)Math.floor(emx));
		emy= ViewContans.AdsorbPoint((int)Math.floor(emy)); 
		AddRectangleParams(getRectlist, smx, smy, emx, emy);
		MoveDrawRectangleOnBitmap(canvas);
	}
	public int PitchOnRectangle(List<RectangleLineBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;	
	    for (int i = 0; i < lines.size(); i++) {
	    	int gspx=(int) lines.get(i).getStartpoint().x;
			int gspy=(int) lines.get(i).getStartpoint().y;
			int gepx=(int) lines.get(i).getEndpoint().x;
			int gepy=(int) lines.get(i).getEndpoint().y;
			boolean xCan=(gspx<dx&&dx<gepx)||(gspx>dx&&dx>gepx);
			boolean yCan=(gspy<dy&&dy<gepy)||(gspy>dy&&dy>gepy);
			if (xCan&&yCan) {
				return i;
			}
	    }
		return -1;
	}
	public boolean ExtandRectangle(List<RectangleLineBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < lines.size(); i++) {
	    	int sx=(int) lines.get(i).getStartpoint().x;
			int sy=(int) lines.get(i).getStartpoint().y;
			int ex=(int) lines.get(i).getEndpoint().x;
			int ey=(int) lines.get(i).getEndpoint().y;
//			boolean lefttop=((sx-40<dx)&&(sx+40>dx))&&((sy-40<dy)&&(sy+40>dy));
//			boolean leftbottom=((sx-40<dx)&&(sx+40>dx))&&((ey-40<dy)&&(ey+40>dy));
//			boolean righttop=((ex-40<dx)&&(ex+40>dx))&&((sy-40<dy)&&(sy+40>dy));
			boolean rightbottom=((ex-40<dx)&&(ex+40>dx))&&((ey-40<dy)&&(ey+40>dy));
			/*if (lefttop) {
				Logger.i("选中矩形", "lefttop");
				end_x=(float)ex;
				end_y=(float)ey;
				start_x=x;
				start_y=y;
				lines.remove(i);
				return true;
			}else if (leftbottom) {
				Logger.i("选中矩形", "leftbottom");
				start_x=x;
				start_y=(float)sy;
				end_x=(float)ex;
				end_y=y;
				lines.remove(i);
				return true;
			}else if (righttop) {
				Logger.i("选中矩形", "righttop");
				start_x=(float)sx;
				start_y=y;
				end_x=x;
				end_y=(float)ey;
				lines.remove(i);
				return true;
			}else*/ 
			if (rightbottom) {
				Logger.i("选中矩形", "rightbottom");
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
	public void AddRectangleParams(List<RectangleLineBean> lists,float sx,float sy,float ex,float ey){
		RectangleLineBean rect=new RectangleLineBean(new PointF(sx, sy), new PointF(ex, ey));
		rect.setDescripte("描述");
		rect.setWidth(4);
		rect.setFull(true);
		rect.setName("rectangle");
		lists.add(rect);
	}
	public void DrawRectanleOnBitmap(List<RectangleLineBean> lines,Canvas canvas){
		for (int i = 0; i < lines.size(); i++) {
			float sx=lines.get(i).getStartpoint().x;
			float sy=lines.get(i).getStartpoint().y;
			float ex=lines.get(i).getEndpoint().x;
			float ey=lines.get(i).getEndpoint().y;
			canvas.drawRect(sx, sy, ex, ey, mPaint);
		}
	}
}
