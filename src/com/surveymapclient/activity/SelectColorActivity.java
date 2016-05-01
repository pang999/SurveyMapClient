package com.surveymapclient.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SelectColorActivity extends FragmentActivity implements OnClickListener {

	RelativeLayout relative_check_black,relative_check_white,
	relative_check_red,relative_check_green,relative_check_blue,relative_check_yellow;
	CheckBox check_black,check_white,check_red,check_green,check_blue,check_yellow;
	ImageView btnBack;
	Button editComplete;
	int color;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_selectcolor);
		iniView();
		Bundle bundle=getIntent().getExtras();
		color= bundle.getInt("COLOR");
		if (color==Color.BLACK) {
			check_black.setChecked(true);
		}else if(color==Color.WHITE){
			check_white.setChecked(true);
		}else if(color==Color.RED){
			check_red.setChecked(true);
		}else if(color==Color.GREEN){
			check_green.setChecked(true);
		}else if(color==Color.BLUE){
			check_blue.setChecked(true);
		}else if(color==Color.YELLOW){
			check_yellow.setChecked(true);
		}
		
	}
	void iniView(){
		btnBack=(ImageView) findViewById(R.id.btnback);
		editComplete=(Button) findViewById(R.id.editcomplete);
		relative_check_black=(RelativeLayout) findViewById(R.id.relative_check_black);
		relative_check_white=(RelativeLayout) findViewById(R.id.relative_check_white);
		relative_check_red=(RelativeLayout) findViewById(R.id.relative_check_red);
		relative_check_green=(RelativeLayout) findViewById(R.id.relative_check_green);
		relative_check_blue=(RelativeLayout) findViewById(R.id.relative_check_blue);
		relative_check_yellow=(RelativeLayout) findViewById(R.id.relative_check_yellow);
		check_black=(CheckBox) findViewById(R.id.check_black);
		check_white=(CheckBox) findViewById(R.id.check_white);
		check_red=(CheckBox) findViewById(R.id.check_red);
		check_green=(CheckBox) findViewById(R.id.check_green);
		check_blue=(CheckBox) findViewById(R.id.check_blue);
		check_yellow=(CheckBox) findViewById(R.id.check_yellow);
		check_black.setClickable(false);
		check_red.setClickable(false);
		check_green.setClickable(false);
		check_blue.setClickable(false);
		check_yellow.setClickable(false);
		btnBack.setOnClickListener(this);
		editComplete.setOnClickListener(this);
		relative_check_black.setOnClickListener(this);
		relative_check_white.setOnClickListener(this);
		relative_check_red.setOnClickListener(this);
		relative_check_green.setOnClickListener(this);
		relative_check_blue.setOnClickListener(this);
		relative_check_yellow.setOnClickListener(this);
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
			bundle.putInt("BACKCOLOR", color);
			this.setResult(RESULT_OK, this.getIntent().putExtras(bundle));
			finish();
			break;
	
		case R.id.relative_check_black:
			color=Color.BLACK;
			check_black.setChecked(true);
			check_white.setChecked(false);
			check_red.setChecked(false);
			check_green.setChecked(false);
			check_blue.setChecked(false);
			check_yellow.setChecked(false);
			break;
		case R.id.relative_check_white:
			color=Color.WHITE;
			check_black.setChecked(false);
			check_white.setChecked(true);
			check_red.setChecked(false);
			check_green.setChecked(false);
			check_blue.setChecked(false);
			check_yellow.setChecked(false);
			break;
		case R.id.relative_check_red:
			color=Color.RED;
			check_black.setChecked(false);
			check_white.setChecked(false);
			check_red.setChecked(true);
			check_green.setChecked(false);
			check_blue.setChecked(false);
			check_yellow.setChecked(false);
			break;
		case R.id.relative_check_green:
			color=Color.GREEN;
			check_black.setChecked(false);
			check_white.setChecked(false);
			check_red.setChecked(false);
			check_green.setChecked(true);
			check_blue.setChecked(false);
			check_yellow.setChecked(false);
			break;
		case R.id.relative_check_blue:
			color=Color.BLUE;
			check_black.setChecked(false);
			check_white.setChecked(false);
			check_red.setChecked(false);
			check_green.setChecked(false);
			check_blue.setChecked(true);
			check_yellow.setChecked(false);
			break;
		case R.id.relative_check_yellow:
			color=Color.YELLOW;
			check_black.setChecked(false);
			check_white.setChecked(false);
			check_red.setChecked(false);
			check_green.setChecked(false);
			check_blue.setChecked(false);
			check_yellow.setChecked(true);
			break;
		}
		
	}
}
