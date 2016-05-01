package com.surveymapclient.view;

import java.util.List;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.model.AudioModel;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;
import com.surveymapclient.model.TextModel;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MagnifierCameraView extends View{
	
	private LinesModel linesModel;
	private PolygonModel polygonModel;
	private TextModel textModel;
	private AudioModel audioModel;
	
	private Paint drawpaint;
	private Canvas  mcanvas;
    private Bitmap  canvasBitmap;
//    private Paint   mPaint;// 真实的画笔
    private Bitmap background;
    public int screen_x=0;
	public int screen_y=0;
	public boolean mIsLongPressed =false;//长按事件
	//长按事件参数值
	float lastx=0;
	float lasty=0;
	public boolean Yes=false;	

	public MagnifierCameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	//初始化画布
    public void initCanvas(){
    	drawpaint = new Paint(Paint.DITHER_FLAG);    
    	canvasBitmap = Bitmap.createBitmap(background);  
        mcanvas = new Canvas(canvasBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中      
        mcanvas.drawColor(Color.TRANSPARENT);
    }
    
    public void setimagebitmap(String path){
    	// 图片路径
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		float destWidth = dm.widthPixels;  
		float destHeight= dm.heightPixels ; 
		// 读取本地图片尺寸
		BitmapFactory.Options options=new BitmapFactory.Options();
		// 设置为true，options依然对应此图片，但解码器不会为此图片分配内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		
		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSampleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) { // 当图片长宽大于屏幕长宽时
			if (srcWidth < srcHeight) {
				inSampleSize = Math.round(srcHeight / destHeight);
			} else {
				inSampleSize = Math.round(srcWidth / destWidth);
			}
		}
		options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		Matrix matrix = new Matrix();
	    matrix.setRotate(90);
	    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    	this.background=bitmap;
    	initCanvas();
    }
    public void Aciton_Down(float x, float y) {
		
		if (DefineActivity.TYPE==Contants.SINGLE) {
			linesModel.Line_touch_down(x, y);
		}else if (DefineActivity.TYPE==Contants.CONTINU) {	
			polygonModel.Continuous_touch_down(x, y);
		}
		invalidate();
	}

	public void Action_Move(float x, float y) {
		screen_x=(int) x;
		screen_y=(int) y;
		if (DefineActivity.TYPE==Contants.SINGLE) {
			linesModel.Line_touch_move(x, y);
		}else if (DefineActivity.TYPE==Contants.CONTINU) {
			polygonModel.Continuous_touch_move(x, y);			
		}
		
        if (mIsLongPressed) {   
     		if (Yes) {    	
     			Yes=false;
     			if (linesModel.ExtendLine(linesModel.Getlines, x, y)) {
					DefineActivity.TYPE=Contants.SINGLE;
					DrawAllOnBitmap();
				} 
     			if (polygonModel.ExtendPolygon(polygonModel.GetpolyList, x, y)) {
					DefineActivity.TYPE=Contants.CONTINU_XETEND;
					DrawAllOnBitmap();
				}

			} 
     		if (DefineActivity.TYPE==Contants.SINGLE_XETEND) {
    			linesModel.Line_touch_move(x, y);
    		}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND) {
				polygonModel.ExtendPolygonLine(x, y);
			}
        }
		invalidate();
	}
	public void Action_Up(float x, float y) {	
		mIsLongPressed=false;		  
		if (DefineActivity.TYPE==Contants.SINGLE||DefineActivity.TYPE==Contants.SINGLE_XETEND) {			
			linesModel.Line_touch_up(x, y, mcanvas);
		}
		if (DefineActivity.TYPE==Contants.CONTINU) {  
        	polygonModel.Continuous_touch_up(x, y, mcanvas);    		
		}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND) {
			polygonModel.MovePolygon_up(mcanvas);
		}
	}
	public void DeleteLineIndex(int i){
		linesModel.Getlines.remove(i);
	}
	public void DeletePolygonIndex(int i){
		polygonModel.GetpolyList.remove(i);
	}
	public void MoveLineUp(LineBean lineBean){
		linesModel.Getlines.add(lineBean);
		DrawAllOnBitmap();
	}
	public void MovePolygonUp(PolygonBean polygonBean){
		polygonModel.GetpolyList.add(polygonBean);
		DrawAllOnBitmap();
	}
	public void ChangeLineAttribute(int index,LineBean line){		 
		//调用初始化画布函数以清空画布        
        linesModel.ChangeLineAttributeAtIndex(linesModel.Getlines, index, line);         
        DrawAllOnBitmap();
	}
	public void ChangeTextContent(int index,String text){
		textModel.ChangeTextContentAtIndex(textModel.GetTextlist,index,mcanvas,text);
		 DrawAllOnBitmap();
	}
	public void setManyTextOnView(float x,float y){
		textModel.Text_touch_up(mcanvas, x, y);
	}
	public void DeleteText(int index){
		textModel.GetTextlist.remove(index);
	}
	public void DeleteAudio(int index){
		audioModel.GetAudiolist.remove(index);
	}
	public void MoveTextUp(TextBean textBean){
		textModel.GetTextlist.add(textBean);
		DrawAllOnBitmap();
	}
	public void MoveAudioUp(AudioBean audioBean){
		audioModel.GetAudiolist.add(audioBean);
		DrawAllOnBitmap();
	}
	public void setAudioOnView(float x,float y,String url,int len){
		audioModel.Audio_touch_up(mcanvas, x, y, url, len);
	}
	public void ChangePolygonLineAttribute(int n, int index,LineBean line){
		 polygonModel.ChangePolygonLineAttributeAtIndex(polygonModel.GetpolyList,n,index,line);
		 DrawAllOnBitmap();
	 }
	public void DeletePolygonLineIndex(int n,int i){
		List<LineBean> lines=polygonModel.GetpolyList.get(n).getPolyLine();
		polygonModel.GetpolyList.remove(n);
		for (int j = 0; j < lines.size(); j++) {
			LineBean lineBean=new LineBean();
			lineBean.setName(lines.get(j).getName());
			lineBean.setStartX(lines.get(j).getStartX());
			lineBean.setStartY(lines.get(j).getStartY());
			lineBean.setEndX(lines.get(j).getEndX());
			lineBean.setEndY(lines.get(j).getEndY());
			lineBean.setDescripte(lines.get(j).getDescripte());	
			lineBean.setPaintColor(lines.get(j).getPaintColor());
			lineBean.setPaintWidth(lines.get(j).getPaintWidth());
			lineBean.setPaintIsFull(lines.get(j).isPaintIsFull());
			linesModel.Getlines.add(lineBean);							
		}
		linesModel.Getlines.remove(i);		
//		DrawAllOnBitmap();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);	
		layout(-screen_x+100, -screen_y+100, 
				Contants.sreenWidth*3-screen_x+50,Contants.screenHeight*3-screen_y+50);
		canvas.scale(2, 2, screen_x, screen_y);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(canvasBitmap, 0, 0, drawpaint);     //显示旧的画布  
		polygonModel.DrawPath(canvas);
		if (DefineActivity.TYPE==Contants.SINGLE||DefineActivity.TYPE==Contants.SINGLE_XETEND) {
			if (linesModel.LineType==Contants.IS_TWO_LINES) {
        		linesModel.DrawLines(canvas);
        	}else{
        		linesModel.DrawLine(canvas);
    		}
		}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND) {
			polygonModel.ExtendDrawPolygon(canvas);
		}		
	}


	private void DrawAllOnBitmap(){
		try {
			initCanvas();
			linesModel.DrawLinesOnBitmap(linesModel.Getlines, mcanvas);
			textModel.DrawTextOnBitmap(textModel.GetTextlist, mcanvas);
			polygonModel.DrawPolygonsOnBitmap(polygonModel.GetpolyList,mcanvas);
			audioModel.DrawAudiosOnBitmap(audioModel.GetAudiolist, mcanvas);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
