package com.surveymapclient.view;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;
import com.surveymapclient.activity.adapter.HistoryAdapter;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MovePopupWindow extends PopupWindow implements OnClickListener{

	View conentView;
	Activity activity;
	LinearLayout datelist,datashare,datahistory;
	public MovePopupWindow(final Activity context){
		activity=context;
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(R.layout.popupmovewindow, null);  
        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
        // 设置SelectPicPopupWindow的View  
        this.setContentView(conentView);  
        // 设置SelectPicPopupWindow弹出窗体的宽  
//<<<<<<< HEAD
        this.setWidth(LayoutParams.WRAP_CONTENT);  
/*=======
        this.setWidth(w /3 + 50);  
>>>>>>> 5f0e3822ab87cb2fc0682396807aa3879550ccdc*/
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
		DefineActivity defineActivity=(DefineActivity) activity;
		switch (v.getId()) {
		case R.id.datalist:
			defineActivity.onDatalist();
			dismiss();
			break;

		case R.id.datashare:
			defineActivity.ShareData();
			dismiss();
			break;
		case R.id.datahistory:
			defineActivity.HistoryData();
			dismiss();
		break;
		}
	} 

}
