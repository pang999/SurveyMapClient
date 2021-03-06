package com.surveymapclient.view;

import com.surveymapclient.activity.CameraActivity;
import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class MorePopupWindow extends PopupWindow implements OnClickListener{

	View conentView;
	Activity activity;
	int mType;
	LinearLayout datelist,datashare,datahistory;
	public MorePopupWindow(final Activity context,int type){
		mType=type;
		activity=context;
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(R.layout.popupwindowmore, null);  
        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
        // 设置SelectPicPopupWindow的View  
        this.setContentView(conentView);  
        // 设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.WRAP_CONTENT);  
        this.setWidth(w /3 + 50);  
        // 设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        datelist=(LinearLayout) conentView.findViewById(R.id.datalist);
        datashare=(LinearLayout) conentView.findViewById(R.id.datashare);
        datahistory=(LinearLayout) conentView.findViewById(R.id.datahistory);
        datelist.setOnClickListener(this);
        datashare.setOnClickListener(this);
        datahistory.setOnClickListener(this);
        // 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw);  
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        // 设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimationPreview); 
	}
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);  
        } else {  
            this.dismiss();  
        }  
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch (v.getId()) {
		case R.id.datalist:
			if (mType==0) {
				DefineActivity defineActivity=(DefineActivity) activity;
				defineActivity.onDatalist();
			}else if (mType==1) {
				CameraActivity cameraActivity=(CameraActivity) activity;
				cameraActivity.onDatalist();
			}			
			dismiss();
			break;

		case R.id.datashare:
			if (mType==0) {
				DefineActivity defineActivity=(DefineActivity) activity;
				defineActivity.ShareData();
			}else if (mType==1) {
				CameraActivity cameraActivity=(CameraActivity) activity;
				
			}	
			dismiss();
			break;
		case R.id.datahistory:
			if (mType==0) {
				DefineActivity defineActivity=(DefineActivity) activity;
				defineActivity.HistoryData();
			}else if (mType==1) {
				CameraActivity cameraActivity=(CameraActivity) activity;
				cameraActivity.HistoryData();
			}	
			dismiss();
		break;
		}
	} 

}
