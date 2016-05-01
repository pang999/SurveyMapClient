package com.surveymapclient.activity;

import com.surveymapclient.activity.adapter.LineAdapter;
import com.surveymapclient.activity.adapter.PolylineAdapter;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.PolygonBean;
import com.surveymapclient.utils.SubListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AttributePolygonActivity extends Activity implements android.view.View.OnClickListener {
	EditText polygonName,polygonDesc;
	LinearLayout onsetcolor;
	View setColor;
	TextView polygonColor;
	SubListView listview;
	PolylineAdapter adapter;
	PolygonBean polygon;
	int polygoncolor=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_polygon);
		polygonName=(EditText) findViewById(R.id.editpolygonName);
		polygonDesc=(EditText) findViewById(R.id.editpolygonDescripte);
		polygonColor=(TextView) findViewById(R.id.editpolygoneColor);
		listview=(SubListView) findViewById(R.id.polygonlinelist);
		onsetcolor=(LinearLayout) findViewById(R.id.onsetcolor);
		onsetcolor.setOnClickListener(this);
		setColor=findViewById(R.id.settingcolor);
		polygon=new PolygonBean();
		Bundle bundle=this.getIntent().getExtras();
		polygon=(PolygonBean) bundle.getSerializable("Polygon");
		polygonName.setText(polygon.getPolyName());
		polygonDesc.setText(polygon.getDescripe());
		int c=polygon.getPolyColor();
		setColor.setBackgroundColor(c);
		polygonColor.setText(ViewContans.setColor(c));
		adapter=new PolylineAdapter(this, polygon.getPolyLine());
		listview.setAdapter(adapter);
	}
	
	public void AttributeBack(View view){
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.onsetcolor:
			Intent intent=new Intent();		
			intent.putExtra("COLOR", polygon.getPolyColor());
			intent.setClass(AttributePolygonActivity.this, SelectColorActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
