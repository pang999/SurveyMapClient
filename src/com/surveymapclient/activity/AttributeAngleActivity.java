package com.surveymapclient.activity;

import com.surveymapclient.entity.AngleBean;
import com.surveymapclient.entity.LineBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AttributeAngleActivity extends Activity {

	EditText angleName,angle,angleDescripte;
	TextView lineColor,lineStyle,lineWidth;
	boolean isfull=false;
	int linecolor=0;
	private float width=0;	
	AngleBean anglebean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute_angle);
		angleName=(EditText) findViewById(R.id.editangleName);
		angle=(EditText) findViewById(R.id.editangle);
		angleDescripte=(EditText) findViewById(R.id.editangleDescripte);
		lineColor=(TextView) findViewById(R.id.editanglelineColor);
		lineWidth=(TextView) findViewById(R.id.editanglelineWidth);
		lineStyle=(TextView) findViewById(R.id.editanglelineStyle);
		anglebean=new AngleBean();
		Bundle bundle=this.getIntent().getExtras();
		anglebean=(AngleBean) bundle.getSerializable("Angle");
		angleName.setText(anglebean.getName());
		angle.setText(anglebean.getAngle()+"");
		angleDescripte.setText(anglebean.getDescripte());
		int c=anglebean.getPaintColor();
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
		lineWidth.setText(anglebean.getPaintWidth()+"");
		width=anglebean.getPaintWidth();
		if (anglebean.isPaintIsFull()) {
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
		AngleBean backangle=new AngleBean();
		Bundle bundle = new Bundle();
		backangle.setName(angleName.getText().toString());
		backangle.setAngle(Double.parseDouble(angle.getText().toString()));
		backangle.setDescripte(angleDescripte.getText().toString());
		backangle.setPaintColor(linecolor);
		backangle.setPaintWidth(width);
		backangle.setPaintIsFull(isfull);
		bundle.putSerializable("BackAngle", backangle);
		AttributeAngleActivity.this.setResult(RESULT_OK, this.getIntent()
					.putExtras(bundle));
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(AttributeAngleActivity.this);
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
				}else if(scolor[i]=="黑色"){
					linecolor=Color.BLACK;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
