package com.surveymapclient.activity;

import com.surveymapclient.entity.CoordinateBean;
import com.surveymapclient.entity.RectangleBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AttributeCoordinateActivity extends Activity {

	EditText coorName,coorVolum,coorLenght,coortWidth,coorHeight,coorDecs;
	TextView lineColor,lineStyle,lineWidth;
	boolean isfull=false;
	int linecolor=0;
	private float width=0;	
	CoordinateBean coordinate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_coordinate);
		coorName=(EditText) findViewById(R.id.editcoorName);
		coorVolum=(EditText) findViewById(R.id.editcoorvolum);
		coorLenght=(EditText) findViewById(R.id.editcoorLengh);
		coortWidth=(EditText) findViewById(R.id.editcoorwidth);
		coorHeight=(EditText) findViewById(R.id.editcoorheight);
		coorDecs=(EditText) findViewById(R.id.editcoorDescripte);
		lineColor=(TextView) findViewById(R.id.editcoorlinecolor);
		lineWidth=(TextView) findViewById(R.id.editcoorlinewidth);
		lineStyle=(TextView) findViewById(R.id.editcoorlineStyle);
		coordinate=new CoordinateBean();
		Bundle bundle=this.getIntent().getExtras();
		coordinate=(CoordinateBean) bundle.getSerializable("Coordinate");
		coorName.setText(coordinate.getName());
		coorVolum.setText(coordinate.getVolum()+"");
		coorLenght.setText(coordinate.getLenght()+"");
		coortWidth.setText(coordinate.getWidth()+"");
		coorHeight.setText(coordinate.getHeight()+"");
		int c=coordinate.getPaintColor();
		if (c==Color.RED) {
			lineColor.setText("红色");
			linecolor=Color.RED;
		}else if (c==Color.BLUE) {
			lineColor.setText("蓝色");
			linecolor=Color.BLUE;
		}else if (c==Color.GREEN) {
			lineColor.setText("绿色");
			linecolor=Color.GREEN;
		}else if (c==Color.BLACK) {
			lineColor.setText("黑色");
			linecolor=Color.BLACK;
		}
		lineWidth.setText(coordinate.getPaintWidth()+"");
		width=coordinate.getPaintWidth();
		if (coordinate.isPaintIsFull()) {
			lineStyle.setText("实线");
			isfull=true;
		}else {
			lineStyle.setText("虚线");
			isfull=false;
		}
	}
	public void AttributeBack(View v){
		finish();
	}
	public void EditComplete(View v){
		CoordinateBean backcoor=new CoordinateBean();
		Bundle bundle = new Bundle();
		backcoor.setName(coorName.getText().toString());
		backcoor.setVolum(Double.parseDouble(coorVolum.getText().toString()));
		backcoor.setLenght(Float.parseFloat(coorLenght.getText().toString()));
		backcoor.setWidth(Float.parseFloat(coortWidth.getText().toString()));
		backcoor.setHeight(Float.parseFloat(coorHeight.getText().toString()));
		backcoor.setPaintColor(linecolor);
		backcoor.setPaintIsFull(isfull);
		backcoor.setPaintWidth(width);
		bundle.putSerializable("BackCoordinate", backcoor);
		AttributeCoordinateActivity.this.setResult(RESULT_OK, 
				this.getIntent().putExtras(bundle));
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeCoordinateActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    指定下拉列表的显示数据
		final String[] linestyle = {"实线", "虚线"};
		builder.setTitle("选择实/虚线");
        //    设置一个下拉的列表选择项
		builder.setItems(linestyle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				if (linestyle[i]=="实线") {
					isfull=true;
				}else if (linestyle[i]=="虚线") {
					isfull=false;
				}
				lineStyle.setText(linestyle[i]);
			}
		});
		builder.show();
	}
	public void onSetWidth(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeCoordinateActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    指定下拉列表的显示数据
		final String[] w = {"2", "4", "6","8"};
		builder.setTitle("选择直线大小");
        //    设置一个下拉的列表选择项
		builder.setItems(w, new OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				String m=w[i];
				if (m=="2") {
					width=2;
				}else if (m=="4") {
					width=4;
				}else if (m=="6") {
					width=6;
				}else if (m=="8") {
					width=8;
				}			
				lineWidth.setText(m);
			}
		});
		builder.show();
	}
	public void onSetColor(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeCoordinateActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    指定下拉列表的显示数据
		final String[] scolor = {"红色", "蓝色", "绿色","黑色"};
		builder.setTitle("选择一种颜色");
        //    设置一个下拉的列表选择项
		builder.setItems(scolor, new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub				
				if (scolor[i]=="红色") {
					linecolor=Color.RED;
				}else if (scolor[i]=="蓝色") {
					linecolor=Color.BLUE;
				}else if (scolor[i]=="绿色") {
					linecolor=Color.GREEN;
				}else if (scolor[i]=="黑色") {
					linecolor=Color.BLACK;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
