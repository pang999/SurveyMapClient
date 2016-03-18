package com.surveymapclient.view;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.surveymapclient.activity.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class HistPopupWindow extends PopupWindow {

	View conentView;
	
	public HistPopupWindow(final Activity context){
		LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        conentView = inflater.inflate(R.layout.historypopupwindow, null);  
        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
        // ����SelectPicPopupWindow��View  
        this.setContentView(conentView);  
        // ����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(w / 2 + 50);  
        // ����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        // ����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        ListView listView=(ListView) conentView.findViewById(R.id.list_history_item);
        List<String> data = new ArrayList<String>();
        data.add("��ʷ����1");
        data.add("��ʷ����2");
        data.add("��ʷ����3");
        data.add("��ʷ����4");
        data.add("��ʷ����5");
        data.add("��ʷ����6");
        data.add("��ʷ����7");
        data.add("��ʷ����8");
        data.add("��ʷ����9");
        data.add("��ʷ����10");
        data.add("��ʷ����11");
        data.add("��ʷ����12");
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,data));
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

}
