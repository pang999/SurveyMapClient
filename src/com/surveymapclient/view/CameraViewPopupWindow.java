package com.surveymapclient.view;

import com.surveymapclient.activity.R;
import com.surveymapclient.entity.AudioBean;
import com.surveymapclient.entity.LineBean;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.entity.TextBean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

public class CameraViewPopupWindow extends PopupWindow {
	
	View conentView;
	MagnifierCameraView mCameraView;
	
	@SuppressLint("InflateParams")
	public CameraViewPopupWindow(final Activity activity) {
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) activity  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		conentView=inflater.inflate(R.layout.popupwindowcamera, null);
		this.setContentView(conentView);
		// 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);
		this.setWidth(200);
		this.setHeight(200);
		// 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw); 
        mCameraView=(MagnifierCameraView) conentView.findViewById(R.id.magnifiercamera);
	}
	
	public void DrawDown(float x,float y){
		mCameraView.Aciton_Down(x, y);
	}
	public void DrawMove(float x,float y){
		mCameraView.Action_Move(x, y);
	}
	public void DrawUp(float ex,float ey){
		mCameraView.Action_Up(ex,ey);
	}
	public void DownlongTime(boolean islong,boolean isyes){
		mCameraView.mIsLongPressed=islong;
		mCameraView.Yes=isyes;
	}
	public void DeleteLine(int i){
		mCameraView.DeleteLineIndex(i);
	}
	public void MoveLineUp(LineBean lineBean){
		mCameraView.MoveLineUp(lineBean);
	}
	public void DeletePolygon(int i){
		mCameraView.DeletePolygonIndex(i);
	}
	public void MovePolygonUp(PolygonBean polygonBean){
		mCameraView.MovePolygonUp(polygonBean);
	}
	public void DeletePolygonLine(int n,int i){
		mCameraView.DeletePolygonLineIndex(n, i);
	}
	public void ChangeLineAttribute(int index,LineBean line){
		mCameraView.ChangeLineAttribute(index, line);
	}
	public void ChangePolygonLineAttribute(int n, int index,LineBean line){
		mCameraView.ChangePolygonLineAttribute(n, index, line);
	}
	public void ChangeTextContent(int index,String text){
		mCameraView.ChangeTextContent(index, text);
	}
	public void setManyTextOnView(float x,float y){
		mCameraView.setManyTextOnView(x, y);
	}
	public void setAudoiOnView(float x,float y,String url,int len){
		mCameraView.setAudioOnView(x, y, url, len);
	}
	public void DeleteText(int index){
		mCameraView.DeleteText(index);
	}
	public void DeleteAudio(int index){
		mCameraView.DeleteAudio(index);
	}
	public void MoveTextUp(TextBean textBean){
		mCameraView.MoveTextUp(textBean);
	}
	public void MoveAudioUp(AudioBean audioBean){
		mCameraView.MoveAudioUp(audioBean);
	}

}
