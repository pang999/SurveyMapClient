package com.surveymapclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SelectWidthActivity extends FragmentActivity implements OnClickListener {

	LinearLayout linear_check_width_1,linear_check_width_2,
	linear_check_width_3,linear_check_width_4,linear_check_width_5;
	CheckBox check_width_1,check_width_2,check_width_3,check_width_4,check_width_5;
	ImageView btnBack;
	Button editComplete;
	float width;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_selectwidth);
		initView();
		Bundle bundle=getIntent().getExtras();
		width=bundle.getFloat("WIDTH");
		if (width==2) {
			check_width_1.setChecked(true);
		}else if (width==4) {
			check_width_2.setChecked(true);
		}else if (width==6) {
			check_width_3.setChecked(true);
		}else if (width==8) {
			check_width_4.setChecked(true);
		}else if (width==10) {
			check_width_5.setChecked(true);
		}
		
	}
	void initView(){
		btnBack=(ImageView) findViewById(R.id.btnback);
		editComplete=(Button) findViewById(R.id.editcomplete);
		linear_check_width_1=(LinearLayout) findViewById(R.id.linear_check_width_1);
		linear_check_width_2=(LinearLayout) findViewById(R.id.linear_check_width_2);
		linear_check_width_3=(LinearLayout) findViewById(R.id.linear_check_width_3);
		linear_check_width_4=(LinearLayout) findViewById(R.id.linear_check_width_4);
		linear_check_width_5=(LinearLayout) findViewById(R.id.linear_check_width_5);
		check_width_1=(CheckBox) findViewById(R.id.check_width_1);
		check_width_2=(CheckBox) findViewById(R.id.check_width_2);
		check_width_3=(CheckBox) findViewById(R.id.check_width_3);
		check_width_4=(CheckBox) findViewById(R.id.check_width_4);
		check_width_5=(CheckBox) findViewById(R.id.check_width_5);
		check_width_1.setClickable(false);
		check_width_2.setClickable(false);
		check_width_3.setClickable(false);
		check_width_4.setClickable(false);
		check_width_5.setClickable(false);
		btnBack.setOnClickListener(this);
		editComplete.setOnClickListener(this);
		linear_check_width_1.setOnClickListener(this);
		linear_check_width_2.setOnClickListener(this);
		linear_check_width_3.setOnClickListener(this);
		linear_check_width_4.setOnClickListener(this);
		linear_check_width_5.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnback:
			finish();
			break;

		case R.id.editcomplete:
			Bundle bundle = new Bundle();
			bundle.putFloat("BACKWIDTH", width);
			this.setResult(RESULT_OK, this.getIntent().putExtras(bundle));
			finish();
			break;
	
		case R.id.linear_check_width_1:
			width=2;
			check_width_1.setChecked(true);
			check_width_2.setChecked(false);
			check_width_3.setChecked(false);
			check_width_4.setChecked(false);
			check_width_5.setChecked(false);
			break;
		case R.id.linear_check_width_2:
			width=4;
			check_width_1.setChecked(false);
			check_width_2.setChecked(true);
			check_width_3.setChecked(false);
			check_width_4.setChecked(false);
			check_width_5.setChecked(false);
			break;
		case R.id.linear_check_width_3:
			width=6;
			check_width_1.setChecked(false);
			check_width_2.setChecked(false);
			check_width_3.setChecked(true);
			check_width_4.setChecked(false);
			check_width_5.setChecked(false);
			break;
		case R.id.linear_check_width_4:
			width=8;
			check_width_1.setChecked(false);
			check_width_2.setChecked(false);
			check_width_3.setChecked(false);
			check_width_4.setChecked(true);
			check_width_5.setChecked(false);
			break;
		case R.id.linear_check_width_5:
			width=10;
			check_width_1.setChecked(false);
			check_width_2.setChecked(false);
			check_width_3.setChecked(false);
			check_width_4.setChecked(false);
			check_width_5.setChecked(true);
			break;
		}
	}
}
