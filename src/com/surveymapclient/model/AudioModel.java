package com.surveymapclient.model;

import java.util.ArrayList;
import java.util.List;
import com.surveymapclient.activity.R;
import com.surveymapclient.entity.AudioBean;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

public class AudioModel {
	Context mContext;
	public List<AudioBean> GetAudiolist=new ArrayList<AudioBean>();
	private PointF movePoint=new PointF();
	private AudioBean audio=new AudioBean();
	Bitmap bitmap;
	public AudioModel(Context context) {
		// TODO Auto-generated constructor stub
		mContext=context;
		bitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);

	}
	
	
	float mx,my;
	
	public void DrawMoveAudio(Canvas canvas){
		canvas.drawBitmap(bitmap, mx, my, null);
	}

	public void DrawAudiosOnBitmap(List<AudioBean> list,Canvas canvas){
		for (int i = 0; i < list.size(); i++) {
			canvas.drawBitmap(bitmap, list.get(i).getAx(), list.get(i).getAy(), null);
		}
	}
	public void Audio_touch_up(Canvas canvas,float x,float y,String audioUrl,int len){
		canvas.drawBitmap(bitmap, x, y, null);
		AddAudioParams(GetAudiolist, x, y,audioUrl,len);
	}
	public void AddAudioParams(List<AudioBean> list,float x,float y,String url,int len){
		AudioBean audioBean=new AudioBean();
		audioBean.setUrl(url);
		audioBean.setAx(x);
		audioBean.setAy(y);
		audioBean.setLenght(len);
		list.add(audioBean);
	}
	public void MoveAudio_dowm(List<AudioBean> list,int i,float rx,float ry){
		mx=list.get(i).getAx();
		my=list.get(i).getAy();
		movePoint.set(rx, ry);
		audio.setUrl(list.get(i).getUrl());
		audio.setLenght(list.get(i).getLenght());
		list.remove(i);
	}
	public void MoveAudio_move(float rx,float ry){
		float dx=rx-movePoint.x;
	    float dy=ry-movePoint.y;	    	
	    if (Math.sqrt(dx*dx+dy*dy)>5f) {
	    	movePoint.set(rx, ry);
	    	mx=mx+dx;
	    	my=my+dy;
	    }
	}

	public void MoveAudio_up(Canvas canvas){
		canvas.drawBitmap(bitmap, mx, my, null);
		AddAudioParams(GetAudiolist, mx, my, audio.getUrl(),audio.getLenght());
	}
	public int PitchOnAudio(List<AudioBean> list,float x,float y){
		int dx=(int) x;
		int dy=(int) y;
		for (int i = 0; i < list.size(); i++) {
			int ax=(int) list.get(i).getAx();
			int ay=(int) list.get(i).getAy();
			boolean xCan=(bitmap.getWidth()+ax>dx)&&(ax<dx);
			boolean yCan=(bitmap.getHeight()+ay>dy)&&(ay<dy);
			if (xCan&&yCan) {
				return i;
			}
		}
		return -1;
	}
}

