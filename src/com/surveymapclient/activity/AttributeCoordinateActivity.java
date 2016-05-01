package com.surveymapclient.activity;

import com.surveymapclient.common.Contants;
import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.LineBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AttributeCoordinateActivity extends Activity implements android.view.View.OnClickListener{

	EditText coorName,coorDecs;
	TextView coorvolum,coorlength,coorwidth,coorheight;
	RelativeLayout gotoeditcoorlength,gotoeditcoorwidth,gotoeditcoorheight;
	CoordinateBean coordinate;
	LineBean xline;
	LineBean yline;
	LineBean zline;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_coordinate);
		coorName=(EditText) findViewById(R.id.editcoorName);
		coorDecs=(EditText) findViewById(R.id.editcoorDescripte);
		coorlength=(TextView) findViewById(R.id.coorLengh);
		coorwidth=(TextView) findViewById(R.id.coorwidth);
		coorheight=(TextView) findViewById(R.id.coorheight);
		gotoeditcoorlength=(RelativeLayout) findViewById(R.id.gotoeditcoorlength);
		gotoeditcoorwidth=(RelativeLayout) findViewById(R.id.gotoeditcoorwidth);
		gotoeditcoorheight=(RelativeLayout) findViewById(R.id.gotoeditcoorheight);
		gotoeditcoorlength.setOnClickListener(this);
		gotoeditcoorwidth.setOnClickListener(this);
		gotoeditcoorheight.setOnClickListener(this);
		coordinate=new CoordinateBean();
		Bundle bundle=this.getIntent().getExtras();
		coordinate=(CoordinateBean) bundle.getSerializable("Coordinate");	
		xline=coordinate.getxLine();
		yline=coordinate.getyLine();
		zline=coordinate.getzLine();
		coorName.setText(coordinate.getName());
		coorDecs.setText(coordinate.getDescripte());
		coorvolum.setText(coordinate.getVolum()+" m³");
		coorlength.setText(coordinate.getxLine().getLength()+" m");
		coorwidth.setText(coordinate.getyLine().getLength()+" m");
		coorheight.setText(coordinate.getzLine().getLength()+" m");
		
	}
	public void AttributeBack(View v){
		finish();
	}
	public void EditComplete(View v){
		CoordinateBean backcoor=new CoordinateBean();
		Bundle bundle = new Bundle();
		backcoor.setName(coorName.getText().toString());
		backcoor.setDescripte(coorDecs.getText().toString());
		backcoor.setVolum(Double.parseDouble(coorvolum.getText().toString()));
		backcoor.setxLine(xline);
		backcoor.setyLine(yline);
		backcoor.setzLine(zline);
		bundle.putSerializable("BackCoordinate", backcoor);
		AttributeCoordinateActivity.this.setResult(RESULT_OK, 
				this.getIntent().putExtras(bundle));
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.gotoeditcoorlength:
			Intent intentlength = new Intent();
			Bundle bundlelength = new Bundle();
			bundlelength.putSerializable("Line", coordinate.getxLine());
			bundlelength.putInt("TYPE", 1);
			intentlength.putExtras(bundlelength);
			intentlength.setClass(AttributeCoordinateActivity.this, AttributeLineActivity.class);
			startActivityForResult(intentlength, Contants.LENGTHLINEATTRIBUTEBACK);
			break;
		case R.id.gotoeditcoorwidth:
			Intent intentwidth = new Intent();
			Bundle bundlewidth = new Bundle();
			bundlewidth.putSerializable("Line", coordinate.getxLine());
			bundlewidth.putInt("TYPE", 1);
			intentwidth.putExtras(bundlewidth);
			intentwidth.setClass(AttributeCoordinateActivity.this, AttributeLineActivity.class);
			startActivityForResult(intentwidth, Contants.LINEATTRIBUTEBACK);
			break;
		case R.id.gotoeditcoorheight:
			Intent intentheight = new Intent();
			Bundle bundleheight = new Bundle();
			bundleheight.putSerializable("Line", coordinate.getxLine());
			bundleheight.putInt("TYPE", 1);
			intentheight.putExtras(bundleheight);
			intentheight.setClass(AttributeCoordinateActivity.this, AttributeLineActivity.class);
			startActivityForResult(intentheight, Contants.LINEATTRIBUTEBACK);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			if (requestCode==Contants.LENGTHLINEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
			    xline = (LineBean) bundle.getSerializable("BackLine");
				coorlength.setText(xline.getLength()+" m");
				coorvolum.setText(xline.getLength()*yline.getLength()*zline.getLength()+" m³");
			}
			if (requestCode==Contants.WIDTHLINEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
			    yline = (LineBean) bundle.getSerializable("BackLine");
				coorwidth.setText(yline.getLength()+" m");	
				coorvolum.setText(xline.getLength()*yline.getLength()*zline.getLength()+" m³");
			}
			if (requestCode==Contants.HEIGHTLINEATTRIBUTEBACK) {
				Bundle bundle = data.getExtras();
			    zline = (LineBean) bundle.getSerializable("BackLine");
				coorheight.setText(zline.getLength()+" m");	
				coorvolum.setText(xline.getLength()*yline.getLength()*zline.getLength()+" m³");
			}
		}
	}
}
