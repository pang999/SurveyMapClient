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
 * Ӧ�ó��������ࣺ��ʾ��ӭ���沢��ת��������
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
        // ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
        isFirstIn = preferences.getBoolean(SHAREDPREFERENCES_NAME, true);
        // �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
		// ����չʾ������
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
	 * ��ת��...
	 */
	private void redirectTo() {
		  if (!isFirstIn) {
	            // ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
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
