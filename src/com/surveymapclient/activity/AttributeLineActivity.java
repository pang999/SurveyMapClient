package com.surveymapclient.activity;
import com.surveymapclient.common.Contants;
import com.surveymapclient.common.Logger;
import com.surveymapclient.common.ViewContans;
import com.surveymapclient.entity.LineBean;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttributeLineActivity extends Activity implements android.view.View.OnClickListener{
	
	EditText lineName,lineLength,lineAngle,lineDescripte;
	TextView lineWidth,lineColor;
	LinearLayout onSetColor;
	LinearLayout onSetWidth;
	View setcolor;
	boolean isfull=false;
	int linecolor=0;
    float linewidth=0;	
	LineBean line;
	private int type=-1;
	public static int BACKLINE=0;
	
	int SELECTCOLORBCAK=100;
	int SELECTWIDTHBCAK=200;
	Bundle getbundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_list);
		lineName=(EditText) findViewById(R.id.editlineName);
		lineLength=(EditText) findViewById(R.id.editlineLength);
		lineAngle=(EditText) findViewById(R.id.editlineAngle);
		lineWidth=(TextView) findViewById(R.id.editlineWidth);
		lineDescripte=(EditText) findViewById(R.id.editlineDescripte);
		lineColor=(TextView) findViewById(R.id.editlineColor);
		onSetColor=(LinearLayout) findViewById(R.id.onsetcolor);
		onSetWidth=(LinearLayout) findViewById(R.id.setlineWidth);
		onSetWidth.setOnClickListener(this);
		onSetColor.setOnClickListener(this);
		setcolor=findViewById(R.id.settingcolor);
		line=new LineBean();
		getbundle=this.getIntent().getExtras();
		line=(LineBean) getbundle.getSerializable("Line");
		type=getbundle.getInt("TYPE");
		lineName.setText(line.getName());
		lineLength.setText(line.getLength()+"");
		lineAngle.setText(line.getAngle()+"");
		setcolor.setBackgroundColor(line.getPaintColor());
		lineColor.setText(ViewContans.setColor(line.getPaintColor()));	
		lineWidth.setText(ViewContans.setWidth(line.getPaintWidth()));
		linecolor=line.getPaintColor();
		linewidth=line.getPaintWidth();		
		lineDescripte.setText(line.getDescripte());
	}
	
	public void AttributeBack(View v){
		finish();
	}

	public void EditComplete(View v){
		LineBean backline=new LineBean();	
		Bundle bundle = new Bundle();
		backline.setName(lineName.getText().toString());
		backline.setLength(Double.parseDouble(lineLength.getText().toString()));
		backline.setPaintColor(linecolor);
		backline.setPaintIsFull(isfull);
		backline.setPaintWidth(linewidth);
		backline.setDescripte(lineDescripte.getText().toString());
		backline.setAngle(Double.parseDouble(lineAngle.getText().toString()));
		bundle.putSerializable("BackLine", backline);
		if (type==2) {
			BACKLINE=Contants.LINEATTRIBUTEBACK;
			bundle.putInt("Index", getbundle.getInt("Index"));
			Intent intent=new Intent();			
			intent.putExtras(bundle);
			intent.setClass(this, DefineActivity.class);
			startActivity(intent);
		}else {
			AttributeLineActivity.this.setResult(RESULT_OK, this.getIntent()
					.putExtras(bundle));
		}
		Logger.i("Activity·µ»Ø", "linecolor="+ViewContans.setColor(linecolor)+",linewidth="+linewidth);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.onsetcolor:
			Intent intent=new Intent();		
			intent.putExtra("COLOR", line.getPaintColor());
			intent.setClass(AttributeLineActivity.this, SelectColorActivity.class);
			startActivityForResult(intent, SELECTCOLORBCAK);
			break;

		case R.id.setlineWidth:
			Intent intentwidth=new Intent();	
			intentwidth.putExtra("WIDTH", linewidth);
			intentwidth.setClass(AttributeLineActivity.this, SelectWidthActivity.class);
			startActivityForResult(intentwidth, SELECTWIDTHBCAK);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			if (requestCode==SELECTCOLORBCAK) {
				Bundle bundle=data.getExtras();
				linecolor=bundle.getInt("BACKCOLOR");
				setcolor.setBackgroundColor(linecolor);
				lineColor.setText(ViewContans.setColor(linecolor));				
			}
			if (requestCode==SELECTWIDTHBCAK) {
				Bundle bundle=data.getExtras();
				linewidth=bundle.getFloat("BACKWIDTH");
				lineWidth.setText(ViewContans.setWidth(linewidth));
			}
		}
	}
}
