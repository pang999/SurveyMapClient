package com.surveymapclient.view;

import java.util.List;

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

public class DefineViewPopupWindow extends PopupWindow {
	
	View conentView;
	MagnifierDefineView mDefineView;
	
	@SuppressLint("InflateParams")
	public DefineViewPopupWindow(final Activity activity) {
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) activity  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		conentView=inflater.inflate(R.layout.popupwindowdefine, null);
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
        mDefineView=(MagnifierDefineView) conentView.findViewById(R.id.magnifierdefine);
	}
	public void AddAllDataFromActivity(
			List<LineBean> linelist,
			List<PolygonBean> polygonlist,
			List<TextBean> textlist,
			List<AudioBean> audiolist){
		mDefineView.AddAllDataFromActivity(
				linelist, 
				polygonlist, 
				textlist, 
				audiolist);
	}
	public void DrawDown(float x,float y){
		mDefineView.Aciton_Down(x, y);
	}
	public void DrawMove(float x,float y){
		mDefineView.Action_Move(x, y);
	}
	public void DrawUp(float ex,float ey){
		mDefineView.Action_Up(ex,ey);
	}
	public void DownlongTime(boolean islong,boolean isyes){
		mDefineView.mIsLongPressed=islong;
		mDefineView.Yes=isyes;
	}
	public void DeleteLine(int i){
		mDefineView.DeleteLineIndex(i);
	}
	public void MoveLineUp(LineBean lineBean){
		mDefineView.MoveLineUp(lineBean);
	}
	public void DeletePolygon(int i){
		mDefineView.DeletePolygonIndex(i);
	}
	public void MovePolygonUp(PolygonBean polygonBean){
		mDefineView.MovePolygonUp(polygonBean);
	}
	public void DeletePolygonLine(int n,int i){
		mDefineView.DeletePolygonLineIndex(n, i);
	}
	public void ChangeLineAttribute(int index,LineBean line){
		mDefineView.ChangeLineAttribute(index, line);
	}
	public void ChangePolygonLineAttribute(int n, int index,LineBean line){
		mDefineView.ChangePolygonLineAttribute(n, index, line);
	}
	public void ChangeTextContent(int index,String text){
		mDefineView.ChangeTextContent(index, text);
	}
	public void setManyTextOnView(float x,float y){
		mDefineView.setManyTextOnView(x, y);
	}
	public void setAudoiOnView(float x,float y,String url,int len){
		mDefineView.setAudioOnView(x, y, url, len);
	}
	public void DeleteText(int index){
		mDefineView.DeleteText(index);
	}
	public void DeleteAudio(int index){
		mDefineView.DeleteAudio(index);
	}
	public void MoveTextUp(TextBean textBean){
		mDefineView.MoveTextUp(textBean);
	}
	public void MoveAudioUp(AudioBean audioBean){
		mDefineView.MoveAudioUp(audioBean);
	}
}
