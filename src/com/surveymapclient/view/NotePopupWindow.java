package com.surveymapclient.view;

import com.surveymapclient.activity.DefineActivity;
import com.surveymapclient.activity.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

public class NotePopupWindow extends PopupWindow{

	private View conentView;

	public NotePopupWindow(final Activity context) {
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(R.layout.popupeditewindow, null);  
        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
        // ����SelectPicPopupWindow��View  
        this.setContentView(conentView);  
        // ����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(w / 3);  
        // ����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // ����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        // ˢ��״̬  
        this.update();  
        Button edittext=(Button) conentView.findViewById(R.id.editetext);
        Button tape=(Button) conentView.findViewById(R.id.tape);
        edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotePopupWindow.this.dismiss();
				DefineActivity defineActivity=(DefineActivity) context;
				defineActivity.showEditeView();
			}
		});
        tape.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NotePopupWindow.this.dismiss();
			}
		});
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
	
}
