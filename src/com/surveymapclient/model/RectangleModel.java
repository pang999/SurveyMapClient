package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;


import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.RectangleBean;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class RectangleModel {

	//画在Bitmap的线笔
	private Paint mPaint=ViewContans.generatePaint(Color.RED,4,true);
	//选中画笔
	private Paint checkpaint=ViewContans.generatePaint(Color.BLUE, 10,true);
	
	private float start_x,start_y,end_x,end_y;
	private PointF movePoint=new PointF();
	private float smx,smy,emx,emy;
	
    public List<RectangleBean> GetRectlist=new ArrayList<RectangleBean>();
    private RectangleBean rectangleBean=new RectangleBean();
		
	public void DrawRectangle(Canvas canvas){		
		canvas.drawRect(start_x, start_y, end_x, end_y, mPaint);
	}
	public void MoveDrawRectangle(Canvas canvas){	
		canvas.drawRect(smx, smy, emx, emy, checkpaint);		
	}

	public void drawRectangleOnBitmap(Canvas canvas){
		
		DrawRectangle(canvas);
		
	}
	public void Rectangle_touch_down(float x,float y){		
		start_x=end_x= ViewContans.AdsorbPoint((int)Math.floor(x));
		start_y=end_y= ViewContans.AdsorbPoint((int)Math.floor(y));	
	}
	public void Rectangle_camera_touch_down(float x,float y){		
		start_x=end_x=x;
		start_y=end_y=y;	
	}
	public void Rectangle_touch_move(float x,float y){		
		end_x=x;
		end_y=y;		
	}
	public void Rectangle_touch_up(float x,float y,Canvas canvas){		
		end_x= ViewContans.AdsorbPoint((int)Math.floor(x));
		end_y= ViewContans.AdsorbPoint((int)Math.floor(y));	
		AddRectangleParams(GetRectlist, start_x, start_y, end_x, end_y);
		DrawRectangle(canvas);
	}
	public void Rectangle_camera_touch_up(float x,float y,Canvas canvas){		
		end_x= x;
		end_y= y;	
		AddRectangleParams(GetRectlist, start_x, start_y, end_x, end_y);
		DrawRectangle(canvas);
	}
	public void MoveRectangle_down(List<RectangleBean> list,int i,float rx,float ry){
		smx=list.get(i).getStartX();
		smy=list.get(i).getStartY();
		emx=list.get(i).getEndX();
		emy=list.get(i).getEndY();
		movePoint.set(rx, ry);
		rectangleBean.setDescripte(list.get(i).getDescripte());
		rectangleBean.setRectName(list.get(i).getRectName());
		rectangleBean.setRectArea(list.get(i).getRectArea());
		rectangleBean.setRectLenght(list.get(i).getRectLenght());
		rectangleBean.setRectWidth(list.get(i).getRectWidth());
		rectangleBean.setPaintColor(list.get(i).getPaintColor());
		rectangleBean.setPaintWidth(list.get(i).getPaintWidth());
		rectangleBean.setFull(list.get(i).isFull());
		list.remove(i);
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
		canvas.drawRect(smx, smy, emx, emy,
    			ViewContans.generatePaint(rectangleBean.getPaintColor(), rectangleBean.getPaintWidth(), rectangleBean.isFull()));
		rectangleBean.setStartX(smx);
		rectangleBean.setStartY(smy);
		rectangleBean.setEndX(emx);
		rectangleBean.setEndY(emy);
		if (rectangleBean.getRectLenght()>0&&rectangleBean.getRectWidth()>0) {
			ViewContans.AddTextOnRectangle(rectangleBean, canvas, rectangleBean.getRectArea()+"", rectangleBean.getRectLenght()+"", rectangleBean.getRectWidth()+"");
		}
    	AddChangeRectangleParams(GetRectlist,smx, smy, emx, emy,
    			rectangleBean.getRectName(), rectangleBean.getDescripte(),
    			rectangleBean.getRectArea(), rectangleBean.getRectLenght(),
    			rectangleBean.getRectWidth(), rectangleBean.getPaintColor(),
    			rectangleBean.getPaintWidth(), rectangleBean.isFull());
	}
	public void MoveRectangle_camera_up(Canvas canvas){
		canvas.drawRect(smx, smy, emx, emy,
    			ViewContans.generatePaint(rectangleBean.getPaintColor(), rectangleBean.getPaintWidth(), rectangleBean.isFull()));
		rectangleBean.setStartX(smx);
		rectangleBean.setStartY(smy);
		rectangleBean.setEndX(emx);
		rectangleBean.setEndY(emy);
		if (rectangleBean.getRectLenght()>0&&rectangleBean.getRectWidth()>0) {
			ViewContans.AddTextOnRectangle(rectangleBean, canvas, rectangleBean.getRectArea()+"", rectangleBean.getRectLenght()+"", rectangleBean.getRectWidth()+"");
		}
    	AddChangeRectangleParams(GetRectlist,smx, smy, emx, emy,
    			rectangleBean.getRectName(), rectangleBean.getDescripte(),
    			rectangleBean.getRectArea(), rectangleBean.getRectLenght(),
    			rectangleBean.getRectWidth(), rectangleBean.getPaintColor(),
    			rectangleBean.getPaintWidth(), rectangleBean.isFull());
	}
	public int PitchOnRectangle(List<RectangleBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;	
	    for (int i = 0; i < lines.size(); i++) {
	    	int gspx=(int) lines.get(i).getStartX();
			int gspy=(int) lines.get(i).getStartY();
			int gepx=(int) lines.get(i).getEndX();
			int gepy=(int) lines.get(i).getEndY();
			boolean xCan=(gspx<dx&&dx<gepx)||(gspx>dx&&dx>gepx);
			boolean yCan=(gspy<dy&&dy<gepy)||(gspy>dy&&dy>gepy);
			if (xCan&&yCan) {
				return i;
			}
	    }
		return -1;
	}
	public void AddTextOnRectangle(List<RectangleBean> list,Canvas canvas,int i,String area,String lengh,String width){
		ViewContans.AddTextOnRectangle(list.get(i), canvas, area+"m2", lengh+"m",width+"m");
		list.get(i).setRectArea(Double.parseDouble(area));
		list.get(i).setRectLenght(Float.parseFloat(lengh));
		list.get(i).setRectWidth(Float.parseFloat(width));
	}

	public boolean ExtandRectangle(List<RectangleBean> lines,float x,float y){
		int dx=(int) x;
	    int dy=(int) y;
	    for (int i = 0; i < lines.size(); i++) {
	    	int sx=(int) lines.get(i).getStartX();
			int sy=(int) lines.get(i).getStartY();
			int ex=(int) lines.get(i).getEndX();
			int ey=(int) lines.get(i).getEndY();
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
	public void AddRectangleParams(List<RectangleBean> lists,float sx,float sy,float ex,float ey){
		RectangleBean rect=new RectangleBean();
		rect.setStartX(sx);
		rect.setStartY(sy);
		rect.setEndX(ex);
		rect.setEndY(ey);
		rect.setDescripte("描述");
		rect.setPaintColor(Color.RED);
		rect.setPaintWidth(4);
		rect.setFull(true);
		rect.setRectLenght(0);
		rect.setRectWidth(0);
		rect.setRectArea(0);
		rect.setRectName("rectangle"+lists.size());
		lists.add(rect);
	}
	public void AddChangeRectangleParams(List<RectangleBean> lists,float sx,float sy,float ex,float ey
		,String name,String desc,double area,float lengh,float width,int paintcolor,float paintwidth,boolean isfull ){
		RectangleBean rect=new RectangleBean();
		rect.setStartX(sx);
		rect.setStartY(sy);
		rect.setEndX(ex);
		rect.setEndY(ey);
		rect.setRectName(name);
		rect.setDescripte(desc);
		rect.setRectLenght(lengh);
		rect.setRectWidth(width);
		rect.setRectArea(area);
		rect.setPaintColor(paintcolor);
		rect.setPaintWidth(paintwidth);
		rect.setFull(isfull);	
		lists.add(rect);
	}
	public void DrawRectanleOnBitmap(List<RectangleBean> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			float sx=list.get(i).getStartX();
			float sy=list.get(i).getStartY();
			float ex=list.get(i).getEndX();
			float ey=list.get(i).getEndY();
			canvas.drawRect(sx, sy, ex, ey,
					ViewContans.generatePaint(list.get(i).getPaintColor(), list.get(i).getPaintWidth(),
							list.get(i).isFull()));
			if (list.get(i).getRectLenght()>0&&list.get(i).getRectWidth()>0) {
	    		Logger.i("BackRectangle", "Model颜色="+list.get(i).getPaintColor());
				ViewContans.AddTextOnRectangle(list.get(i), canvas, list.get(i).getRectArea()+"m2", list.get(i).getRectLenght()+"m", list.get(i).getRectWidth()+"m");
			}
		}
	}
	public void ChangeRectAttributeAtIndex(List<RectangleBean> rectlist,int index,Canvas canvas,RectangleBean rect){
		float sx=rectlist.get(index).getStartX();
    	float sy=rectlist.get(index).getStartY();
    	float ex=rectlist.get(index).getEndX();
    	float ey=rectlist.get(index).getEndY();    	
    	canvas.drawRect(sx, sy, ex, ey,
    			ViewContans.generatePaint(rect.getPaintColor(), rect.getPaintWidth(), rect.isFull()));
    	if (rect.getRectLenght()>0&&rect.getRectWidth()>0) {
    		Logger.i("BackRectangle", "Model颜色="+rect.getPaintColor());
			ViewContans.AddTextOnRectangle(rectlist.get(index), canvas, rect.getRectArea()+"m2", rect.getRectLenght()+"m", rect.getRectWidth()+"m");
		}
    	rectlist.remove(index);
    	AddChangeRectangleParams(rectlist,sx, sy, ex, ey,
    			rect.getRectName(), rect.getDescripte(),
    			rect.getRectArea(), rect.getRectLenght(),
    			rect.getRectWidth(), rect.getPaintColor(),
    			rect.getPaintWidth(), rect.isFull());
    	
	}
}
