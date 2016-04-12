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
        // ����SelectPicPopupWindow��View  
        this.setContentView(conentView);  
        // ����SelectPicPopupWindow��������Ŀ�  
//<<<<<<< HEAD
        this.setWidth(LayoutParams.WRAP_CONTENT);  
/*=======
        this.setWidth(w /3 + 50);  
>>>>>>> 5f0e3822ab87cb2fc0682396807aa3879550ccdc*/
        // ����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // ����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        datelist=(LinearLayout) conentView.findViewById(R.id.datalist);
        datashare=(LinearLayout) conentView.findViewById(R.id.datashare);
        datahistory=(LinearLayout) conentView.findViewById(R.id.datahistory);
        datelist.setOnClickListener(this);
        datashare.setOnClickListener(this);
        datahistory.setOnClickListener(this);
        // ˢ��״̬  
        this.update();  
        // ʵ����һ��ColorDrawable��ɫΪ��͸��  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        // ��back���������ط�ʹ����ʧ,������������ܴ���OnDismisslistener �����������ؼ��仯�Ȳ���  
        this.setBackgroundDrawable(dw);  
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        // ����SelectPicPopupWindow�������嶯��Ч��  
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
