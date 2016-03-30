package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.common.GLFont;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.TextBean;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

public class TextModel {
	
//	private Paint mPaint=ViewContans.generatePaint(Color.BLACK, 4,true);
	public List<TextBean> getTextlist=new ArrayList<TextBean>();
	private float loca_x,loca_y;
	private TextBean tb;
	Paint setpaint=ViewContans.generateTextPaint();
	private PointF movePoint=new PointF();
	public void DrawText(Canvas canvas){	
		canvas.drawText("移动文字", loca_x, loca_y, setpaint);	
	}
	public void DrawTextList(List<TextBean> lists,Canvas canvas){
		for (int i = 0; i < lists.size(); i++) {
			canvas.drawText(lists.get(i).getText(), lists.get(i).getLocaPoint().x,
					lists.get(i).getLocaPoint().y, lists.get(i).getPaint());
		}
	}
	public void Text_touch_move(float x,float y){
		loca_x=x;
		loca_y=y;
	}
	public void MoveText_down(List<TextBean> lists,int i,float rx,float ry){
		loca_x=lists.get(i).getLocaPoint().x;
		loca_y=lists.get(i).getLocaPoint().y;
//		tb=new TextBean(new PointF(x, y));
//		tb.setText(lists.get(i).getText());
//		tb.setPaint(lists.get(i).getPaint());
		movePoint.set(rx, ry);
		lists.remove(i);
	}
	public void MoveText_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	    	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	loca_x=loca_x+dx;
	    	loca_y=loca_y+dy;
	    }
	}
	public void MoveText_up(Canvas canvas){
		DrawText(canvas);
		AddMoveTextParams(getTextlist);
	}
	public void Text_touch_up(Canvas canvas,float x,float y){
		loca_x=x;
		loca_y=y;
		DrawText(canvas);
		AddTextParams(getTextlist);
	}
	public void AddMoveTextParams(List<TextBean> lists){
		TextBean textBean=new TextBean(new PointF(loca_x, loca_y));
		textBean.setText("多边形介绍");
		textBean.setPaint(setpaint);
		lists.add(textBean);
	}
	public void AddTextParams(List<TextBean> lists){
		TextBean textBean=new TextBean(new PointF(loca_x, loca_y));
		textBean.setText("多边形介绍等");
		textBean.setPaint(setpaint);
		lists.add(textBean);
	}
	
	public int PitchOnText(List<TextBean> lists,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < lists.size(); i++) {
			int lx=(int) lists.get(i).getLocaPoint().x;
			int ly=(int) lists.get(i).getLocaPoint().y;
			int m=lists.get(i).getText().length()/10;
			int height=(int) GLFont.getFontHeight(lists.get(i).getPaint());
			if (m>=1) {
				height=height*m;
			}
			int width=0;
			if (m==0) {
				width=getTextWidth(lists.get(i).getPaint(), lists.get(i).getText());
			}else {
				width=getTextWidth(lists.get(i).getPaint(), lists.get(i).getText().substring(0, 9));
			}
			Logger.i("获取文字", "width="+width+",height="+height);
			boolean xCan=(lx<dx)&&((lx+width)>dx);
			boolean yCan=(ly<dy)&&((ly+height)>dy);
			if (xCan&&yCan) {
				return i;
			}		
		}
		return -1;
	}
	public int getTextWidth(Paint paint, String str) {  
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
