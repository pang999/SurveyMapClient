package com.surveymapclient.activity;
import com.surveymapclient.common.IToast;
import com.surveymapclient.common.Logger;
import com.surveymapclient.entity.CouplePointLineBean;
import com.surveymapclient.impl.AlterlineCallBack;
import com.surveymapclient.view.DefineView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LineAttributeActivity extends Activity{
	
	EditText lineName,lineLength,lineAngle,lineDescripte;
	TextView lineColor,lineStyle,lineWidth;
	int index;
	boolean isfull=false;
	int linecolor=0;
	private float width=0;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attributelist);
		lineName=(EditText) findViewById(R.id.editlineName);
		lineLength=(EditText) findViewById(R.id.editlineLength);
		lineAngle=(EditText) findViewById(R.id.editlineAngle);
		lineColor=(TextView) findViewById(R.id.editlineColor);
		lineWidth=(TextView) findViewById(R.id.editlineWidth);
		lineStyle=(TextView) findViewById(R.id.editlineStyle);
		lineDescripte=(EditText) findViewById(R.id.editlineDescripte);
		Bundle bundle=this.getIntent().getExtras();
		index=bundle.getInt("i");
		lineName.setText(bundle.getString("name"));
		lineLength.setText(bundle.getDouble("length")+"");
		lineAngle.setText(bundle.getDouble("angle")+"");
		int c=bundle.getInt("color");
		if (c==Color.RED) {
			lineColor.setText("��ɫ");
			linecolor=Color.RED;
		}else if (c==Color.BLUE) {
			lineColor.setText("��ɫ");
			linecolor=Color.BLUE;
		}else if (c==Color.GREEN) {
			lineColor.setText("��ɫ");
			linecolor=Color.GREEN;
		}	
		int linecolor=c;
		String ssString=null;
		if (linecolor==Color.RED) {
			ssString="��ɫ";
		}else if (linecolor==Color.BLUE) {
			ssString="��ɫ";
		}else if (linecolor==Color.GREEN) {
			ssString="��ɫ";
		}
//		Logger.i("ɫ����", "----------->color="+ssString+",width="+couple.getWidth()+",style="+couple.isFull());
		lineWidth.setText(bundle.getFloat("width")+"");
		width=bundle.getFloat("width");
		if (bundle.getBoolean("style")) {
			lineStyle.setText("ʵ��");
			isfull=true;
		}else {
			lineStyle.setText("����");
			isfull=false;
		}
		
		lineDescripte.setText(bundle.getString("descripte"));
	}
	
	public void AttributeBack(View v){
		finish();
	}

	public void EditComplete(View v){
		Bundle bundle = new Bundle();
		bundle.putString("backName", lineName.getText().toString());		
		bundle.putInt("backIndex", index);
		bundle.putInt("backColor", linecolor);
		bundle.putDouble("backLenght", Double.parseDouble(lineLength.getText().toString()));
		bundle.putFloat("backWidth",width);
		bundle.putDouble("backAngle", Double.parseDouble(lineAngle.getText().toString()));			
		bundle.putBoolean("backStyle", isfull);
		bundle.putString("backDesc", lineDescripte.getText().toString());
		LineAttributeActivity.this.setResult(RESULT_OK, this.getIntent()
					.putExtras(bundle));
//		String ssString=null;
//		if (linecolor==Color.RED) {
//			ssString="��ɫ";
//		}else if (linecolor==Color.BLUE) {
//			ssString="��ɫ";
//		}else if (linecolor==Color.GREEN) {
//			ssString="��ɫ";
//		}
//		Logger.i("ɫ����", "------line-------��color="+ssString+",width="+width+",style="+isfull);
		finish();
	}
	public void onSetStyle(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(LineAttributeActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] linestyle = {"ʵ��", "����"};
		builder.setTitle("ѡ��ʵ/����");
        //    ����һ���������б�ѡ����
		builder.setItems(linestyle, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				if (linestyle[i]=="ʵ��") {
					isfull=true;
				}else if (linestyle[i]=="����") {
					isfull=false;
				}
				lineStyle.setText(linestyle[i]);
			}
		});
		builder.show();
	}
	public void onSetWidth(View v){
		AlertDialog.Builder builder=new AlertDialog.Builder(LineAttributeActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] w = {"2", "4", "6","8"};
		builder.setTitle("ѡ��ֱ�ߴ�С");
        //    ����һ���������б�ѡ����
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
		AlertDialog.Builder builder=new AlertDialog.Builder(LineAttributeActivity.this);
		builder.setIcon(R.drawable.ic_launcher);
		//	    ָ�������б����ʾ����
		final String[] scolor = {"��ɫ", "��ɫ", "��ɫ"};
		builder.setTitle("ѡ��һ����ɫ");
        //    ����һ���������б�ѡ����
		builder.setItems(scolor, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub				
				if (scolor[i]=="��ɫ") {
					linecolor=Color.RED;
				}else if (scolor[i]=="��ɫ") {
					linecolor=Color.BLUE;
				}else if (scolor[i]=="��ɫ") {
					linecolor=Color.GREEN;
				}
				lineColor.setText(scolor[i]);
			}
		});
		builder.show();
	}
}
