package com.surveymapclient.activity.welcome;

import com.surveymapclient.activity.HomeActivity;
import com.surveymapclient.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 */
public class StartAnimation extends Activity {

	 boolean isFirstIn = false;
	 private static final String SHAREDPREFERENCES_NAME = "first_pref";
	 SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.startanimation, null);
		setContentView(view);
	    preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean(SHAREDPREFERENCES_NAME, true);
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				redirectTo();
			}
		});

	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		  if (!isFirstIn) {
	            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
	            startActivity(new Intent(this, HomeActivity.class));
	            finish();
	        } else {
	            SharedPreferences.Editor editor=preferences.edit();
	            editor.putBoolean(SHAREDPREFERENCES_NAME,false);
	            editor.commit();
	           startActivity(new Intent(this, Pages.class));
	           finish();
	        }
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
