package com.surveymapclient.activity;

import com.surveymapclient.common.Logger;
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

public class AttributeRectangleActivity extends Activity {

	EditText rectName,rectArea,rectLenght,rectWidth,rectDecs;
	TextView lineColor,lineStyle,lineWidth;
	boolean isfull=false;
	int rectlinecolor=0;
	private float width=0;	
	RectangleBean rectangle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_rectangle);
		rectName=(EditText) findViewById(R.id.editrectangleName);
		rectArea=(EditText) findViewById(R.id.editrectangleArea);
		rectLenght=(EditText) findViewById(R.id.editrectangleLengh);
		rectWidth=(EditText) findViewById(R.id.editrectanglewidth);
		rectDecs=(EditText) findViewById(R.id.editlineDescripte);
		lineColor=(TextView) findViewById(R.id.editrectlinecolor);
		lineWidth=(TextView) findViewById(R.id.editrectlinewidth);
		lineStyle=(TextView) findViewById(R.id.editrectlineStyle);
		rectangle=new RectangleBean();
		Bundle bundle=this.getIntent().getExtras();
		rectangle=(RectangleBean) bundle.getSerializable("Rectangle");
		rectName.setText(rectangle.getRectName());
		rectDecs.setText(rectangle.getDescripte());
		rectArea.setText(rectangle.getRectArea()+"");
		rectLenght.setText(rectangle.getRectLenght()+"");
		rectWidth.setText(rectangle.getRectWidth()+"");
		int c=rectangle.getPaintColor();
		if (c==Color.RED) {
			lineColor.setText("红色");
			rectlinecolor=Color.RED;
		}else if (c==Color.BLUE) {
			lineColor.setText("蓝色");
			rectlinecolor=Color.BLUE;
		}else if (c==Color.GREEN) {
			lineColor.setText("绿色");
			rectlinecolor=Color.GREEN;
		}else if (c==Color.BLACK) {
			lineColor.setText("黑色");
			rectlinecolor=Color.BLACK;
		}
		lineWidth.setText(rectangle.getPaintWidth()+"");
		width=rectangle.getPaintWidth();
		if (rectangle.isFull()) {
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
		RectangleBean backrect=new RectangleBean();
		Bundle bundle = new Bundle();
		backrect.setRectName(rectName.getText().toString());
		backrect.setRectArea(Double.parseDouble(rectArea.getText().toString()));
		backrect.setRectLenght(Float.parseFloat(rectLenght.getText().toString()));
		backrect.setRectWidth(Float.parseFloat(rectWidth.getText().toString()));
		backrect.setDescripte(rectDecs.getText().toString());
		backrect.setPaintColor(rectlinecolor);
		backrect.setPaintWidth(width);
		backrect.setFull(isfull);
		bundle.putSerializable("BackRectangle", backrect);
		AttributeRectangleActivity.this.setResult(RESULT_OK, 
				this.getIntent().putExtras(bundle));
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeRectangleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeRectangleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeRectangleActivity.this);
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
					rectlinecolor=Color.RED;
				}else if (scolor[i]=="蓝色") {
					rectlinecolor=Color.BLUE;
				}else if (scolor[i]=="绿色") {
					rectlinecolor=Color.GREEN;
				}else if (scolor[i]=="黑色") {
					rectlinecolor=Color.BLACK;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
