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
	public List<TextBean> GetTextlist=new ArrayList<TextBean>();
	private float loca_x,loca_y;
	private TextBean tb=new TextBean();
	Paint setpaint=ViewContans.generateTextPaint();
	private PointF movePoint=new PointF();
	public void DrawText(Canvas canvas){	
		canvas.drawText("Text", loca_x, loca_y, setpaint);	
	}
	public void DrawMoveText(Canvas canvas){
		canvas.drawText(tb.getText(),loca_x,loca_y, setpaint);
	}
	public void DrawTextOnBitmap(List<TextBean> lists,Canvas canvas){
		for (int i = 0; i < lists.size(); i++) {
			canvas.drawText(lists.get(i).getText(), lists.get(i).getTx(),
					lists.get(i).getTy(), setpaint);
		}
	}
//	public void Text_touch_move(float x,float y){
//		loca_x=x;
//		loca_y=y;
//	}
	public void Text_touch_up(Canvas canvas,float x,float y){
		loca_x=x;
		loca_y=y;
		DrawText(canvas);		
		AddTextParams(GetTextlist);
	}
	public void MoveText_down(List<TextBean> lists,int i,float rx,float ry){
		loca_x=lists.get(i).getTx();
		loca_y=lists.get(i).getTy();
		movePoint.set(rx, ry);
		tb.setText(lists.get(i).getText());
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
		canvas.drawText(tb.getText(),loca_x,loca_y, setpaint);
		AddChangeTextParams(GetTextlist,loca_x,loca_y,tb.getText());
	}
	
	public void ChangeTextContentAtIndex(List<TextBean> list,int index,Canvas canvas,String text){
		float x=list.get(index).getTx();
		float y=list.get(index).getTy();
//		canvas.drawText(text,x,y, setpaint);		
		list.remove(index);
		AddChangeTextParams(list,x, y,text);
	}
	
	public void AddChangeTextParams(List<TextBean> lists,float x,float y,String text){
		TextBean textBean=new TextBean();
		textBean.setText(text);
		textBean.setTx(x);
		textBean.setTy(y);
		lists.add(textBean);
	}
	public void AddTextParams(List<TextBean> lists){
		TextBean textBean=new TextBean();
		textBean.setText("Text");
		textBean.setTx(loca_x);
		textBean.setTy(loca_y);
		lists.add(textBean);
	}
	
	public int PitchOnText(List<TextBean> lists,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < lists.size(); i++) {
			int lx=(int) lists.get(i).getTx();
			int ly=(int) lists.get(i).getTy();
			int m=lists.get(i).getText().length()/10;
			int height=(int) GLFont.getFontHeight(setpaint);
			if (m>=1) {
				height=height*m;
			}
			int width=0;
			if (m==0) {
				width=getTextWidth(setpaint, lists.get(i).getText());
			}else {
				width=getTextWidth(setpaint, lists.get(i).getText().substring(0, 9));
			}
			Logger.i("»ñÈ¡ÎÄ×Ö", "width="+width+",height="+height);
			boolean xCan=(lx-40<dx)&&((lx+width+40)>dx);
			boolean yCan=(ly-80<dy)&&((ly+height+40)>dy);
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
