package com.surveymapclient.view;

import java.util.ArrayList;
import java.util.List;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.TextBean;
import com.surveymapclient.impl.CallBackData;
import com.surveymapclient.impl.DialogCallBack;
import com.surveymapclient.impl.VibratorCallBack;
import com.surveymapclient.model.AudioModel;
import com.surveymapclient.model.LineAndPolygonModel;
import com.surveymapclient.model.LinesModel;
import com.surveymapclient.model.PolygonModel;
import com.surveymapclient.model.TextModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DefineView extends View{
	
	DialogCallBack dialogCallBack;
	VibratorCallBack vibratorCallBack;	
	CallBackData callBackData;
	
    private Canvas  mCanvas;
    private Bitmap  mBitmap;    
    	
    final int width = Contants.sreenWidth*3;
	final int height = Contants.screenHeight*3;
	public int screen_x=-Contants.sreenWidth*1;
	public int screen_y=-Contants.screenHeight*1;
	
	private boolean isFirst=false;
	
	private float startDistance;

	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	private int mode = DRAG;
	// ��һ�����µ���ָ�ĵ�   
    private PointF dragPoint = new PointF();
	private float scale=1;

    //��ֱ��
    private boolean isMovingdraw=false;
    private boolean mIsLongPressed =false;//�����¼�
    private boolean mIsShortPressed=false;
    private boolean isMoveLinedrag=false;

    private boolean isLineWriteText=false;
    private boolean isPolygonLineWriteText=false;
    //ѡ��ֱ��
	private static int m=-1;
	private static int n=-1;
    
    private LinesModel linesModel;
    private PolygonModel polygonModel;
    private LineAndPolygonModel linepoly;
    private TextModel textModel;
    private AudioModel audioModel;
    
    public DefineView(Context context) {
		// TODO Auto-generated constructor stub
    	super(context);
	}
    
	public DefineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		linesModel=new LinesModel();
		polygonModel=new PolygonModel();
		linepoly=new LineAndPolygonModel();
		textModel=new TextModel();
		audioModel=new AudioModel(context);			
		dialogCallBack=(DialogCallBack) context;
		vibratorCallBack=(VibratorCallBack) context;
		callBackData=(CallBackData) context;
		isFirst=true;	
        DefineActivity.TYPE=Contants.DRAG;
        initCanvas();	
	}
	//��ʼ������
    public void initCanvas(){      
        //������С 
        mBitmap = Bitmap.createBitmap(Contants.sreenWidth*3, Contants.screenHeight*3, 
            Bitmap.Config.RGB_565);      
        mCanvas = new Canvas(mBitmap);  //����mCanvas���Ķ���������������mBitmap��      
        ViewContans.initCanvasHuabu(mCanvas,mBitmap,scale);         
    }
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isFirst) {
			isFirst=false;
			layout(screen_x, screen_y, 3*Contants.sreenWidth+screen_x, 3*Contants.screenHeight+screen_y);
		}		
//	    ViewContans.initCanvasHuabu(canvas,mBitmap,scale);
		canvas.scale(scale, scale,width/2,height/2);
		// ��ǰ���Ѿ���������ʾ����
	    canvas.drawBitmap(mBitmap, 0, 0, null);     //��ʾ�ɵĻ���         
		polygonModel.DrawPath(canvas);
        if (DefineActivity.TYPE==Contants.SINGLE&&isMovingdraw||DefineActivity.TYPE==Contants.SINGLE_XETEND) {       	
        	if (isMoveLinedrag) {
				linesModel.MoveDrawLine(canvas);
			}else {
				if (linesModel.LineType==Contants.IS_TWO_LINES) {
	        		linesModel.DrawLines(canvas);
	        	}else{
	        		linesModel.DrawLine(canvas);
	    		}	
			}          	
		}
        if (DefineActivity.TYPE==Contants.CONTINU&&isMovingdraw) {
        	if (isMoveLinedrag) {
        		polygonModel.MoveDrawPolygon(canvas);
			}			
		}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND&&isMovingdraw) {
			polygonModel.ExtendDrawPolygon(canvas);
		}else if (DefineActivity.TYPE==Contants.TEXT) {
			textModel.DrawMoveText(canvas);
		}else if (DefineActivity.TYPE==Contants.AUDIO) {
			audioModel.DrawMoveAudio(canvas);
		}if (isLineWriteText) {
        	linesModel.LineChangeToText(linesModel.Getlines, canvas, m);
		}if (isPolygonLineWriteText) {		
			polygonModel.PolygonLineChangeToText(polygonModel.GetpolyList.get(n).getPolyLine(), canvas, m);
		} 
		
	}

	//�����¼�����ֵ
	float lastx=0;
	float lasty=0;
	long lastDownTime=0;
	
	boolean Yes=false;	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		float rx=event.getRawX();
		float ry=event.getRawY();
		// TODO Auto-generated method stub
		switch (event.getAction()& MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN: 
        	isMovingdraw=true;
        	lastx=x;
        	lasty=y;
        	lastDownTime= event.getDownTime();   
			if (DefineActivity.TYPE==Contants.SINGLE) {							
				int i=linesModel.PitchOnLine(linesModel.Getlines, x, y);
				if (i>=0) {				
					isMoveLinedrag=true;
					linesModel.MoveLine_down(linesModel.Getlines, i, rx, ry);
					callBackData.onCallBackDeleteLine(i);
					DrawAllOnBitmap();
				}else {
					linesModel.Line_touch_down(x, y);
					callBackData.onCallBackDown(x, y);
				}	
				int p=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
        		int pl=polygonModel.PitchOnPolygonLine(polygonModel.GetpolyList, x, y);
        		if (p>=0) {
        			n=p;
        			if (pl>=0) {
        				List<LineBean> lines=polygonModel.GetpolyList.get(n).getPolyLine();
        				polygonModel.GetpolyList.remove(n);
        				int m=linesModel.Getlines.size();  
        				Logger.i("GetlineList","before="+ linesModel.Getlines.size());
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
//        				linesModel.Getlines=lines;
        				Logger.i("GetlineList","after="+ linesModel.Getlines.size());
        				isMoveLinedrag=true;
    					linesModel.MoveLine_down(linesModel.Getlines,m+ pl, rx, ry);   					
    					callBackData.onCallBackDeletePolygonLine(n, m+pl);
    					DrawAllOnBitmap();
        			}
				}
				
			}else if (DefineActivity.TYPE==Contants.CONTINU) {	
				int i=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
				if (i>=0) {
					isMoveLinedrag=true;
					Logger.i("���ж����", "i="+i);
					polygonModel.MovePolygon_down(polygonModel.GetpolyList, i, rx, ry);
					callBackData.onCallBackDeletePolygon(i);
					DrawAllOnBitmap();
				}else {
					polygonModel.Continuous_touch_down(x, y);
					callBackData.onCallBackDown(x, y);
				}								
			}else if(DefineActivity.TYPE==Contants.DRAG){
				mIsShortPressed=false;
				drag_touch_start(rx, ry);
			}
			int ti=textModel.PitchOnText(textModel.GetTextlist, x, y);
			if (ti>=0) {
				DefineActivity.TYPE=Contants.TEXT;
				Logger.i("��ȡ����", "i="+ti);
				textModel.MoveText_down(textModel.GetTextlist, ti, rx, ry);
				DrawAllOnBitmap();
				callBackData.onCallBackDeleteText(ti);
			}
			int au=audioModel.PitchOnAudio(audioModel.GetAudiolist, x, y);
			if (au>=0) {
				DefineActivity.TYPE=Contants.AUDIO;
				audioModel.MoveAudio_dowm(audioModel.GetAudiolist, au, rx, ry);
				DrawAllOnBitmap();
				callBackData.onCallBackDeleteAudio(au);
			}
    	    invalidate();
            break;
        case MotionEvent.ACTION_POINTER_DOWN: //�����һֻ��ָ������Ļ����������һ����ָ����     // ��ֻ��ָ����
        	zoom_down(event);
		    invalidate();
			break;
        case MotionEvent.ACTION_MOVE:   
        	Logger.i("��Ļ����ͼ", "��Ļ:x="+getLeft()+",screem_x="+screen_x+",RowX="+(int)rx+"����ͼ:x="+(int)x+"; ��Ļ:y="+getTop()+",screem_y="+screen_y+",RowY="+(int)ry+",��ͼ:y="+(int)y);
            if (DefineActivity.TYPE==Contants.SINGLE) {       	
            	if (isMoveLinedrag) {        		
					linesModel.MoveLine_move(rx, ry);
				}else {					
	    			linesModel.Line_touch_move(x, y);
	    			callBackData.onCallBackMove(x, y);
				}          	
            	
			}else if (DefineActivity.TYPE==Contants.CONTINU) {
				if (isMoveLinedrag) {
					polygonModel.MovePolygon_move(rx, ry);
				}else {
					polygonModel.Continuous_touch_move(x, y);
					callBackData.onCallBackMove(x, y);
				}			
			}else if (DefineActivity.TYPE==Contants.TEXT) {
				textModel.MoveText_move(rx, ry);
			}else if (DefineActivity.TYPE==Contants.AUDIO) {
				audioModel.MoveAudio_move(rx, ry);
			}else if(DefineActivity.TYPE==Contants.DRAG) {				
				if (mode==DRAG) {
	            	drag_touch_move(rx, ry);
				}else if(mode==ZOOM){
					setzoomcanvas(event);
				}
			}	
            if (!mIsLongPressed) {           	
            	mIsLongPressed=ViewContans.isLongPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 750);
            	Yes=true;
        	}
            if (mIsLongPressed) {        		
        		if (Yes) {
        			Yes=false;        			        			 
        			if (linesModel.ExtendLine(linesModel.Getlines, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.SINGLE_XETEND;
						DrawAllOnBitmap();
						callBackData.onCallBackDown(x, y);	
						callBackData.onCallBackLong(true,true);
					} 
        			if (polygonModel.ExtendPolygon(polygonModel.GetpolyList, x, y)) {
						vibratorCallBack.onVibratorCallBack();
						DefineActivity.TYPE=Contants.CONTINU_XETEND;
						DrawAllOnBitmap();
						callBackData.onCallBackDown(x, y);
						callBackData.onCallBackLong(true,true);
					}
        			Logger.i("����ʱ����", "����ʱ����");      			
				}          		
        		if (DefineActivity.TYPE==Contants.SINGLE_XETEND) {
					linesModel.Line_touch_move(x, y);
					callBackData.onCallBackMove(x, y);					
				}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND) {
					polygonModel.ExtendPolygonLine(x, y);
					callBackData.onCallBackMove(x, y);
				}
			}
    	    invalidate();
            break;
        case MotionEvent.ACTION_UP:
        	isMovingdraw=false;
        	mIsLongPressed=false;
			if (DefineActivity.TYPE==Contants.CONTINU) {  
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					polygonModel.MovePolygon_up(mCanvas);
					int m=polygonModel.GetpolyList.size()-1;
					callBackData.onCallBackMovePolygon(polygonModel.GetpolyList.get(m));
				}else {
	        		polygonModel.Continuous_touch_up(x, y, mCanvas);
	        		callBackData.onCallBackUp(x, y);
				}
        		
			}else if (DefineActivity.TYPE==Contants.CONTINU_XETEND) {
				polygonModel.MovePolygon_up(mCanvas);				
				callBackData.onCallBackUp(x, y);
			}else if (DefineActivity.TYPE==Contants.SINGLE||DefineActivity.TYPE==Contants.SINGLE_XETEND) {						
				if (isMoveLinedrag) {
					isMoveLinedrag=false;
					linesModel.MoveLine_up(mCanvas, rx, ry);
					int m=linesModel.Getlines.size()-1;
					callBackData.onCallBackMoveLine(linesModel.Getlines.get(m));
				}else {
					mIsLongPressed=false;				
					linesModel.Line_touch_up(x, y , mCanvas);	
					callBackData.onCallBackUp(x, y);
				}
				if (linesModel.Getlines.size()>=3) {					
					linepoly.CalculatePolyFromlist(linesModel.Getlines);
					for (int i = 0; i < linepoly.GetPolyInt.size(); i++) {
						Logger.i("ֱ��ʣ������", "GetPolyInt="+linepoly.GetPolyInt.get(i).intValue());
						linesModel.Getlines.remove(linepoly.GetPolyInt.get(i).intValue());
					}
					for (int i = 0; i < linepoly.backpolylist.size(); i++) {						
						PolygonBean polygonBean=new PolygonBean();
						List<LineBean> lineslist=new ArrayList<LineBean>();
						for (int j = 0; j < linepoly.backpolylist.get(i).getPolyLine().size(); j++) {
							LineBean lineBean=new LineBean();
							lineBean.setName("lines");
							lineBean.setStartX(linepoly.backpolylist.get(i).getPolyLine().get(j).getStartX());
							lineBean.setStartY(linepoly.backpolylist.get(i).getPolyLine().get(j).getStartY());
							lineBean.setEndX(linepoly.backpolylist.get(i).getPolyLine().get(j).getEndX());
							lineBean.setEndY(linepoly.backpolylist.get(i).getPolyLine().get(j).getEndY());
							lineBean.setDescripte("����-"+j);	
							lineBean.setPaintColor(Color.BLACK);
							lineBean.setPaintWidth(6);
							lineBean.setPaintIsFull(false);
							lineslist.add(lineBean);
						}				
						polygonBean.setPolyLine(lineslist);
						polygonModel.GetpolyList.add(polygonBean);
						DrawAllOnBitmap();
					}
					linepoly.ClearInt();					
				}	
								
			}else if (DefineActivity.TYPE==Contants.TEXT) {
				textModel.MoveText_up(mCanvas);	
				int m=textModel.GetTextlist.size()-1;
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
				if (mIsShortPressed) {
					int tx=textModel.PitchOnText(textModel.GetTextlist, x, y);
	        		if (tx>=0) {
	        			Logger.i("ѡ������", "t="+tx);
						dialogCallBack.onDialogCallBack(textModel.GetTextlist.get(tx), tx);
					}
				}
				callBackData.onCallBackMoveTextUp(textModel.GetTextlist.get(m));
			}else if (DefineActivity.TYPE==Contants.AUDIO) {
				audioModel.MoveAudio_up(mCanvas);
				int m=audioModel.GetAudiolist.size()-1;
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
				if (mIsShortPressed) {
					int aud=audioModel.PitchOnAudio(audioModel.GetAudiolist, x, y);
					if (aud>=0) {
						dialogCallBack.onDialogCallBack(audioModel.GetAudiolist.get(aud), aud);
					}
				}
				callBackData.onCallBackMoveAudioUp(audioModel.GetAudiolist.get(m));
			}else {
				mIsShortPressed=ViewContans.isShortPressed(lastx, lasty, x, y, lastDownTime, event.getEventTime(), 200);												      	      	
				if (mIsShortPressed) {
	        		int l=linesModel.PitchOnLine(linesModel.Getlines, x, y);
	        		if (l>=0) {
	    				dialogCallBack.onDialogCallBack(linesModel.Getlines.get(l), l,0);
					}
	        		int p=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, x, y);
	        		int pl=polygonModel.PitchOnPolygonLine(polygonModel.GetpolyList, x, y);
	        		if (p>=0) {
	        			n=p;
	        			if (pl>=0) {
		        			dialogCallBack.onDialogCallBack(polygonModel.GetpolyList.get(p).getPolyLine().get(pl),pl,2);
						}else
							if (p>=0) {
		        			dialogCallBack.onDialogCallBack(polygonModel.GetpolyList.get(p), p);
		        			Logger.i("ѡ�ж����", "p="+p);
		        		}
					}        		
				}
			}			
			typeListener.onTypeChange();
			DefineActivity.TYPE=Contants.DRAG;
    	    invalidate();
            break;
        case MotionEvent.ACTION_POINTER_UP: //һֻ��ָ�뿪��Ļ��������һֻ��ָ������ᴥ���¼�
			//ʲô��û��
			mode = NONE;
			break;
        }
		return true;
	}
	private void DrawAllOnBitmap(){
		try {
			initCanvas(); 
			linesModel.DrawLinesOnBitmap(linesModel.Getlines, mCanvas);
			textModel.DrawTextOnBitmap(textModel.GetTextlist, mCanvas);
			polygonModel.DrawPolygonsOnBitmap(polygonModel.GetpolyList,mCanvas);
			audioModel.DrawAudiosOnBitmap(audioModel.GetAudiolist, mCanvas);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void AddAllDataFromActivity(
			List<LineBean> linelist,
			List<PolygonBean> polygonlist,
			List<TextBean> textlist,
			List<AudioBean> audiolist){
		linesModel.Getlines=linelist;
		polygonModel.GetpolyList=polygonlist;
		textModel.GetTextlist=textlist;
		audioModel.GetAudiolist=audiolist;
		DrawAllOnBitmap();
	}
	/**
	 * ������ק
	 * @param rx
	 * @param ry
	 */
	private void drag_touch_start(float rx,float ry){
		dragPoint.set(rx,ry);
        mode=DRAG;
	}
	private void drag_touch_move(float rx,float ry){
		float dx=rx-dragPoint.x;
	    float dy=ry-dragPoint.y;
		if (Math.sqrt(dx*dx+dy*dy)>5f) {
			dragPoint.set(rx,ry);  				
			screen_x+=(int)dx;
			screen_y+=(int)dy;
		    layout(screen_x, screen_y, width+screen_x, height+screen_y);
		    DefineActivity.LocationXY(screen_x, screen_y);
		}
	}
	/**
	 * ������ֵ�ֱ����
	 * @param rx
	 * @param ry
	 * @return
	 */
	public boolean SetTextToLine(int rx,int ry){
		float gx=(float)(-screen_x+rx);
		float gy=(float)(-screen_y+ry-50);
		int i=linesModel.PitchOnLineToText(linesModel.Getlines, gx, gy);
		if (i>=0) {
			m=i;			
			return true;
		}else {		
			return false;
		}		
	}
	public boolean SetTextToPolygonLine(int rx,int ry){
		float gx=(float)(-screen_x+rx);
		float gy=(float)(-screen_y+ry-50);
		int p=polygonModel.PitchOnPolygon(polygonModel.GetpolyList, gx, gy);		
		if (p>=0) {
			n=p;
			int i=linesModel.PitchOnLineToText(polygonModel.GetpolyList.get(p).getPolyLine(), gx, gy);
			if (i>=0) {
				m=i;
				return true;
			}else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	public void setManyTextOnView(){
		Logger.i("����λ��", "x="+(Contants.sreenWidth/2-screen_x)+"y="+(Contants.screenHeight/2-screen_y));
		textModel.Text_touch_up(mCanvas, (Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y));
		callBackData.onCallBackText((Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y));
	}
	public void setAudioOnView(String url,int len){		
		audioModel.Audio_touch_up(mCanvas, (Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y), url,len);
		callBackData.onCallBackAudio((Contants.sreenWidth/2-screen_x), (Contants.screenHeight/2-screen_y), url, len);
	}
	public void SetwriteLineText(String text){
		linesModel.AddTextOnLine(linesModel.Getlines, mCanvas, m,text,10);		
	}
	public void SetwritePolygonLineText(String text){
		polygonModel.AddTextOnPolygonLine(polygonModel.GetpolyList.get(n).getPolyLine(), mCanvas,m, text, 10);	
	}
	public void LineChangeToText(){
		isLineWriteText=true;
		invalidate();
	}
	public void LineNoChangeToText(){
		isLineWriteText=false;
		invalidate();
	}
	public void PolygonLineChangeToText(){
		isPolygonLineWriteText=true;
		invalidate();
	}
	public void PolygonLineNoChangeToText(){
		isPolygonLineWriteText=false;
		invalidate();
	}
	public List<LineBean> BackLinelist(){
		return linesModel.Getlines;
	}
	public List<PolygonBean> BackPolylist(){
		return polygonModel.GetpolyList;
	}
	public List<TextBean> BackTextlist(){
		return textModel.GetTextlist;
	}
	public List<AudioBean> BackAudiolist(){
		return audioModel.GetAudiolist;
	}
	/**
	 * ��������
	 * @param event
	 */
	private void zoom_down(MotionEvent event){
		startDistance = ViewContans.distance(event);//��������ľ���
		if(startDistance > 5f) {  //������ָ֮�����С�������ش���5����Ϊ�Ƕ�㴥��
			mode = ZOOM;
		}
	}
	private void setzoomcanvas(MotionEvent event){
		float distance = ViewContans.distance(event);  //����֮��ľ���
		if(distance > 5f) {
			scale = distance / startDistance;	
		}
	}
	 
	 /**
	  * �����ĺ���˼����ǽ�������գ�
	  * ������������Path·�����һ���Ƴ�����
	  * ���½�·�����ڻ������档
	  */
	 public void UnDo(){
	     if (linesModel.Getlines!=null&&linesModel.Getlines.size()>0) {
	    	//���ó�ʼ��������������ջ���
	         initCanvas();  
	         linesModel.DeleteLine(linesModel.Getlines, mCanvas);
	         invalidate();
		 }	 
     }
	 /**
	  * ɾ��ĳ��
	  */
	 public void RemoveIndexLine(int index){
         if(linesModel.Getlines != null && linesModel.Getlines.size() > 0){
            //���ó�ʼ��������������ջ���
        	linesModel.Getlines.remove(index);
            DrawAllOnBitmap();                       
            invalidate();// ˢ��
         }
     }
	 public void RemoveIndexPolygon(int index){
		 if (polygonModel.GetpolyList!=null&&polygonModel.GetpolyList.size()>0) {
			polygonModel.GetpolyList.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void RemoveIndexPolygonLine(int index){
		 if (polygonModel.GetpolyList!=null&&polygonModel.GetpolyList.size()>0){	
			 List<LineBean> lines=polygonModel.GetpolyList.get(n).getPolyLine();
			 polygonModel.GetpolyList.remove(n);
			 lines.remove(index);
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
			 DrawAllOnBitmap();				 
			 invalidate();
		 }
	 }
	 public void RemoveIndexText(int index){
		 if (textModel.GetTextlist!=null&&textModel.GetTextlist.size()>0) {
			textModel.GetTextlist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void RemoveIndexAudio(int index){
		 if (audioModel.GetAudiolist!=null&&audioModel.GetAudiolist.size()>0) {
			audioModel.GetAudiolist.remove(index);
			DrawAllOnBitmap();
			invalidate();
		}
	 }
	 public void ChangeLineAttribute(int index,LineBean line){		 
		//���ó�ʼ��������������ջ���        
         linesModel.ChangeLineAttributeAtIndex(linesModel.Getlines, index, line);         
         DrawAllOnBitmap();
         callBackData.onCallBackLineAttribute(index, line);
	 }
	 public void ChangePolygonLineAttribute(int index,LineBean line){
		 polygonModel.ChangePolygonLineAttributeAtIndex(polygonModel.GetpolyList,n,index,line);
		 DrawAllOnBitmap();
		 callBackData.onCallBaclPolygonLineAttribute(n, index, line);
	 }
	 public void ChangeTextContent(int index,String text){		 
		 textModel.ChangeTextContentAtIndex(textModel.GetTextlist,index,mCanvas,text);
		 DrawAllOnBitmap();
		 callBackData.onCallBackChangeTextContent(index, text);
	 }
	 public void LocationXY(){
		 screen_x=-Contants.sreenWidth;
		 screen_y=-Contants.screenHeight;
		 layout(screen_x, screen_y, width+screen_x, height+screen_y);
		 DefineActivity.LocationXY(screen_x, screen_y);
	 }
	 public void BackLocation(){
		 layout(screen_x, screen_y, width+screen_x, height+screen_y);
	 }
	 //����ͼƬ
	 public String SavaBitmap(){
		return ViewContans.saveBitmap(mBitmap);
	 }	
	 //���������С��ʼ��
	 public void ZoomCanvas(float cale){
		scale=cale;
		invalidate();
	 }	  
	 /**
	  * @Description(����):    �л���״�������¼�
	  * @Package(����): com.surveymapclient.view
	  * @ClassName(����): TypeChangeListener 
	  * @author(����): Pang
	  * @date(ʱ��): 2016-4-4 ����9:08:10 
	  * @version(�汾): V1.0
	  */
	 public interface TypeChangeListener{
		 public void onTypeChange();
	 }
	 
	 private TypeChangeListener typeListener;
	 public void setOnTypeChangeListener(TypeChangeListener listener){
		 this.typeListener=listener;
	 }

}
