package com.surveymapclient.activity.base;

import com.surveymapclient.activity.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // �޸�״̬����ɫ��4.4+��Ч
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus();
//        }
//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//        tintManager.setStatusBarTintEnabled(true);
//        tintManager.setStatusBarTintResource(R.color.status_bar_bg);//֪ͨ��������ɫ
	}
	
	 @TargetApi(19)
	    protected void setTranslucentStatus() {
	        Window window = getWindow();
	        // Translucent status bar
	        window.setFlags(
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
	                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	    }

}
